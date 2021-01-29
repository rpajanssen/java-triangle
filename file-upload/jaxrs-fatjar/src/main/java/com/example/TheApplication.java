package com.example;

import com.example.rest.exceptionhandlers.ConstraintViolationHandler;
import com.example.rest.exceptionhandlers.DefaultExceptionHandler;
import com.example.rest.exceptionhandlers.ValidationExceptionHandler;
import com.example.rest.resources.FileUploadResource;
import com.example.rest.resources.FileUploadWithFormAndServletRequestResource;

import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;

/**
 * The root of our JAX-RS application. Because all setup is orchestrated from the Main class we do not need to
 * annotate this class with : @ApplicationPath.
 */
public class TheApplication extends Application {

	@Override
	public Set<Class<?>> getClasses() {
		Set<Class<?>> classes = new HashSet<>();

		// resources
		classes.add(FileUploadResource.class);
		classes.add(FileUploadWithFormAndServletRequestResource.class);

		// exception mappers
		classes.add(ConstraintViolationHandler.class);
		classes.add(DefaultExceptionHandler.class);
		classes.add(ValidationExceptionHandler.class);

		return classes;
	}
}
