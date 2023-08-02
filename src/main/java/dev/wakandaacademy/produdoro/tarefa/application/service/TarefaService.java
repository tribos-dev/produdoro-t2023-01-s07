package dev.wakandaacademy.produdoro.tarefa.application.service;

import dev.wakandaacademy.produdoro.tarefa.application.api.TarefaDetalhadoResponse;
import dev.wakandaacademy.produdoro.tarefa.application.api.TarefaIdResponse;
import dev.wakandaacademy.produdoro.tarefa.application.api.TarefaRequest;
import dev.wakandaacademy.produdoro.tarefa.domain.Tarefa;

import java.util.List;
import java.util.UUID;
public interface TarefaService {
    TarefaIdResponse criaNovaTarefa(TarefaRequest tarefaRequest);
    Tarefa detalhaTarefa(String usuario, UUID idTarefa);
    void incrementaPomodoro(UUID idUsuario, UUID idTarefa, String emailUsuario);
    void removeTarefasConcluidas(String usuarioEmail, UUID idUsuario);
    void concluiTarefa(String usuarioEmail, UUID idTarefa);
	List<TarefaDetalhadoResponse> listaTodasTarefas(String usuario, UUID idUsuario);
    void ativaTarefa(UUID idTarefa, UUID idUsuario, String usuario);
}


