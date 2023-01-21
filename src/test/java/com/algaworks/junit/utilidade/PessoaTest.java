package com.algaworks.junit.utilidade;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PessoaTest {

    @Test
    void assercaoAgrupada(){
        Pessoa pessoa = new Pessoa("marco", "silva");
        assertAll(
                () -> assertEquals("marco", pessoa.getNome()),
                () -> assertEquals("silva", pessoa.getSobrenome())
        );
    }

}