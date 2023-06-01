package ru.otus.web.servlet;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import ru.otus.data.crm.service.ClientService;
import ru.otus.web.service.TemplateProcessor;

import java.io.IOException;
import java.util.Collections;

import static jakarta.servlet.http.HttpServletResponse.SC_UNAUTHORIZED;

@RequiredArgsConstructor
public class LoginServlet extends HttpServlet {

    private static final String PARAM_LOGIN = "login";
    private static final String PARAM_PASSWORD = "password";
    private static final String LOGIN_PAGE_TEMPLATE = "login.html";
    private static final String SUCCESS_AUTH_REDIRECT_PAGE = "/clients";
    private static final int MAX_INACTIVE_INTERVAL = 30;

    private final TemplateProcessor templateProcessor;
    private final ClientService clientService;


    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws IOException {
        response.setContentType("text/html");
        response.getWriter().println(
                templateProcessor.getPage(LOGIN_PAGE_TEMPLATE,
                        Collections.emptyMap()));
    }

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response) throws IOException {

        var name = request.getParameter(PARAM_LOGIN);
        var password = request.getParameter(PARAM_PASSWORD);

        if (clientService.authenticate(name, password)) {
            var session = request.getSession();
            session.setMaxInactiveInterval(MAX_INACTIVE_INTERVAL);
            response.sendRedirect(SUCCESS_AUTH_REDIRECT_PAGE);
        } else {
            response.setStatus(SC_UNAUTHORIZED);
            response.getWriter()
                    .println("Wrong credentials: " + SC_UNAUTHORIZED);
        }
    }

}
