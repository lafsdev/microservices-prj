package io.github.lafsdev.mscartoes.application.controller;

import io.github.lafsdev.mscartoes.application.representation.CartaoSaveRequest;
import io.github.lafsdev.mscartoes.application.representation.CartoesPorClienteResponse;
import io.github.lafsdev.mscartoes.application.service.CartaoService;
import io.github.lafsdev.mscartoes.application.service.ClienteCartaoService;
import io.github.lafsdev.mscartoes.domain.Cartao;
import io.github.lafsdev.mscartoes.domain.ClienteCartao;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("cartoes")
public class CartoesController {

    private final CartaoService cartaoService;
    private final ClienteCartaoService clienteCartaoService;

    @GetMapping
    public String status() {
        return "Ok";
    }

    @PostMapping
    public ResponseEntity cadastra(@RequestBody CartaoSaveRequest request) {
        Cartao cartao = request.toModel();
        cartaoService.save(cartao);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }


    @GetMapping(params = "renda")
    public ResponseEntity<List<Cartao>> getCartoesRendaAteh(@RequestParam("renda") Long renda) {
        List<Cartao> cartoesRendaMenorIgual = cartaoService.getCartoesRendaMenorIgual(renda);
        return ResponseEntity.ok(cartoesRendaMenorIgual);
    }

    @GetMapping(params = "cpf")
    public ResponseEntity<List<CartoesPorClienteResponse>> getCartoesClientes(@RequestParam("cpf") String cpf) {
        List<ClienteCartao> clienteCartaos = clienteCartaoService.listCartoesByCpf(cpf);
        List<CartoesPorClienteResponse> responseList = clienteCartaos.stream().map(CartoesPorClienteResponse::fromModel).toList();
        return ResponseEntity.ok(responseList);
    }
}
