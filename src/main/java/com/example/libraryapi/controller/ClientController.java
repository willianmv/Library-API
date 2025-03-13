package com.example.libraryapi.controller;

import com.example.libraryapi.model.Client;
import com.example.libraryapi.service.ClientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/clients")
@RequiredArgsConstructor
@Slf4j
public class ClientController {

    private final ClientService clientService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('GERENTE')")
    public void salvar(@RequestBody Client client){

        log.info("Registrando novo client: {} com scope: {}", client.getClientId(), client.getScope());

        clientService.salvar(client);
    }

}
