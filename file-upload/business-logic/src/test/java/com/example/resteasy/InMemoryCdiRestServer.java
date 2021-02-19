package com.example.resteasy;

import io.undertow.Undertow;
import io.undertow.servlet.Servlets;
import io.undertow.servlet.api.DeploymentInfo;
import io.undertow.servlet.api.ServletInfo;
import org.jboss.resteasy.cdi.CdiInjectorFactory;
import org.jboss.resteasy.core.ResteasyDeploymentImpl;
import org.jboss.resteasy.plugins.server.undertow.UndertowJaxrsServer;
import org.jboss.resteasy.spi.ResteasyDeployment;

import javax.servlet.MultipartConfigElement;
import javax.ws.rs.Path;
import javax.ws.rs.core.Application;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.HashSet;
import java.util.Set;

/**
 * A test utility class that provides you with an in-memory jaxrs server that fully supports CDI. This
 * implementation uses Undertow.
 */
public class InMemoryCdiRestServer implements AutoCloseable {
    private String host = "localhost";
    private int port;

    private Set<Object> beans = new HashSet<>();
    private Set<Class> resourceAndProviderClasses = new HashSet<>();

    private UndertowJaxrsServer server;

    private InMemoryCdiRestServer(Object... objects) {
        append(objects);
    }

    /**
     * Create instance and pass given instances/resource/provider classes
     */
    public static InMemoryCdiRestServer instance(Object... objects) throws IOException {
        InMemoryCdiRestServer inMemoryRestServer = new InMemoryCdiRestServer(objects);
        inMemoryRestServer.start();
        return inMemoryRestServer;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public Set<Object> getBeans() {
        return beans;
    }

    public Set<Class> getResourceAndProviderClasses() {
        return resourceAndProviderClasses;
    }

    @Override
    public void close() {
        if (server != null) {
            server.stop();
            server = null;
        }
    }

    private void append(Object... objects) {
        for (Object object : objects) {
            if (object instanceof Class) {
                resourceAndProviderClasses.add((Class) object);
            } else {
                this.beans.add(object);
            }
        }
    }

    private void start() throws IOException {
        port = findFreePort();

        server = buildServer();

        server.start(Undertow.builder().addHttpListener(port, host));
    }

    private static int findFreePort() throws IOException {
        ServerSocket server = new ServerSocket(0);
        int port = server.getLocalPort();
        server.close();

        return port;
    }

    private UndertowJaxrsServer buildServer() {
        UndertowJaxrsServer jaxrsServer = new UndertowJaxrsServer();

        ResteasyDeployment deployment = new ResteasyDeploymentImpl();
        registerBeans(deployment, getBeans());
        registerResourcesAndProviders(deployment, getResourceAndProviderClasses());
        deployment.setInjectorFactoryClass(CdiInjectorFactory.class.getName());

        DeploymentInfo deploymentInfo = jaxrsServer.undertowDeployment(deployment, "/");
        deploymentInfo.setClassLoader(InMemoryCdiRestServer.class.getClassLoader());
        deploymentInfo.setDeploymentName("Undertow + RestEasy CDI example");
        deploymentInfo.setContextPath("");

        final ServletInfo restEasyServlet = deploymentInfo.getServlets().get("ResteasyServlet");

        // setup the multipart http calls
        restEasyServlet.setMultipartConfig(
                new MultipartConfigElement("/tmp", 35000000L, 218018841L, 0)
        );
        //deploymentInfo.addServlet(restEasyServlet);

        deploymentInfo.addListener(Servlets.listener(org.jboss.weld.environment.servlet.Listener.class));

        return jaxrsServer.deploy(deploymentInfo);
    }

    private void registerBeans(ResteasyDeployment deployment, Set<Object> beans) {
        for (Object object : beans) {
            if (object instanceof Application) {
                deployment.setApplication((Application) object);
            } else {
                if (object.getClass().isAnnotationPresent(Path.class)) {
                    deployment.getResources().add(object);
                } else {
                    if(!resourceDetectedAndDeployed(deployment, object)) {
                        deployment.getDefaultContextObjects().put(object.getClass(), object);
                    }
                }
            }
        }
    }

    private boolean resourceDetectedAndDeployed(ResteasyDeployment deployment, Object object) {
        Class[] interfaces = object.getClass().getInterfaces();
        for (Class anInterface: interfaces) {
            if (anInterface.isAnnotationPresent(Path.class)) {
                deployment.getResources().add(object);

                return true;
            }
        }

        return false;
    }

    private void registerResourcesAndProviders(ResteasyDeployment deployment, Set<Class> resourcesAndProviders) {
        for (Class resourceOrProvider : resourcesAndProviders) {
            if (Application.class.isAssignableFrom(resourceOrProvider)) {
                deployment.setApplicationClass(resourceOrProvider.getName());
            } else {
                deployment.getProviderClasses().add(resourceOrProvider.getName());
            }
        }
    }
}
