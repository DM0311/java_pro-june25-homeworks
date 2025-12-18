package ru.otus.java.pro.servlet;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import ru.otus.java.pro.crm.model.Address;
import ru.otus.java.pro.crm.model.Client;
import ru.otus.java.pro.crm.model.Phone;
import ru.otus.java.pro.crm.service.DBServiceClient;
import ru.otus.java.pro.services.TemplateProcessor;

@SuppressWarnings({"java:S1989"})
public class UsersServlet extends HttpServlet {

    private static final String USERS_PAGE_TEMPLATE = "users.html";

    private final transient DBServiceClient serviceClient;
    private final transient TemplateProcessor templateProcessor;

    public UsersServlet(TemplateProcessor templateProcessor, DBServiceClient serviceClient) {
        this.templateProcessor = templateProcessor;
        this.serviceClient = serviceClient;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse response) throws IOException {
        Map<String, Object> paramsMap = new HashMap<>();
        var list = serviceClient.findAll();
        paramsMap.put("clients", list);

        response.setContentType("text/html");
        response.getWriter().println(templateProcessor.getPage(USERS_PAGE_TEMPLATE, paramsMap));
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        String name = req.getParameter("clientName");
        String street = req.getParameter("clientAddress");
        String phone = req.getParameter("clientPhone");

        if (name != null
                && !name.isBlank()
                && street != null
                && !street.isBlank()
                && phone != null
                && !phone.isBlank()) {
            Client client = new Client(null, name, new Address(null, street), List.of(new Phone(null, phone)));

            serviceClient.saveClient(client);
        }
        resp.sendRedirect(req.getContextPath() + "/users");
    }
}
