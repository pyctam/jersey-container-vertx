package com.github.pyctam.helloworld;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Rustam Bogubaev on 8/6/15.
 */
@ApplicationPath("/test/rest/v1")
public class HelloWorldApplication extends Application {
    private final Set<Class<?>> classes;

    public HelloWorldApplication() {
        HashSet<Class<?>> c = new HashSet<Class<?>>();
        c.add(HelloWorldResource.class);
        classes = Collections.unmodifiableSet(c);
    }

    @Override
    public Set<Class<?>> getClasses() {
        return classes;
    }
}
