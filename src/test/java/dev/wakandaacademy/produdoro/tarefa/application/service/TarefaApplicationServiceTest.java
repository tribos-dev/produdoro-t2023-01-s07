package dev.wakandaacademy.produdoro.tarefa.application.service;

import static dev.wakandaacademy.produdoro.DataHelper.ID_TAREFA_VALIDO;
import static dev.wakandaacademy.produdoro.DataHelper.TOKEN_VALIDO;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import dev.wakandaacademy.produdoro.DataHelper;
import dev.wakandaacademy.produdoro.usuario.application.repository.UsuarioRepository;
import dev.wakandaacademy.produdoro.usuario.domain.Usuario;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import dev.wakandaacademy.produdoro.tarefa.application.api.TarefaIdResponse;
import dev.wakandaacademy.produdoro.tarefa.application.api.TarefaRequest;
import dev.wakandaacademy.produdoro.tarefa.application.repository.TarefaRepository;
import dev.wakandaacademy.produdoro.tarefa.domain.Tarefa;

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

    private Optional<Tarefa> OptionalTarefaValidaEsperada;
    private Tarefa tarefaValidaEsperada;
    private Usuario usuarioValido;
    private TarefaRequest editaTarefaRequest;

    @Test
    void deveRetornarIdTarefaNovaCriada() {
        TarefaRequest request = getTarefaRequest();
        when(tarefaRepository.salva(any())).thenReturn(new Tarefa(request));

        TarefaIdResponse response = tarefaApplicationService.criaNovaTarefa(request);

        assertNotNull(response);
        assertEquals(TarefaIdResponse.class, response.getClass());
        assertEquals(UUID.class, response.getIdTarefa().getClass());
    }



    public TarefaRequest getTarefaRequest() {
        TarefaRequest request = new TarefaRequest("tarefa 1", UUID.randomUUID(), null, null, 0);
        return request;
    }
    @Test
    void editaTarefa() {
        when(usuarioRepository.buscaUsuarioPorEmail(TOKEN_VALIDO)).
                thenReturn(usuarioValido);
        when(tarefaRepository.buscaTarefaPorId(ID_TAREFA_VALIDO)).
                thenReturn(OptionalTarefaValidaEsperada);
        tarefaValidaEsperada.altera(editaTarefaRequest);
        tarefaRepository.salva(tarefaValidaEsperada);
        tarefaApplicationService.editaTarefa(TOKEN_VALIDO, ID_TAREFA_VALIDO, editaTarefaRequest);
        assertEquals(editaTarefaRequest.getDescricao(), tarefaValidaEsperada.getDescricao());
    }
}
