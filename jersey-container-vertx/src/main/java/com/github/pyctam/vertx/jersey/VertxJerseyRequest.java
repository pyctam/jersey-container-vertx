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

import io.vertx.core.http.HttpServerRequest;
import org.glassfish.jersey.internal.MapPropertiesDelegate;
import org.glassfish.jersey.internal.PropertiesDelegate;
import org.glassfish.jersey.server.ContainerRequest;
import org.glassfish.jersey.server.spi.ContainerResponseWriter;

import javax.ws.rs.core.SecurityContext;
import java.io.InputStream;
import java.net.URI;
import java.security.Principal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Rustam Bogubaev on 3/1/15.
 */
public class VertxJerseyRequest {
    private URI baseUri;
    private HttpServerRequest vertxRequest;

    public VertxJerseyRequest(URI baseUri, HttpServerRequest vertxRequest) {
        this.baseUri = baseUri;
        this.vertxRequest = vertxRequest;
    }

    /**
     * Create new Jersey container request context.
     *
     * @return Jersey container request context.
     */
    public ContainerRequest adapt() {
        InputStream entityStream = createEntityStream();
        ContainerResponseWriter writer = createResponseWriter();
        Map<String, List<String>> headers = adaptHeaders();

        ContainerRequest request = createContainerRequest();
        request.setEntityStream(entityStream);
        request.setWriter(writer);
        request.getHeaders().putAll(headers);

        return request;
    }

    protected Map<String, List<String>> adaptHeaders() {
        Map<String, List<String>> headers = new LinkedHashMap<>();

        for (String header : vertxRequest.headers().names()) {
            List<String> values = vertxRequest.headers().getAll(header);
            headers.put(header, values);
        }

        return headers;
    }

    protected ContainerResponseWriter createResponseWriter() {
        return new VertxJerseyWriter(vertxRequest.response());
    }

    protected InputStream createEntityStream() {
        return new VertxJerseyInputStream(vertxRequest);
    }

    protected ContainerRequest createContainerRequest() {
        URI baseUri = getBaseUri();
        URI requestUri = getRequestUri();
        String httpMethod = getHttpMethod();
        SecurityContext securityContext = getSecurityContext();
        PropertiesDelegate propertiesDelegate = getPropertiesDelegate();

        return new ContainerRequest(baseUri, requestUri, httpMethod, securityContext, propertiesDelegate);
    }

    /**
     * Gets custom {@link org.glassfish.jersey.internal.PropertiesDelegate properties delegate} extracted from Vert.x HTTP server request.
     *
     * @return custom {@link org.glassfish.jersey.internal.PropertiesDelegate properties delegate} extracted from Vert.x HTTP server request.
     */
    private PropertiesDelegate getPropertiesDelegate() {
        return new MapPropertiesDelegate();
    }

    /**
     * Gets security context of the current request extracted from Vert.x HTTP server request. Must not be {@code null}.
     * The {@link javax.ws.rs.core.SecurityContext#getUserPrincipal()} must return
     * {@code null} if the current request has not been authenticated
     * by the container.
     *
     * @return security context of the current request extracted from Vert.x HTTP server request.
     */
    private SecurityContext getSecurityContext() {
        return new SecurityContext() {
            @Override
            public boolean isUserInRole(final String role) {
                return false;
            }

            @Override
            public boolean isSecure() {
                return vertxRequest.absoluteURI().startsWith("https");
            }

            @Override
            public Principal getUserPrincipal() {
                return null;
            }

            @Override
            public String getAuthenticationScheme() {
                return null;
            }
        };
    }

    /**
     * Gets request HTTP method name extracted from Vert.x HTTP server request.
     *
     * @return request HTTP method name extracted from Vert.x HTTP server request.
     */
    private String getHttpMethod() {
        return vertxRequest.method().name();
    }

    /**
     * Gets request URI extracted from Vert.x HTTP server request.
     *
     * @return request URI extracted from Vert.x HTTP server request.
     */
    private URI getRequestUri() {
        try {
            String uri = vertxRequest.uri();
            return getBaseUri().resolve(uri);
        } catch (Exception ex) {
            throw new IllegalArgumentException(ex);
        }
    }

    /**
     * Gets base application URI extracted from Vert.x HTTP server request.
     *
     * @return base application URI extracted from Vert.x HTTP server request.
     */
    protected URI getBaseUri() {
        return baseUri;
    }
}
