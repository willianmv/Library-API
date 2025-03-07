package com.example.libraryapi.service;

import com.example.libraryapi.model.Client;
import com.example.libraryapi.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ClientService {

    private final ClientRepository clientRepository;
    private final PasswordEncoder passwordEncoder;

    public void salvar(Client client){
        client.setClientSecret(passwordEncoder.encode(client.getClientSecret()));
        clientRepository.save(client);
    }

    public Client obterPorClientId(String id){
        return clientRepository.findByClientId(id);
    }

}
