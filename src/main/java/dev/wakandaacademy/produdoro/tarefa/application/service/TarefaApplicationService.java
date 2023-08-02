package dev.wakandaacademy.produdoro.tarefa.application.service;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import dev.wakandaacademy.produdoro.handler.APIException;
import dev.wakandaacademy.produdoro.tarefa.application.api.TarefaDetalhadoResponse;
import dev.wakandaacademy.produdoro.tarefa.application.api.TarefaIdResponse;
import dev.wakandaacademy.produdoro.tarefa.application.api.TarefaRequest;
import dev.wakandaacademy.produdoro.tarefa.application.repository.TarefaRepository;
import dev.wakandaacademy.produdoro.tarefa.domain.Tarefa;
import dev.wakandaacademy.produdoro.usuario.application.repository.UsuarioRepository;
import dev.wakandaacademy.produdoro.usuario.domain.Usuario;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
@RequiredArgsConstructor
public class TarefaApplicationService implements TarefaService {
	private final TarefaRepository tarefaRepository;
	private final UsuarioRepository usuarioRepository;

	@Override
	public TarefaIdResponse criaNovaTarefa(TarefaRequest tarefaRequest) {
		log.info("[inicia] TarefaApplicationService - criaNovaTarefa");
		Tarefa tarefaCriada = tarefaRepository.salva(new Tarefa(tarefaRequest));
		log.info("[finaliza] TarefaApplicationService - criaNovaTarefa");
		return TarefaIdResponse.builder().idTarefa(tarefaCriada.getIdTarefa()).build();
	}

	@Override
	public Tarefa detalhaTarefa(String usuario, UUID idTarefa) {
		log.info("[inicia] TarefaApplicationService - detalhaTarefa");
		Usuario usuarioPorEmail = usuarioRepository.buscaUsuarioPorEmail(usuario);
		log.info("[usuarioPorEmail] {}", usuarioPorEmail);
		Tarefa tarefa = tarefaRepository.buscaTarefaPorId(idTarefa)
				.orElseThrow(() -> APIException.build(HttpStatus.NOT_FOUND, "Tarefa não encontrada!"));
		tarefa.pertenceAoUsuario(usuarioPorEmail);
		log.info("[finaliza] TarefaApplicationService - detalhaTarefa");
		return tarefa;
	}

	@Override
	public List<TarefaDetalhadoResponse> listaTodasTarefas(String usuario, UUID idUsuario) {
		log.info("[inicia] TarefaApplicationService - listaTodasTarefas");
		Usuario usuarioPorEmail = usuarioRepository.buscaUsuarioPorEmail(usuario);
		validaUsuario(usuarioPorEmail, idUsuario);
		List<Tarefa> tarefas = tarefaRepository.buscarTodasTarefas(idUsuario);
		log.info("[finaliza] TarefaApplicationService - listaTodasTarefas");
		return TarefaDetalhadoResponse.converte(tarefas);
	}

	private void validaUsuario(Usuario usuario, UUID idUsuario) {
		if (!usuario.getIdUsuario().equals(idUsuario)) {
			throw APIException.build(HttpStatus.BAD_REQUEST, "Usuário não encontrado");
		}
	}

    @Override
    public void concluiTarefa(String usuarioEmail, UUID idTarefa) {
        log.info("[inicia] TarefaApplicationService - concluiTarefa");
        Tarefa tarefa = detalhaTarefa(usuarioEmail, idTarefa);
        tarefa.concluiTarefa();
        tarefaRepository.salva(tarefa);
        log.info("[finaliza] TarefaApplicationService - concluiTarefa");

    }
    
    @Override
    public void removeTarefasConcluidas(String usuarioEmail, UUID idUsuario) {
        log.info("[inicia] TarefaApplicationService - removeTarefasConcluidas");
        usuarioRepository.buscaUsuarioPorEmail(usuarioEmail);
        tarefaRepository.listTarefasConcluidas(idUsuario);
        tarefaRepository.limpaTarefasConcluidas(idUsuario);
        log.info("[finaliza] TarefaApplicationService - removeTarefasConcluidas");

    }

    @Override
    public void incrementaPomodoro(UUID idUsuario, UUID idTarefa, String emailUsuario) {
        log.info("[inicia] TarefaApplicationService - incrementaPomodoro");
        Tarefa tarefa = detalhaTarefa(emailUsuario, idTarefa);
        log.info("incrementa 1 Pomodoro");
        tarefa.incrementaPomodoro();
        tarefaRepository.salva(tarefa);
        log.info("[finaliza] TarefaApplicationService - incrementaPomodoro");
    }

    @Override
    public void ativaTarefa(UUID idTarefa, UUID idUsuario, String usuario) {
        log.info("[inicia] TarefaApplicationService - ativaTarefa");
        Usuario usuarioPorEmail = usuarioRepository.buscaUsuarioPorEmail(usuario);
        Tarefa tarefa = tarefaRepository.buscaTarefaPorId(idTarefa)
                .orElseThrow(() -> APIException.build(HttpStatus.BAD_REQUEST, "ID da Tarefa inválido"));
        tarefa.pertenceAoUsuario(usuarioPorEmail);
        tarefa.validaUsuario(idUsuario);
        tarefa.ativaTarefa();
        tarefaRepository.desativaTarefa(idUsuario);
        tarefaRepository.salva(tarefa);
        log.info("[finaliza] TarefaApplicationService - ativaTarefa");

    }
}
