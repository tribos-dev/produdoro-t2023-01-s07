package dev.wakandaacademy.produdoro.tarefa.application.service;

import dev.wakandaacademy.produdoro.DataHelper;
import dev.wakandaacademy.produdoro.handler.APIException;
import dev.wakandaacademy.produdoro.tarefa.application.api.TarefaIdResponse;
import dev.wakandaacademy.produdoro.tarefa.application.api.TarefaRequest;
import dev.wakandaacademy.produdoro.tarefa.application.repository.TarefaRepository;
import dev.wakandaacademy.produdoro.tarefa.domain.StatusAtivacaoTarefa;
import dev.wakandaacademy.produdoro.tarefa.domain.Tarefa;
import dev.wakandaacademy.produdoro.usuario.application.repository.UsuarioRepository;
import dev.wakandaacademy.produdoro.usuario.domain.Usuario;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.Optional;
import java.util.UUID;

import static dev.wakandaacademy.produdoro.DataHelper.getTarefaRequest;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TarefaApplicationServiceTest {

    //	@Autowired
    @InjectMocks
    TarefaApplicationService tarefaApplicationService;

    //	@MockBean
    @Mock
    TarefaRepository tarefaRepository;

    @Mock
    UsuarioRepository usuarioRepository;

    @Test
    void deveRetornarIdTarefaNovaCriada() {
        TarefaRequest request = getTarefaRequest();
        when(tarefaRepository.salva(any())).thenReturn(new Tarefa(request));

        TarefaIdResponse response = tarefaApplicationService.criaNovaTarefa(request);

        assertNotNull(response);
        assertEquals(TarefaIdResponse.class, response.getClass());
        assertEquals(UUID.class, response.getIdTarefa().getClass());
    }

    @Test
    @DisplayName("Teste Ativa Tarefa")
    void ativaTarefaDeveRetornarTarefaAtiva() {
        UUID idTarefa = DataHelper.createTarefa().getIdTarefa();
        UUID idUsuario = DataHelper.createUsuario().getIdUsuario();
        String email = "usuariotest@gmail.com";
        Tarefa retorno = DataHelper.getTarefaForAtivaTarefa();
        when(usuarioRepository.buscaUsuarioPorEmail(anyString())).thenReturn(DataHelper.createUsuario());
        when(tarefaRepository.buscaTarefaPorId(any())).thenReturn(Optional.of(DataHelper.createTarefa()));
        tarefaApplicationService.ativaTarefa(idTarefa, idUsuario, email);
        verify(tarefaRepository,times(1)).buscaTarefaPorId(idTarefa);
        assertEquals(StatusAtivacaoTarefa.ATIVA, retorno.getStatusAtivacao());

    }
    @Test
    @DisplayName("Teste Ativa Tarefa - Test Exception")
    void ativaTarefaDeveRetornarException() {
        UUID idTarefaInvalido = UUID.fromString("a713162f-20a9-4db9-a85b-90cd51ab18f4");
        UUID idUsuario = DataHelper.getUsuarioForAtivaTarefa().getIdUsuario();
        String usuarioEmail = DataHelper.getUsuarioForAtivaTarefa().getEmail();
        when(tarefaRepository.buscaTarefaPorId(idTarefaInvalido)).thenThrow(APIException.class);
        assertThrows(APIException.class,
                () -> tarefaApplicationService.ativaTarefa(idTarefaInvalido, idUsuario, usuarioEmail));
    }
    @Test
    void deveAtivarTarefa(){
    }
    @Test
    void deveIncrementarUmPomodoroATarefaOk(){
        Usuario usuario = DataHelper.createUsuario();
        UUID idUsuario = usuario.getIdUsuario();
        Tarefa tarefa = DataHelper.createTarefa();
        UUID idTarefa = tarefa.getIdTarefa();
        String email = usuario.getEmail();
        when(usuarioRepository.buscaUsuarioPorEmail(anyString())).thenReturn(usuario);
        when(tarefaRepository.buscaTarefaPorId(any())).thenReturn(Optional.of(tarefa));
        tarefaApplicationService.incrementaPomodoro(idUsuario, idTarefa, email);
        verify(tarefaRepository,times(1)).salva(any());
    }

    @Test
    void deveRetornarExcecaoUsuarioNaoDono(){
        Usuario usuario = DataHelper.createUsuario();
        UUID idUsuario = usuario.getIdUsuario();
        Tarefa tarefa = Tarefa.builder().idUsuario(UUID.randomUUID()).build();
        UUID idTarefa = tarefa.getIdTarefa();
        String email = usuario.getEmail();
        when(usuarioRepository.buscaUsuarioPorEmail(anyString())).thenReturn(usuario);
        when(tarefaRepository.buscaTarefaPorId(any())).thenReturn(Optional.of(tarefa));
        APIException ex = assertThrows(APIException.class, () -> tarefaApplicationService.incrementaPomodoro(idUsuario, idTarefa, email));
        assertEquals("Usuário não é dono da Tarefa solicitada!" , ex.getMessage());
        assertEquals(HttpStatus.UNAUTHORIZED , ex.getStatusException());
    }

}



