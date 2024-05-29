package io.github.lafsdev.mscartoes.application.service;

import io.github.lafsdev.mscartoes.domain.ClienteCartao;
import io.github.lafsdev.mscartoes.infra.repository.ClienteCartaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClienteCartaoService {

    private final ClienteCartaoRepository clienteCartaoRepository;

    public List<ClienteCartao> listCartoesByCpf(String cpf) {
        return this.clienteCartaoRepository.findByCpf(cpf);
    }
}
