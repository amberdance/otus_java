package ru.otus.web.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jetty.http.HttpStatus;
import ru.otus.data.crm.model.Client;
import ru.otus.data.crm.service.ClientService;
import ru.otus.web.service.TemplateProcessor;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


@RequiredArgsConstructor
public class ClientsServlet extends HttpServlet {

    private final static String CLIENTS_PAGE = "clients.html";
    private final TemplateProcessor templateProcessor;
    private final ObjectMapper objectMapper;
    private final ClientService clientService;


    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response) {
        var clients = clientService.findAll();

        try {
            if (request.getServletPath().contains("api")) {
                handleJsonClientsResponse(response, clients);
            } else {
                handleHtmlClientsResponse(response, clients);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        var json = req.getReader().lines()
                .collect(Collectors.joining(System.lineSeparator()));
        var client = objectMapper.readValue(json, Client.class);
        clientService.saveClient(client);
        resp.setStatus(HttpStatus.CREATED_201);
    }

    private void handleJsonClientsResponse(HttpServletResponse response,
                                           List<Client> clients)
            throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        var out = response.getOutputStream();
        out.print(objectMapper.writeValueAsString(clients));

    }

    private void handleHtmlClientsResponse(HttpServletResponse response,
                                           List<Client> clients)
            throws IOException {
        response.setContentType("text/html");
        response.getWriter().println(
                templateProcessor.getPage(CLIENTS_PAGE,
                        Collections.singletonMap("clients", clients)));
    }


}
