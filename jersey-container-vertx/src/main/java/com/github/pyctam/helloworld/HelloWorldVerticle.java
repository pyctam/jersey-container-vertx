package com.github.pyctam.helloworld;

import com.github.pyctam.vertx.jersey.VertxJerseyContainer;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Handler;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerRequest;
import org.glassfish.jersey.server.ContainerFactory;
import org.glassfish.jersey.server.ContainerRequest;

import javax.ws.rs.core.Application;
import java.util.logging.Logger;

/**
 * Created by Rustam Bogubaev on 8/6/15.
 */
public class HelloWorldVerticle extends AbstractVerticle {
    private static final Logger LOGGER = Logger.getLogger(HelloWorldVerticle.class.getCanonicalName());

    @Override
    public void start() throws Exception {
        Application application = new HelloWorldApplication();
        final VertxJerseyContainer jerseyContainer = ContainerFactory.createContainer(VertxJerseyContainer.class, application);

        HttpServer server = vertx.createHttpServer();

        server.requestHandler(new Handler<HttpServerRequest>() {
            public void handle(HttpServerRequest request) {
                ContainerRequest containerRequest = jerseyContainer.adapt(request);
                jerseyContainer.getApplicationHandler().handle(containerRequest);

                LOGGER.info("handled: " + request.absoluteURI());
            }
        }).listen(8899, "localhost");

        LOGGER.info("Started");
    }

    public void stop() {
        LOGGER.info("Stopped");
    }
}
