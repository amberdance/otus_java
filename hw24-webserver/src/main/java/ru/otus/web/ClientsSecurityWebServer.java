package ru.otus.web;

import lombok.extern.slf4j.Slf4j;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import ru.otus.data.crm.service.ClientService;
import ru.otus.web.server.CustomSecurityWebServer;
import ru.otus.web.servlet.AuthorizationFilter;
import ru.otus.web.servlet.ClientsServlet;
import ru.otus.web.servlet.LoginServlet;

import java.util.Arrays;


@Slf4j
public class ClientsSecurityWebServer extends CustomSecurityWebServer {

    private static final int SERVER_PORT = 8080;
    private static final String[] PATHS = {"/clients", "/api/clients/*"};
    private final ClientService clientService;

    public ClientsSecurityWebServer(ClientService clientService) {
        super(SERVER_PORT);
        this.clientService = clientService;
    }

    @Override
    protected Handler applySecurity(ServletContextHandler servletContextHandler,
                                    String... paths) {
        log.debug("Applying security to given paths: {}", (Object) paths);

        servletContextHandler.addServlet(new ServletHolder(
                        new LoginServlet(templateProcessor, clientService)),
                "/login");
        var authorizationFilter = new AuthorizationFilter();

        Arrays.stream(paths).forEachOrdered(
                path -> servletContextHandler.addFilter(
                        new FilterHolder(authorizationFilter), path, null));

        return servletContextHandler;
    }

    @Override
    protected ServletContextHandler createServletContextHandler() {
        log.debug("Creating servlet context handlers...");

        var servletContextHandler =
                new ServletContextHandler(ServletContextHandler.SESSIONS);

        for (var path : PATHS) {
            servletContextHandler.addServlet(new ServletHolder(
                    new ClientsServlet(templateProcessor, objectMapper,
                            clientService)), path);
        }

        return servletContextHandler;
    }
}
