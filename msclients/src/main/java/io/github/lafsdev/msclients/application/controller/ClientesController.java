package io.github.lafsdev.msclients.application.controller;

import io.github.lafsdev.msclients.application.representation.ClienteSaveRequest;
import io.github.lafsdev.msclients.application.service.ClienteService;
import io.github.lafsdev.msclients.domain.Cliente;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping("clientes")
@Slf4j
public class ClientesController {

    private final ClienteService clienteService;

    public ClientesController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @GetMapping
    public String status(){
        log.info("Obtendo status do microservice de clients");
        return "ok";
    }

    @PostMapping
    public ResponseEntity save(@RequestBody ClienteSaveRequest request){
        Cliente cliente = request.toModel();
        clienteService.save(cliente);
        URI headerLocation = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .query("cpf={cpf}")
                .buildAndExpand(cliente.getCpf())
                .toUri();
        return ResponseEntity.created(headerLocation).build();
    }

    @GetMapping(params="cpf")
    public ResponseEntity dadosCliente(@RequestParam String cpf){
        Optional<Cliente> cliente = clienteService.getByCpf(cpf);
        if(cliente.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(cliente);
    }
}
