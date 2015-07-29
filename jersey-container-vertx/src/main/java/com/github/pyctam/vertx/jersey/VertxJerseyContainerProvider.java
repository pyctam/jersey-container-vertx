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

import org.glassfish.jersey.server.spi.ContainerProvider;
import org.vertx.java.core.http.HttpServerRequest;

import javax.ws.rs.ProcessingException;
import javax.ws.rs.core.Application;

/**
 * Created by developer on 3/1/15.
 */
public class VertxJerseyContainerProvider implements ContainerProvider {
    @Override
    public <T> T createContainer(Class<T> type, Application application) throws ProcessingException {
        if (HttpServerRequest.class == type || VertxJerseyContainer.class == type) {
            return type.cast(new VertxJerseyContainer(application));
        }

        return null;
    }
}
