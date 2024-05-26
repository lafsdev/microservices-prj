package io.github.lafsdev.mscartoes.application.controller;

import io.github.lafsdev.mscartoes.application.representation.CartaoSaveRequest;
import io.github.lafsdev.mscartoes.application.service.CartaoService;
import io.github.lafsdev.mscartoes.domain.Cartao;
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
}
