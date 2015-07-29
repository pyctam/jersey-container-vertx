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

import org.glassfish.jersey.server.ContainerException;
import org.glassfish.jersey.server.ContainerResponse;
import org.glassfish.jersey.server.spi.ContainerResponseWriter;
import org.vertx.java.core.http.HttpServerResponse;

import javax.ws.rs.core.MultivaluedMap;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by developer on 3/1/15.
 */
public class VertxJerseyWriter implements ContainerResponseWriter {
    private HttpServerResponse vertxResponse;

    public VertxJerseyWriter(HttpServerResponse vertxResponse) {
        this.vertxResponse = vertxResponse;
    }

    @Override
    public OutputStream writeResponseStatusAndHeaders(long contentLength, ContainerResponse responseContext) throws ContainerException {
        MultivaluedMap<String, String> responseHeaders = responseContext.getStringHeaders();

        for (Map.Entry<String, List<String>> entry : responseHeaders.entrySet()) {
            String key = entry.getKey();
            List<String> values = entry.getValue();
            vertxResponse.putHeader(key, values);
        }

        vertxResponse.setStatusCode(responseContext.getStatus());
        vertxResponse.setStatusMessage(responseContext.getStatusInfo().getReasonPhrase());

        return new VertxJerseyOutputStream(vertxResponse);
    }

    @Override
    public boolean suspend(long timeOut, TimeUnit timeUnit, ContainerResponseWriter.TimeoutHandler timeoutHandler) {
        throw new UnsupportedOperationException("Method suspend is not supported by the container.");
    }

    @Override
    public void setSuspendTimeout(long timeOut, TimeUnit timeUnit) throws IllegalStateException {
        throw new UnsupportedOperationException("Method setSuspendTimeout is not supported by the container.");
    }

    @Override
    public void commit() {
        vertxResponse.end();
    }

    @Override
    public void failure(Throwable error) {
        try {
            vertxResponse.setStatusCode(500);
            vertxResponse.setStatusMessage(error.getMessage());
        } finally {
            commit();
        }
    }

    @Override
    public boolean enableResponseBuffering() {
        return true;
    }
}
