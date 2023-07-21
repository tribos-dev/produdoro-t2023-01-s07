package dev.wakandaacademy.produdoro.tarefa.application.api;

import dev.wakandaacademy.produdoro.tarefa.domain.StatusAtivacaoTarefa;
import dev.wakandaacademy.produdoro.tarefa.domain.Tarefa;
import lombok.Value;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Value


public class ListTarefaResponse {
    private UUID idTarefa;
    private String descricao;
    private StatusAtivacaoTarefa statusAtivacao;


    public static List<ListTarefaResponse> converte(List<Tarefa> tarefas) {
        return tarefas.stream()
                .map(ListTarefaResponse ::new)
                .collect(Collectors.toList());
    }

    public ListTarefaResponse(Tarefa tarefa) {
        this.idTarefa = tarefa.getIdTarefa();
        this.descricao = tarefa.getDescricao();
        this.statusAtivacao = tarefa.getStatusAtivacao();
    }
}
