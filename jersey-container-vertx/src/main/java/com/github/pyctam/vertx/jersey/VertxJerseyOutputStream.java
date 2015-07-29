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

import io.vertx.core.http.HttpServerResponse;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by Rustam Bogubaev on 3/1/15.
 */
public class VertxJerseyOutputStream extends OutputStream {
    private HttpServerResponse vertxResponse;

    public VertxJerseyOutputStream(HttpServerResponse vertxResponse) {
        this.vertxResponse = vertxResponse;
        this.vertxResponse.setChunked(true);
    }

    @Override
    public void write(int b) throws IOException {
        vertxResponse.write(String.valueOf((char) b));
    }
}
