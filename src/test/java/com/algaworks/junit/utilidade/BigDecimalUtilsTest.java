package com.algaworks.junit.utilidade;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.CsvSource;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class BigDecimalUtilsTest {


    @ParameterizedTest
    @CsvFileSource(resources = "/numeros.csv")
    void diferentes(BigDecimal x, BigDecimal y){
        assertFalse(BigDecimalUtils.iguais(x,y));
    }


}