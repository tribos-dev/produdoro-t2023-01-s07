package dev.wakandaacademy.produdoro.tarefa.application.api;

import java.util.List;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RestController;
import dev.wakandaacademy.produdoro.config.security.service.TokenService;
import dev.wakandaacademy.produdoro.handler.APIException;
import dev.wakandaacademy.produdoro.tarefa.application.service.TarefaService;
import dev.wakandaacademy.produdoro.tarefa.domain.Tarefa;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RestController
@Log4j2
@RequiredArgsConstructor
public class TarefaRestController implements TarefaAPI {
	private final TarefaService tarefaService;
	private final TokenService tokenService;

	@Override
	public TarefaIdResponse postNovaTarefa(TarefaRequest tarefaRequest) {
		log.info("[inicia]  TarefaRestController - postNovaTarefa  ");
		TarefaIdResponse tarefaCriada = tarefaService.criaNovaTarefa(tarefaRequest);
		log.info("[finaliza]  TarefaRestController - postNovaTarefa");
		return tarefaCriada;
	}

	@Override
	public TarefaDetalhadoResponse detalhaTarefa(String token, UUID idTarefa) {
		log.info("[inicia] TarefaRestController - detalhaTarefa");
		String usuario = getUsuarioByToken(token);
		Tarefa tarefa = tarefaService.detalhaTarefa(usuario, idTarefa);
		log.info("[finaliza] TarefaRestController - detalhaTarefa");
		return new TarefaDetalhadoResponse(tarefa);
	}

	@Override
	public void editaTarefa(String token, UUID idTarefa, TarefaRequest tarefaRequest) {
		log.info("[inicia]  TarefaRestController - editaTarefa");
		String usuario = getUsuarioByToken(token);
		tarefaService.editaTarefa(usuario, idTarefa, tarefaRequest);
		log.info("[finaliza]  TarefaRestController - editaTarefa");
	}

	@Override
	public void incrementaPomodoro(String token, UUID idTarefa, UUID idUsuario) {
		log.info("[inicia] TarefaRestController - detalhaTarefa");
		String emailUsuario = getUsuarioByToken(token);
		tarefaService.incrementaPomodoro(idUsuario, idTarefa, emailUsuario);
	}

	@Override
	public void limpaTarefasConcluidas(String token, UUID idUsuario) {
		log.info("[inicia] TarefaRestController - limpaTarefasConcluidas");
		String usuarioEmail = getUsuarioByToken(token);
		tarefaService.removeTarefasConcluidas(usuarioEmail, idUsuario);
		log.info("[finaliza] TarefaRestController - limpaTarefasConcluidas");

	}
	@Override
	public void concluiTarefa(String token, UUID idTarefa) {
		log.info("[inicia] TarefaRestController - concluiTarefa");
		String usuarioEmail = getUsuarioByToken(token);
		tarefaService.concluiTarefa(usuarioEmail, idTarefa);
		log.info("[Finaliza] TarefaRestController - concluiTarefa");
	}
	@Override
	public void ativaTarefa(UUID idTarefa, UUID idUsuario, String token) {
		log.info("[inicia] TarefaRestController - ativaTarefa");
		String usuario = getUsuarioByToken(token);
		tarefaService.ativaTarefa(idTarefa, idUsuario, usuario);
		log.info("[finaliza] TarefaRestController - ativaTarefa");

	}

	private String getUsuarioByToken(String token) {
		log.debug("[token] {}", token);
		String usuario = tokenService.getUsuarioByBearerToken(token)
				.orElseThrow(() -> APIException.build(HttpStatus.UNAUTHORIZED, token));
		log.info("[usuario] {}", usuario);
		return usuario;
	}

	@Override
	public List<TarefaDetalhadoResponse> getListaTodasTarefas(String token, UUID idUsuario) {
		log.info("[inicia] TarefaRestController - getListaTodasTarefas");
		String usuario = getUsuarioByToken(token);
		List<TarefaDetalhadoResponse> tarefaLista = tarefaService.listaTodasTarefas(usuario, idUsuario);
		log.info("[finaliza] TarefaRestController - getListaTodasTarefas");
		return tarefaLista;
	}
}
