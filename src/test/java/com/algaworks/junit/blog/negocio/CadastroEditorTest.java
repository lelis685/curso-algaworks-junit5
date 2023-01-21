package com.algaworks.junit.blog.negocio;

import com.algaworks.junit.blog.armazenamento.ArmazenamentoEditor;
import com.algaworks.junit.blog.exception.RegraNegocioException;
import com.algaworks.junit.blog.modelo.Editor;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoSettings;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;


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
    void beforeEach(){
        editor = new Editor(null, "marco", "marco@mail.com", BigDecimal.TEN, true);

        Mockito.when(armazenamentoEditor.salvar(editor))
                .thenAnswer(invocationOnMock -> {
                    Editor editorPassado = invocationOnMock.getArgument(0, Editor.class);
                    editorPassado.setId(1L);
                    return editorPassado;
                });
    }


    @Test
    public void dado_editor_valido_quando_criar_entao_retorna_id_cadastro(){
        Editor editorsalvo = cadastroEditor.criar(editor);
        assertEquals(1L, editorsalvo.getId());
    }

//    @Test
//    public void Dado_um_editor_null_Quando_criar_Entao_deve_lancar_exception() {
//        assertThrows(NullPointerException.class, ()-> cadastroEditor.criar(null));
//        assertFalse(armazenamentoEditor.chamouSalvar);
//    }
//
//    @Test
//    void Dado_um_editor_com_email_existente_Quando_criar_Entao_deve_lancar_exception() {
//        editor.setEmail("alex.existe@email.com");
//        assertThrows(RegraNegocioException.class, ()-> cadastroEditor.criar(editor));
//    }
//
//    @Test
//    void Dado_um_editor_com_email_existente_Quando_criar_Entao_nao_deve_salvar() {
//        editor.setEmail("alex.existe@email.com");
//        try {
//            cadastroEditor.criar(editor);
//        } catch (RegraNegocioException e) { }
//        assertFalse(armazenamentoEditor.chamouSalvar);
//    }




}