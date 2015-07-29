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
import org.glassfish.jersey.server.ApplicationHandler;
import org.glassfish.jersey.server.ContainerRequest;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.internal.ConfigHelper;
import org.glassfish.jersey.server.spi.Container;
import org.glassfish.jersey.server.spi.ContainerLifecycleListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created by Rustam Bogubaev on 3/1/15.
 */
public class VertxJerseyContainer implements Container {
    private static final Logger LOGGER = LoggerFactory.getLogger(VertxJerseyContainer.class);

    private ApplicationHandler appHandler;
    private ContainerLifecycleListener containerListener;

    public VertxJerseyContainer(Application application) {
        this.appHandler = new ApplicationHandler(application);
        this.containerListener = ConfigHelper.getContainerLifecycleListener(appHandler);
    }

    @Override
    public ResourceConfig getConfiguration() {
        return appHandler.getConfiguration();
    }

    @Override
    public ApplicationHandler getApplicationHandler() {
        return appHandler;
    }

    @Override
    public void reload() {
        reload(appHandler.getConfiguration());
    }

    @Override
    public void reload(ResourceConfig resourceConfig) {
        containerListener.onShutdown(this);
        appHandler = new ApplicationHandler(resourceConfig);
        containerListener = ConfigHelper.getContainerLifecycleListener(appHandler);
        containerListener.onReload(this);
        containerListener.onStartup(this);
    }

    public ContainerRequest adapt(HttpServerRequest request) {
        try {
            URI baseUri = resolveBaseUri(request);
            LOGGER.debug("base URI: {}", baseUri);

            return new VertxJerseyRequest(baseUri, request).adapt();
        } catch (Exception e) {
            LOGGER.error("Unable to create container request due to: ", e);
            return null;
        }
    }

    protected URI resolveBaseUri(HttpServerRequest request) throws URISyntaxException {
        String path = resolveApplicationPath();
        URI absolute = new URI(request.absoluteURI());

        if (path == null) {
            return absolute.resolve("/");
        }

        if (!path.endsWith("/")) {
            path = path.concat("/");
        }

        return absolute.resolve(path);
    }

    protected String resolveApplicationPath() {
        try {
            Application application = getApplicationHandler().getConfiguration().getApplication();
            Class clazz = application.getClass();
            boolean present = clazz.isAnnotationPresent(ApplicationPath.class);

            if (!present) {
                return null;
            }

            ApplicationPath annotation = (ApplicationPath) clazz.getAnnotation(ApplicationPath.class);
            String value = annotation.value();

            if (value == null) {
                return null;
            }

            value = value.trim();

            return !value.isEmpty() ? value : null;
        } catch (Exception e) {
            LOGGER.error("Unable to resolve application path due to: ", e);
            return null;
        }
    }
}
