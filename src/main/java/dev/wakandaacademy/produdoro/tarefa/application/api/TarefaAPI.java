package dev.wakandaacademy.produdoro.tarefa.application.api;

import org.springframework.http.HttpStatus;

import org.springframework.web.bind.annotation.*;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;


import io.swagger.v3.oas.annotations.parameters.RequestBody;

import javax.validation.Valid;
import java.util.UUID;

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


    @PatchMapping("/ativa/{idTarefa}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    void ativaTarefa(@PathVariable UUID idTarefa, @RequestParam UUID idUsuario,
                     @RequestHeader(name = "Authorization",required = true) String token);

}
