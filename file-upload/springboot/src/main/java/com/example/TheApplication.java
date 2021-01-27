package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;

/**
 * The root of our spring-boot application and JAX-RS configuration.
 */
@SpringBootApplication
@ApplicationPath("/api")
public class TheApplication extends Application {
    private static ConfigurableApplicationContext context;

	/**
	 * Spring application starter.
	 */
	public static void main(String[] args) {
		context = SpringApplication.run(TheApplication.class, args);
	}

	public static boolean isRunning() {
	    return context != null && context.isRunning();
    }

	public static ApplicationContext getContext() {
	    return context;
    }

	/**
	 * Note that we only need to declare the jax-rs request/response filters. Since we did not annotate it with @Named -
	 * something that is not required by JAX-RS but is required by Spring -  it will not be wired and we need to
	 * define it here. If you have more JAX-RS classes that are missing the annotation @Named, either add it or
	 * declare the class here or the instance in an overridden getSingletons() method.
	 */
	@Override
	public Set<Class<?>> getClasses() {
		Set<Class<?>> classes = new HashSet<>();

		return classes;
	}
}
