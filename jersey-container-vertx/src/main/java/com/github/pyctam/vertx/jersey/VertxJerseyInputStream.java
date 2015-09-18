/**
 * Copyright 2015 the original author or authors.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.pyctam.vertx.jersey;


import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpServerRequest;

/**
 * Created by Rustam Bogubaev on 3/1/15.
 */
public class VertxJerseyInputStream {
    private VertxJerseyBuffer buffer;
    private HttpServerRequest vertxRequest;

    public VertxJerseyInputStream(HttpServerRequest vertxRequest) {
        this.vertxRequest = vertxRequest;
        onData();
        onBody();
        onEnd();
    }

    public VertxJerseyBuffer buffer() {
        if (buffer == null) {
            buffer = new VertxJerseyBuffer();
        }

        return buffer;
    }

    private void onEnd() {
        vertxRequest.endHandler(new Handler<Void>() {
            @Override
            public void handle(Void event) {
                try {
                    buffer().getOutputStream().flush();
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
                    buffer().getOutputStream().write(body.getBytes());
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    private void onData() {
        vertxRequest.bodyHandler(new Handler<Buffer>() {
            @Override
            public void handle(Buffer data) {
                try {
                    buffer().getOutputStream().write(data.getBytes());
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }
}
