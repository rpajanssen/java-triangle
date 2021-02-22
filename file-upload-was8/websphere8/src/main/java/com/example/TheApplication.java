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
	 *
	 * todo : doesn't work yet
	 *
	 */
	@Override
	public Set<Object> getSingletons() {
		Set<Object> singletons = new HashSet<>();
		singletons.add(getJSONBConfiguration());
		return singletons;
	}

	private ContextResolver getJSONBConfiguration() {
		return new ContextResolver<Jsonb>() {
			@Override
			public Jsonb getContext(Class type) {
				return buildJsonB();
			}
		};
	}

	private Jsonb buildJsonB() {
		JsonbConfig config = new JsonbConfig().withBinaryDataStrategy(BinaryDataStrategy.BASE_64_URL);
		return JsonbBuilder.newBuilder().
				withConfig(config).
				build();
	}
}
