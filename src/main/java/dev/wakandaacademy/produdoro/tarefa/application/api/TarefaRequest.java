package dev.wakandaacademy.produdoro.tarefa.application.api;



import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.NonNull;
import lombok.Value;

import java.util.UUID;

@Value
//@NoArgsConstructor(access = AccessLevel.PUBLIC, force = true)
//@AllArgsConstructor(access = AccessLevel.PRIVATE)

public class TarefaRequest {

    @NotBlank
    @Size(message = "Campo descrição tarefa não pode estar vazio", max = 255, min = 3)
    private String descricao;
    @NonNull
    private UUID idUsuario;
    private UUID idArea;
    private UUID idProjeto;
    private int contagemPomodoro;


}
