package com.algaworks.junit.blog.negocio;

import com.algaworks.junit.blog.armazenamento.ArmazenamentoEditor;
import com.algaworks.junit.blog.modelo.Editor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoSettings;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@MockitoSettings
class CadastroEditorTest {

    @Mock
    GerenciadorEnvioEmail gerenciadorEnvioEmail;

    @Mock
    ArmazenamentoEditor armazenamentoEditor;

    @InjectMocks
    CadastroEditor cadastroEditor;

    Editor editor;

    @BeforeEach
    void beforeEach() {
        editor = new Editor(null, "marco", "marco@mail.com", BigDecimal.TEN, true);

        when(armazenamentoEditor.salvar(any()))
                .thenAnswer(invocationOnMock -> {
                    Editor editorPassado = invocationOnMock.getArgument(0, Editor.class);
                    editorPassado.setId(1L);
                    return editorPassado;
                });
    }


    @Test
    public void dado_editor_valido_quando_criar_entao_retorna_id_cadastro() {
        Editor editorsalvo = cadastroEditor.criar(editor);
        assertEquals(1L, editorsalvo.getId());
    }

    @Test
    public void dado_editor_valido_quando_criar_entao_retorna_deve_chamar_metodo_salvar() {
        cadastroEditor.criar(editor);
        Mockito.verify(armazenamentoEditor, Mockito.times(1))
                .salvar(Mockito.eq(editor));
    }


    @Test
    public void dado_editor_ivalido_quando_criar_deve_lancar_exception_entao_nao_deve_enviar_email() {
        when(armazenamentoEditor.salvar(editor)).thenThrow(new RuntimeException());
        assertAll(
                () ->   assertThrows(RuntimeException.class, ()-> cadastroEditor.criar(editor)),
                () ->   verify(gerenciadorEnvioEmail, never()).enviarEmail(any())
        );
    }



}