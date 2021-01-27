package com.example;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.jboss.resteasy.plugins.server.servlet.HttpServletDispatcher;

import javax.servlet.MultipartConfigElement;

/**
 * The main class that will be executed by executing the fat jar. This class will start the Jetty server and define the
 * JAX-RS application class (so we do NOT need to annotate the application class).
 */
public class Main {
    static final String APPLICATION_PATH = "/api";
    static final String CONTEXT_ROOT = "/";

    public Main() {}

    public static void main(String[] args) {
        try {
            new Main().run();
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    public void run() throws Exception {
        final int port = 8080;
        final Server server = new Server(port);

        // Setup the basic Application "context" at "/".
        // This is also known as the handler tree (in Jetty speak).
        final ServletContextHandler context = new ServletContextHandler(server, CONTEXT_ROOT);

        // Setup RESTEasy's HttpServletDispatcher at "/api/*".
        final ServletHolder jettyServlet = new ServletHolder(new HttpServletDispatcher());

        // setup the context (normally in your context.xml
        // enable CDI: register the CdiInjectorFactory
        jettyServlet.setInitParameter("resteasy.injector.factory", org.jboss.resteasy.cdi.CdiInjectorFactory.class.getCanonicalName());
        jettyServlet.setInitParameter("resteasy.servlet.mapping.prefix", APPLICATION_PATH);
        jettyServlet.setInitParameter("javax.ws.rs.Application", TheApplication.class.getCanonicalName());

        // setup the multipart http calls
        jettyServlet.getRegistration().setMultipartConfig(
                new MultipartConfigElement("/tmp", 35000000L, 218018841L, 0)
        );

        context.addServlet(jettyServlet, APPLICATION_PATH + "/*");

        // Setup the DefaultServlet at "/".
        final ServletHolder defaultServlet = new ServletHolder(new DefaultServlet());
        context.addServlet(defaultServlet, CONTEXT_ROOT);

        // enable CDI
        //context.addEventListener(new org.eclipse.jetty.webapp.DecoratingListener()); // todo : documentation suggests this may be required
        context.addEventListener(new org.jboss.weld.environment.servlet.Listener());

        server.start();
        server.join();
    }
}
