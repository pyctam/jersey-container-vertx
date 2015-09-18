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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by developer on 9/15/15.
 */
public class VertxJerseyBuffer {
    private byte[] buffer;
    private InputStream is;
    private OutputStream os;

    public byte[] getBuffer() {
        return buffer;
    }

    public int size() {
        return buffer != null ? buffer.length : 0;
    }

    public OutputStream getOutputStream() {
        if (os == null) {
            os = new OutputStream() {
                @Override
                public synchronized void write(int b) throws IOException {
                    if (buffer == null) {
                        buffer = new byte[]{(byte) b};
                        return;
                    }

                    byte[] resized = new byte[buffer.length + 1];
                    System.arraycopy(buffer, 0, resized, 0, buffer.length);
                    resized[resized.length - 1] = (byte) b;

                    buffer = resized;

                    System.out.println("os buffer: " + new String(buffer));
                }

                @Override
                public synchronized void write(byte[] b) throws IOException {
                    super.write(b);
                }

                @Override
                public synchronized void write(byte[] b, int off, int len) throws IOException {
                    super.write(b, off, len);
                }
            };
        }

        return os;
    }

    public InputStream getInputStream() {
        if (is == null) {
            is = new InputStream() {
                @Override
                public synchronized int read() throws IOException {
                    if (buffer == null) {
                        return -1;
                    }

                    if (buffer.length == 0) {
                        return -1;
                    }

                    int newSize = buffer.length - 1;
                    byte[] resized = new byte[newSize > 0 ? newSize : 0];
                    byte result = buffer[0];
                    System.arraycopy(buffer, 1, resized, 0, resized.length);
                    buffer = resized;

                    System.out.println("is buffer: " + new String(buffer));

                    return result;
                }

                @Override
                public synchronized int read(byte[] b) throws IOException {
                    return super.read(b);
                }

                @Override
                public synchronized int read(byte[] b, int off, int len) throws IOException {
                    return super.read(b, off, len);
                }
            };
        }

        return is;
    }
}
