package ru.otus.java.pro.controllers;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.otus.java.pro.model.Client;
import ru.otus.java.pro.service.ClientService;

@Controller
public class MainController {
    private final ClientService clientService;

    @Autowired
    public MainController(ClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping("/")
    public String indexPage(Model model) {
        Iterable<Client> clients = clientService.findAll();
        model.addAttribute("clients", clients);
        return "index";
    }

    @PostMapping("/user")
    public String addUser(
            @RequestParam(required = true) String clientName,
            @RequestParam(required = true) String clientAddress,
            @RequestParam(required = true) String clientPhone,
            Model model) {

        Client client = new Client();
        client.setName(clientName);
        clientService.createClient(client, clientAddress, List.of(clientPhone));
        return "redirect:/";
    }
}
