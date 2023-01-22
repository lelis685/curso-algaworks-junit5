package com.algaworks.junit.blog.negocio;

import com.algaworks.junit.blog.armazenamento.ArmazenamentoEditor;
import com.algaworks.junit.blog.exception.EditorNaoEncontradoException;
import com.algaworks.junit.blog.exception.RegraNegocioException;
import com.algaworks.junit.blog.modelo.Editor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoSettings;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@MockitoSettings
class CadastroEditorTest {

    @Captor
    ArgumentCaptor<Mensagem> mensagemArgumentCaptor;

    @Mock
    GerenciadorEnvioEmail gerenciadorEnvioEmail;

    @Mock
    ArmazenamentoEditor armazenamentoEditor;

    @InjectMocks
    CadastroEditor cadastroEditor;

    @Nested
    class CadastroComEditorValido {

        @Spy
        Editor editor = new Editor(null, "marco", "marco@mail.com", BigDecimal.TEN, true);

        @BeforeEach
        void beforeEach() {
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
                    () -> assertThrows(RuntimeException.class, () -> cadastroEditor.criar(editor)),
                    () -> verify(gerenciadorEnvioEmail, never()).enviarEmail(any())
            );
        }


        @Test
        public void dado_editor_valido_quando_cadastrar_entao_retorna_deve_enviar_email_destinatario_correto() {
            Editor editorSalvo = cadastroEditor.criar(editor);
            verify(gerenciadorEnvioEmail).enviarEmail(mensagemArgumentCaptor.capture());
            Mensagem mensagemPassada = mensagemArgumentCaptor.getValue();
            assertEquals(editorSalvo.getEmail(), mensagemPassada.getDestinatario());
        }


        @Test
        public void dado_editor_valido_quando_cadastrar_entao_deve_verificar_email() {
            Editor editorSpy = spy(editor);
            cadastroEditor.criar(editorSpy);
            verify(editorSpy, atLeast(1)).getEmail();
        }


        @Test
        public void dado_editor_com_email_inexistente_quando_cadastrar_entao_deve_lancar_Exception() {
            when(armazenamentoEditor.encontrarPorEmail("marco@mail.com"))
                    .thenReturn(Optional.empty())
                    .thenReturn(Optional.of(editor));

            Editor editorEmailExistente = new Editor(null, "marco", "marco@mail.com", BigDecimal.TEN, true);
            cadastroEditor.criar(editor);
            assertThrows(RegraNegocioException.class, () -> cadastroEditor.criar(editorEmailExistente));
        }


        @Test
        void dado_editor_valido_quando_cadastrar_entao_enviar_email_apos_Salvar() {
            cadastroEditor.criar(editor);
            InOrder inOrder = inOrder(armazenamentoEditor, gerenciadorEnvioEmail);
            inOrder.verify(armazenamentoEditor, times(1)).salvar(editor);
            inOrder.verify(gerenciadorEnvioEmail, times(1)).enviarEmail(any());
        }
    }

    @Nested
    class CadastroComEditorNull{
        @Test
        void dado_editor_null_quando_cadastrar_entao_deve_lancar_exception() {

            assertThrows(NullPointerException.class, () -> cadastroEditor.criar(null));
            verify(armazenamentoEditor, never()).salvar(any());
            verify(gerenciadorEnvioEmail, never()).enviarEmail(any());
        }
    }


    @Nested
    class EdicaoComEditorValido{
        @Spy
        Editor editor = new Editor(1L, "marco", "marco@mail.com", BigDecimal.TEN, true);

        @BeforeEach
        void init(){
            when(armazenamentoEditor.salvar(editor)).thenAnswer(invocationOnMock -> invocationOnMock.getArgument(0, Editor.class));
            when(armazenamentoEditor.encontrarPorId(1L)).thenReturn(Optional.of(editor));
        }

        @Test
        void dado_um_editor_valido_quando_editar_entao_deve_alterar_editor_salvo(){
            Editor editorAtual = new Editor(1L, "marco", "marco@mail.com", BigDecimal.TEN, false);
            cadastroEditor.editar(editorAtual);
            verify(editor, times(1)).atualizarComDados(editorAtual);

            InOrder inOrder = inOrder(editor, armazenamentoEditor);
            inOrder.verify(editor).atualizarComDados(editorAtual);
            inOrder.verify(armazenamentoEditor).salvar(editor);
        }

    }



    @Nested
    class EdicaoComEditorInexistente {

        Editor editor = new Editor(99L, "Alex", "alex@email.com", BigDecimal.TEN, true);

        @BeforeEach
        void init() {
            Mockito.when(armazenamentoEditor.encontrarPorId(99L)).thenReturn(Optional.empty());
        }

        @Test
        void Dado_um_editor_que_nao_exista_Quando_editar_Entao_deve_lancar_exception() {
            assertThrows(EditorNaoEncontradoException.class, ()-> cadastroEditor.editar(editor));
            verify(armazenamentoEditor, never()).salvar(Mockito.any(Editor.class));
        }

    }




}