package io.github.lafsdev.msavaliadorcredito.application;

import feign.FeignException;
import feign.RetryableException;
import io.github.lafsdev.msavaliadorcredito.domain.model.CartaoCliente;
import io.github.lafsdev.msavaliadorcredito.domain.model.DadosCliente;
import io.github.lafsdev.msavaliadorcredito.domain.model.SituacaoCliente;
import io.github.lafsdev.msavaliadorcredito.ex.DadosClientesNotFoundException;
import io.github.lafsdev.msavaliadorcredito.ex.ErroComunicacaoMicroservicesException;
import io.github.lafsdev.msavaliadorcredito.infra.clients.CartoesResourcesClient;
import io.github.lafsdev.msavaliadorcredito.infra.clients.ClienteResourceClient;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AvaliadorCreditoService {

    private final ClienteResourceClient clienteResourceClient;
    private final CartoesResourcesClient cartoesResourcesClient;

    public SituacaoCliente obterSituacaoCliente(String cpf) throws DadosClientesNotFoundException, ErroComunicacaoMicroservicesException {

        try {
            ResponseEntity<DadosCliente> responseEntityDadosCliente = clienteResourceClient.dadosCliente(cpf);
            ResponseEntity<List<CartaoCliente>> responseEntityCartoesClientes = cartoesResourcesClient.getCartoesClientes(cpf);


            return SituacaoCliente.builder()
                    .cliente(responseEntityDadosCliente.getBody())
                    .cartoes(responseEntityCartoesClientes.getBody())
                    .build();
        } catch (FeignException.FeignClientException | RetryableException | FeignException.ServiceUnavailable e) {
            int status = e.status();
            if (HttpStatus.NOT_FOUND.value() == status) {
                throw new DadosClientesNotFoundException();
            }
            throw new ErroComunicacaoMicroservicesException(e.getMessage(), status);
        }
    }
}
