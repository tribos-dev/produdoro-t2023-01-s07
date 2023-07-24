package dev.wakandaacademy.produdoro.tarefa.application.api;

import java.util.UUID;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/tarefa")
public interface TarefaAPI {
    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    TarefaIdResponse postNovaTarefa(@RequestBody @Valid TarefaRequest tarefaRequest);

    @GetMapping("/{idTarefa}")
    @ResponseStatus(code = HttpStatus.OK)
    TarefaDetalhadoResponse detalhaTarefa(@RequestHeader(name = "Authorization", required = true) String token,
                                          @PathVariable UUID idTarefa);

    @PatchMapping("/incrementaPomodoro")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    void incrementaPomodoro(@RequestHeader(name = "Authorization", required = true) String token,
                            @RequestParam(name = "idTarefa", required = true) UUID idTarefa,
                            @RequestParam(name = "idUsuario", required = true) UUID idUsuario);


}
