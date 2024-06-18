package io.github.lafsdev.msavaliadorcredito.ex;

public class DadosClientesNotFoundException extends Exception{

    public DadosClientesNotFoundException() {
        super("Dados do cliente não encontrado pelo cpf informado");
    }
}
