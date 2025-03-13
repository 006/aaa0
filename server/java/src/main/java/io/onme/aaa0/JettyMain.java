package io.onme.aaa0;

import java.util.EnumSet;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.DefaultHandler;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.servlets.CrossOriginFilter;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import org.glassfish.jersey.servlet.ServletContainer;

import io.onme.aaa0.jersey.RestfulApplication;
import jakarta.servlet.DispatcherType;

/**
 * @Ttron Mar 12, 2025
 */
public class JettyMain {
	/**
	 * @param context
	 * @return
	 */
	private static Server getServer(ServletContextHandler context) {
		QueuedThreadPool threadPool = new QueuedThreadPool();
		threadPool.setName("server");

		// Create a Server instance.
		Server server = new Server(threadPool);

		// Create a ServerConnector to accept connections from clients.
		ServerConnector connector = new ServerConnector(server);

		// The port to listen to.
		connector.setPort(8080);
		// The address to bind to.
		connector.setHost("0.0.0.0");

		// The TCP accept queue size.
		connector.setAcceptQueueSize(128);

		// Add the Connector to the Server
		server.addConnector(connector);

		// Create the server level handler list.
		HandlerList handlers = new HandlerList();
		// Make sure DefaultHandler is last (for error handling reasons)
		handlers.setHandlers(new Handler[] { context, new DefaultHandler() });
		server.setHandler(handlers);
		// server.setHandler( context );
		return server;
	}

	public static void main(String[] args) {

		ServletContextHandler context = new ServletContextHandler(ServletContextHandler.NO_SESSIONS);
		context.setContextPath("/");
		ServletHolder servletHolder = context.addServlet(ServletContainer.class, "/*");
		// servletHolder.setInitParameter( ServerProperties.TRACING, "ALL" );
		// servletHolder.setInitParameter( ServerProperties.TRACING_THRESHOLD, "TRACE"
		// );
		servletHolder.setInitParameter("jakarta.ws.rs.Application", RestfulApplication.class.getName());

		// jerseyHandler.getServletContext().setAttribute( KEY_BUNDLE_CONTEXT, context
		// );

		// Add the filter, and then use the provided FilterHolder to configure it
		FilterHolder cors = context.addFilter(CrossOriginFilter.class, "/*", EnumSet.of(DispatcherType.REQUEST));
		cors.setInitParameter(CrossOriginFilter.ACCESS_CONTROL_ALLOW_ORIGIN_HEADER, "*");
		cors.setInitParameter(CrossOriginFilter.ACCESS_CONTROL_ALLOW_HEADERS_HEADER, "*");
		cors.setInitParameter(CrossOriginFilter.ACCESS_CONTROL_ALLOW_CREDENTIALS_HEADER, "true");
		cors.setInitParameter(CrossOriginFilter.ACCESS_CONTROL_ALLOW_METHODS_HEADER,
				"GET,POST,PUT,DELETE,OPTIONS,HEAD");
		cors.setInitParameter(CrossOriginFilter.ALLOWED_METHODS_PARAM, "GET,POST,PUT,DELETE,OPTIONS,HEAD");
		cors.setInitParameter(CrossOriginFilter.ALLOWED_ORIGINS_PARAM, "*");
		cors.setInitParameter(CrossOriginFilter.ALLOWED_HEADERS_PARAM, "*");
		// cors.setInitParameter( CrossOriginFilter.ALLOWED_HEADERS_PARAM,
		// "X-Requested-With,Content-Type,Accept,Origin,Authorization" );

		// Create and configure a ThreadPool.
		Server server = getServer(context);

		// Start the Server so it starts accepting connections from clients.
		try {
			server.start();
			server.join();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			server.destroy();
		}
	}
}
