package io.github.lafsdev.msavaliadorcredito.application;

import io.github.lafsdev.msavaliadorcredito.domain.model.DadosAvaliacao;
import io.github.lafsdev.msavaliadorcredito.domain.model.RetornoAvaliacaoCliente;
import io.github.lafsdev.msavaliadorcredito.domain.model.SituacaoCliente;
import io.github.lafsdev.msavaliadorcredito.ex.DadosClientesNotFoundException;
import io.github.lafsdev.msavaliadorcredito.ex.ErroComunicacaoMicroservicesException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("avaliacoes-credito")
public class AvaliadorCreditoController {

    private final AvaliadorCreditoService avaliadorCreditoService;

    @GetMapping
    public String status() {
        return "Ok";
    }

    @GetMapping(value = "situacao-cliente", params = "cpf")
    public ResponseEntity consultaSituacaoCliente(@RequestParam("cpf") String cpf) {
        try {
            SituacaoCliente situacaoCliente = avaliadorCreditoService.obterSituacaoCliente(cpf);
            return ResponseEntity.ok(situacaoCliente);
        } catch (DadosClientesNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (ErroComunicacaoMicroservicesException e) {
            return ResponseEntity.status(HttpStatus.resolve(e.getStatus())).body(e.getMessage());
        }
    }

   @PostMapping
    public ResponseEntity realizarAvaliacao(@RequestBody DadosAvaliacao dadosAvaliacao){
       try {
           RetornoAvaliacaoCliente retornoAvaliacaoCliente = avaliadorCreditoService.realizarAvaliacao(dadosAvaliacao.getCpf(), dadosAvaliacao.getRenda());
           return ResponseEntity.ok(retornoAvaliacaoCliente);
       } catch (DadosClientesNotFoundException e) {
           return ResponseEntity.notFound().build();
       } catch (ErroComunicacaoMicroservicesException e) {
           return ResponseEntity.status(HttpStatus.resolve(e.getStatus())).body(e.getMessage());
       }
   }
}
