package ru.otus.web.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import ru.otus.web.helpers.FileSystemHelper;
import ru.otus.web.service.TemplateProcessor;
import ru.otus.web.service.TemplateProcessorImpl;

@Slf4j
public class CustomSecurityWebServer implements WebServer {

    protected final ObjectMapper objectMapper;
    protected final TemplateProcessor templateProcessor;
    private static final String TEMPLATES_DIR = "/templates/";
    private static final String START_PAGE_NAME = "index.html";
    private static final String COMMON_RESOURCES_DIR = "static";
    private static final String[] SECURED_PATHS =
            {"/admin/*", "/clients/*", "/api/clients/*"};
    private final Server server;

    public CustomSecurityWebServer(int port) {
        this.objectMapper = new ObjectMapper();
        this.templateProcessor = new TemplateProcessorImpl(TEMPLATES_DIR);
        server = new Server(port);
    }

    @Override
    public void start() throws Exception {
        if (server.getHandlers().length == 0) {
            initContext();
        }

        server.start();
    }

    @Override
    public void join() throws Exception {
        server.join();
    }

    @Override
    public void stop() throws Exception {
        server.stop();
    }

    private void initContext() {
        var resourceHandler = createResourceHandler();
        var servletContextHandler = createServletContextHandler();
        var handlers = new HandlerList();

        handlers.addHandler(resourceHandler);
        handlers.addHandler(
                applySecurity(servletContextHandler, SECURED_PATHS));

        server.setHandler(handlers);
    }


    protected Handler applySecurity(ServletContextHandler servletContextHandler,
                                    String... paths) {
        return servletContextHandler;
    }

    protected ServletContextHandler createServletContextHandler() {
        return new ServletContextHandler(ServletContextHandler.SESSIONS);
    }


    private ResourceHandler createResourceHandler() {
        log.debug("Creating resource handlers...");
        var resourceHandler = new ResourceHandler();
        resourceHandler.setDirectoriesListed(false);
        resourceHandler.setWelcomeFiles(new String[] {START_PAGE_NAME});
        resourceHandler.setResourceBase(
                FileSystemHelper.localFileNameOrResourceNameToFullPath(
                        COMMON_RESOURCES_DIR));

        return resourceHandler;
    }

}
