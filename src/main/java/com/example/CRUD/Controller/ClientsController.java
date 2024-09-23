package com.example.CRUD.Controller;

import com.example.CRUD.Model.Client;
import com.example.CRUD.Model.ClientDto;
import com.example.CRUD.Repository.ClientRepository;
import jakarta.validation.Valid;
import org.aspectj.apache.bcel.generic.InstructionConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@Controller
@RequestMapping("/clients")
public class ClientsController {

    @Autowired
    private ClientRepository clientRepository;

    @GetMapping({"","/"})
    public String getClients(Model model) {
        var clients = clientRepository.findAll(Sort.by(Sort.Direction.DESC,"id"));
        model.addAttribute("clients", clients);

        return "clients/index";
    }

    @GetMapping("/create")
    public String createClient(Model model) {
        ClientDto clientDto = new ClientDto();
        model.addAttribute("clientDto", clientDto);

        return "clients/create";
    }

    @PostMapping("/create")
    public String createClient (@Valid @ModelAttribute ClientDto clientDto, BindingResult result) {

        if (clientRepository.findByEmail(clientDto.getEmail()) != null) {
            result.addError(new FieldError("clientDto","email", clientDto.getEmail(), false, null, null,"Email address is already used"));
        }
        if (result.hasErrors()) {
            return "clients/create";
        }

        Client client = new Client();
        client.setFirstName(clientDto.getFirstName());
        client.setLastName(clientDto.getLastName());
        client.setEmail(clientDto.getEmail());
        client.setPhone(clientDto.getPhone());
        client.setAddress(clientDto.getAddress());
        client.setStatus(clientDto.getStatus());
        client.setCreatedAt(new Date());

        clientRepository.save(client);

        return "redirect:/clients";
    }

    @GetMapping("/edit")
    public String editClient(Model model, @RequestParam int id) {

        Client client = clientRepository.findById(id).orElse(null);
        if (client == null) {
            return "redirect:/clients";
        }

        ClientDto clientDto = new ClientDto();
        clientDto.setFirstName(client.getFirstName());
        clientDto.setLastName(client.getLastName());
        clientDto.setEmail(client.getEmail());
        clientDto.setPhone(client.getPhone());
        clientDto.setAddress(client.getAddress());
        clientDto.setStatus(client.getStatus());

        model.addAttribute("client", client);
        model.addAttribute("clientDto", clientDto);

        return "clients/edit";

    }

    @PostMapping("/edit")
    public String editClient(Model model, @RequestParam int id, @Valid @ModelAttribute ClientDto clientDto, BindingResult result) {

        Client client = clientRepository.findById(id).orElse(null);
        if (client == null) {
            return "redirect:/client";
        }

        model.addAttribute("client", client);

        if (result.hasErrors()) {
            return "clients/edit";
        }

        client.setFirstName(clientDto.getFirstName());
        client.setLastName(clientDto.getLastName());
        client.setEmail(clientDto.getEmail());
        client.setPhone(clientDto.getPhone());
        client.setAddress(clientDto.getAddress());
        client.setStatus(clientDto.getStatus());

        try {
            clientRepository.save(client);
        } catch (Exception ex) {
            result.addError(
                    new FieldError("clientDto", "email", clientDto.getEmail(), false, null, null, "Email address is already used" )
            );

            return "clients/edit";
        }

        return "redirect:/clients";
    }

    @GetMapping("/delete")
    public String deleteClient(@RequestParam int id) {

        Client client = clientRepository.findById(id).orElse(null);

        if (client != null) {
            clientRepository.delete(client);
        }

        return "redirect:/clients";


    }



}
