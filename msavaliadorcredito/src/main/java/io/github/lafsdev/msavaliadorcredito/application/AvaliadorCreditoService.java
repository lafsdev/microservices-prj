package io.github.lafsdev.msavaliadorcredito.application;

import io.github.lafsdev.msavaliadorcredito.domain.model.DadosCliente;
import io.github.lafsdev.msavaliadorcredito.domain.model.SituacaoCliente;
import io.github.lafsdev.msavaliadorcredito.infra.clients.ClienteResourceClient;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AvaliadorCreditoService {

    private final ClienteResourceClient clienteResourceClient;

    public SituacaoCliente obterSituacaoCliente(String cpf) {
        ResponseEntity<DadosCliente> responseEntity = clienteResourceClient.dadosCliente(cpf);

        return SituacaoCliente.builder()
                .cliente(responseEntity.getBody())
                .build();
    }
}
