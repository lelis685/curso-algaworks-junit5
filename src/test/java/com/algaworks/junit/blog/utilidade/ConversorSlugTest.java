package com.algaworks.junit.blog.utilidade;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

class ConversorSlugTest {


    @Test
    void deveConverterJuntoComCodigo(){
        try (MockedStatic<GeradorCodigo> geradorCodigoMockedStatic = Mockito.mockStatic(GeradorCodigo.class)) {
            geradorCodigoMockedStatic.when(GeradorCodigo::gerar).thenReturn("123456");
            String slug = ConversorSlug.converterJuntoComCodigo("ola mundo java");

            assertEquals("ola-mundo-java-123456", slug);
        }


    }

}