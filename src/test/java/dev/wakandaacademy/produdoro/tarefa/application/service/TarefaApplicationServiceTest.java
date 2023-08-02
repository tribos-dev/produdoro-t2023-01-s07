package dev.wakandaacademy.produdoro.tarefa.application.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;

import java.util.Optional;
import java.util.UUID;

import dev.wakandaacademy.produdoro.DataHelper;
import dev.wakandaacademy.produdoro.handler.APIException;
import dev.wakandaacademy.produdoro.tarefa.domain.StatusTarefa;
import dev.wakandaacademy.produdoro.usuario.application.repository.UsuarioRepository;
import dev.wakandaacademy.produdoro.usuario.domain.Usuario;
import org.junit.jupiter.api.Assertions;

import dev.wakandaacademy.produdoro.tarefa.application.api.TarefaIdResponse;
import dev.wakandaacademy.produdoro.tarefa.application.api.TarefaRequest;
import dev.wakandaacademy.produdoro.tarefa.application.repository.TarefaRepository;
import dev.wakandaacademy.produdoro.tarefa.domain.StatusAtivacaoTarefa;
import dev.wakandaacademy.produdoro.tarefa.domain.Tarefa;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.DisplayName;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import dev.wakandaacademy.produdoro.tarefa.application.api.TarefaDetalhadoResponse;


@ExtendWith(MockitoExtension.class)
class TarefaApplicationServiceTest {

	// @Autowired
	@InjectMocks
	TarefaApplicationService tarefaApplicationService;

	// @MockBean
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

	public TarefaRequest getTarefaRequest() {
		TarefaRequest request = new TarefaRequest("tarefa 1", UUID.randomUUID(), null, null, 0);
		return request;
	}

	@Test
	@DisplayName("Deve retornar uma lista com todas as tarefas")
	void deveListarTodasTarefasDoUsuario() {
		Usuario usuario = DataHelper.createUsuario();
		UUID usuario1 = usuario.getIdUsuario();
		when(usuarioRepository.buscaUsuarioPorEmail(anyString())).thenReturn(usuario);
		when(tarefaRepository.buscarTodasTarefas(any(UUID.class))).thenReturn(DataHelper.createListTarefa());
		List<TarefaDetalhadoResponse> lista = tarefaApplicationService.listaTodasTarefas("email@email.com", usuario1);
		assertNotNull(lista);
		assertFalse(lista.isEmpty());
    }

    @Test
    public void deveConcluirTarefa() {

        Usuario usuario = DataHelper.createUsuario();
        Tarefa tarefa = DataHelper.createTarefa();

        when(usuarioRepository.buscaUsuarioPorEmail(any())).thenReturn(usuario);
        when(tarefaRepository.buscaTarefaPorId(any())).thenReturn(Optional.of(tarefa));

        tarefaApplicationService.concluiTarefa(usuario.getEmail(), tarefa.getIdTarefa());
        tarefaRepository.salva(tarefa);

        verify(usuarioRepository,times(1)).buscaUsuarioPorEmail(usuario.getEmail());
        verify(tarefaRepository,times(1)).buscaTarefaPorId(tarefa.getIdTarefa());

        assertEquals(StatusTarefa.CONCLUIDA, tarefa.getStatus());
    }

    @Test
    public void tarefaNaoDeveSerConcluida() {

        Usuario usuario = DataHelper.createUsuario();

        when(usuarioRepository.buscaUsuarioPorEmail(any())).thenReturn(usuario);
        when(tarefaRepository.buscaTarefaPorId(any()))
                .thenThrow(APIException.build(HttpStatus.BAD_REQUEST, "Id tarefa não encontrado!"));

        APIException ex = Assertions.assertThrows(APIException.class,
                () -> tarefaApplicationService.concluiTarefa(usuario.getEmail(), UUID.randomUUID()));

    }

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
	@DisplayName("Teste Ativa Tarefa")
	void ativaTarefaDeveRetornarTarefaAtiva() {
		UUID idTarefa = DataHelper.createTarefa().getIdTarefa();
		UUID idUsuario = DataHelper.createUsuario().getIdUsuario();
		String email = "usuariotest@gmail.com";
		Tarefa retorno = DataHelper.getTarefaForAtivaTarefa();
		when(usuarioRepository.buscaUsuarioPorEmail(anyString())).thenReturn(DataHelper.createUsuario());
		when(tarefaRepository.buscaTarefaPorId(any())).thenReturn(Optional.of(DataHelper.createTarefa()));
		tarefaApplicationService.ativaTarefa(idTarefa, idUsuario, email);
		verify(tarefaRepository, times(1)).buscaTarefaPorId(idTarefa);
		assertEquals(StatusAtivacaoTarefa.ATIVA, retorno.getStatusAtivacao());

	}
}