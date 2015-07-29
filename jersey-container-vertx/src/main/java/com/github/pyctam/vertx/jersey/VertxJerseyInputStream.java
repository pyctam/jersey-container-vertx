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

import org.vertx.java.core.Handler;
import org.vertx.java.core.buffer.Buffer;
import org.vertx.java.core.http.HttpServerRequest;

import java.io.PipedInputStream;
import java.io.PipedOutputStream;

/**
 * Created by developer on 3/1/15.
 */
public class VertxJerseyInputStream extends PipedInputStream {
    private PipedOutputStream pipedOutput;
    private HttpServerRequest vertxRequest;

    public VertxJerseyInputStream(HttpServerRequest vertxRequest) {
        this.pipedOutput = new PipedOutputStream();
        this.vertxRequest = vertxRequest;
        onData();
        onBody();
        onEnd();
    }

    private void onEnd() {
        vertxRequest.endHandler(new Handler<Void>() {
            @Override
            public void handle(Void event) {
                try {
                    pipedOutput.flush();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    private void onBody() {
        vertxRequest.bodyHandler(new Handler<Buffer>() {
            @Override
            public void handle(Buffer body) {
                try {
                    pipedOutput.write(body.getBytes());
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    private void onData() {
        vertxRequest.dataHandler(new Handler<Buffer>() {
            @Override
            public void handle(Buffer data) {
                try {
                    pipedOutput.write(data.getBytes());
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }
}
