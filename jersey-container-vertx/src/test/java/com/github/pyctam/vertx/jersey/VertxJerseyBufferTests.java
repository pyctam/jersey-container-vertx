/**
 * Copyright 2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.pyctam.vertx.jersey;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Created by developer on 9/17/15.
 */
public class VertxJerseyBufferTests {
    @Test
    public void writeToBuffer() throws IOException {
        // given
        String s = "A Closeable is a source or destination of data that can be closed. The close method is invoked to release resources that the object is holding (such as open files).";
        VertxJerseyBuffer buffer = new VertxJerseyBuffer();

        // when
        buffer.getOutputStream().write(s.getBytes());

        // then
        Assert.assertEquals(s, new String(buffer.getBuffer()));
    }

    @Test
    public void readFromBuffer() throws IOException {
        // given
        String s = "A Closeable is a source or destination of data that can be closed. The close method is invoked to release resources that the object is holding (such as open files).";
        VertxJerseyBuffer buffer = new VertxJerseyBuffer();
        buffer.getOutputStream().write(s.getBytes());

        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        // when
        int size = readSize(buffer);
        byte[] chunk = new byte[size];
        while (buffer.getInputStream().read(chunk) != -1) {
            baos.write(chunk);
            size = readSize(buffer);

            if (size == 0) {
                break;
            }

            chunk = new byte[size];
        }

        // then
        Assert.assertEquals(s, new String(baos.toByteArray()));
    }

    protected int readSize(VertxJerseyBuffer buffer) {
        return buffer.size() > 32 ? 32 : buffer.size();
    }
}
