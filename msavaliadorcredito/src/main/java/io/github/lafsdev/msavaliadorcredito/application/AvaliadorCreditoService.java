package io.github.lafsdev.msavaliadorcredito.application;

import feign.FeignException;
import feign.RetryableException;
import io.github.lafsdev.msavaliadorcredito.domain.model.*;
import io.github.lafsdev.msavaliadorcredito.ex.DadosClientesNotFoundException;
import io.github.lafsdev.msavaliadorcredito.ex.ErroComunicacaoMicroservicesException;
import io.github.lafsdev.msavaliadorcredito.infra.clients.CartoesResourcesClient;
import io.github.lafsdev.msavaliadorcredito.infra.clients.ClienteResourceClient;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
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

    public RetornoAvaliacaoCliente realizarAvaliacao(String cpf, Long renda) throws DadosClientesNotFoundException, ErroComunicacaoMicroservicesException {
        try {
            ResponseEntity<DadosCliente> dadosClienteResponse = clienteResourceClient.dadosCliente(cpf);
            ResponseEntity<List<Cartao>> cartoesResponse = cartoesResourcesClient.getCartoesRendaAteh(renda);

            List<Cartao> cartoes = cartoesResponse.getBody();
            List<CartaoAprovado> cartaoAprovados = cartoes.stream().map(cartao -> {

                BigDecimal limiteBasico = cartao.getLimiteBasico();
                BigDecimal idadeBD = BigDecimal.valueOf(dadosClienteResponse.getBody().getIdade());

                BigDecimal fator = idadeBD.divide(BigDecimal.valueOf(10));
                BigDecimal limiteAprovado = fator.multiply(limiteBasico);

                CartaoAprovado aprovado = new CartaoAprovado();
                aprovado.setCartao(cartao.getNome());
                aprovado.setBandeira(cartao.getBandeira());
                aprovado.setLimiteAprovado(limiteAprovado);

                return aprovado;
            }).toList();
            return new RetornoAvaliacaoCliente(cartaoAprovados);
        } catch (FeignException.FeignClientException | RetryableException | FeignException.ServiceUnavailable e) {
            int status = e.status();
            if (HttpStatus.NOT_FOUND.value() == status) {
                throw new DadosClientesNotFoundException();
            }
            throw new ErroComunicacaoMicroservicesException(e.getMessage(), status);
        }

    }
}
