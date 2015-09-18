package com.github.pyctam.helloworld;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.Date;

/**
 * Created by Rustam Bogubaev on 8/6/15.
 */
@Path("/helloworld")
public class HelloWorldResource {
    public static final String HW = "Hello World!";

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/get")
    public String getHello() {
        return HW + "(" + new Date() + ")";
    }

    @POST
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/post")
    public String postHello(String payload) {
        return HW + "(" + new Date() + ")\n\nPayload: " + payload;
    }
}
