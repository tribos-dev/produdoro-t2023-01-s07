package dev.wakandaacademy.produdoro.tarefa.application.service;

import dev.wakandaacademy.produdoro.DataHelper;
import dev.wakandaacademy.produdoro.handler.APIException;
import dev.wakandaacademy.produdoro.tarefa.application.api.TarefaIdResponse;
import dev.wakandaacademy.produdoro.tarefa.application.api.TarefaRequest;
import dev.wakandaacademy.produdoro.tarefa.application.repository.TarefaRepository;
import dev.wakandaacademy.produdoro.tarefa.domain.StatusAtivacaoTarefa;
import dev.wakandaacademy.produdoro.tarefa.domain.Tarefa;
import dev.wakandaacademy.produdoro.usuario.application.repository.UsuarioRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static dev.wakandaacademy.produdoro.DataHelper.getTarefaRequest;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
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
                ()-> tarefaApplicationService.ativaTarefa(idTarefaInvalido, idUsuario, usuarioEmail));

    }
}



