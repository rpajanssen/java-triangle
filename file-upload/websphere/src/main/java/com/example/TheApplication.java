package com.example;

import com.example.rest.exceptionhandlers.ConstraintViolationHandler;
import com.example.rest.exceptionhandlers.DefaultExceptionHandler;
import com.example.rest.exceptionhandlers.ValidationExceptionHandler;
import com.example.rest.resources.FileUploadResource;
import com.example.rest.resources.FileUploadWithFormAndServletRequestResource;

import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import javax.json.bind.JsonbConfig;
import javax.json.bind.config.BinaryDataStrategy;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import javax.ws.rs.ext.ContextResolver;
import java.util.HashSet;
import java.util.Set;

@ApplicationPath(value = "/api")
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

	/**
	 * Configure the binding in order to support byte[] as properties to be serialized
	 * arriving as a base64 encoded string.
	 *
	 * This configuration is NOT required for the form-upload.
	 */
	@Override
	public Set<Object> getSingletons() {
		Set<Object> singletons = new HashSet<>();
		singletons.add(getJSONBConfiguration());
		return singletons;
	}

	ContextResolver getJSONBConfiguration() {
		return new ContextResolver<Jsonb>() {
			@Override
			public Jsonb getContext(Class type) {
				return buildJsonB();
			}
		};
	}

	private Jsonb buildJsonB() {
		JsonbConfig config = new JsonbConfig()
				// use the prefab binary data strategy to configure byte[] to be serialized as base64 encoded strings
				.withBinaryDataStrategy(BinaryDataStrategy.BASE_64)
				// to use the custom adapter incomment the line below and outcomment the binary data strategy configuration
				//.withAdapters(new FormJsonBAdapter())
				;

		return JsonbBuilder.newBuilder().
				withConfig(config).
				build();
	}
}
