package dev.wakandaacademy.produdoro;

import dev.wakandaacademy.produdoro.pomodoro.domain.ConfiguracaoPadrao;
import dev.wakandaacademy.produdoro.tarefa.application.api.TarefaRequest;
import dev.wakandaacademy.produdoro.tarefa.domain.StatusAtivacaoTarefa;
import dev.wakandaacademy.produdoro.tarefa.domain.Tarefa;
import dev.wakandaacademy.produdoro.usuario.application.api.UsuarioNovoRequest;
import dev.wakandaacademy.produdoro.usuario.domain.StatusUsuario;
import dev.wakandaacademy.produdoro.usuario.domain.Usuario;

import java.util.List;
import java.util.UUID;

public class DataHelper {

    private static final UUID usuario1 = UUID.fromString("a713162f-20a9-4db9-a85b-90cd51ab18f4");
    public static final String TOKEN_VALIDO = "tokenValido@email.com";
    public static final UUID ID_TAREFA_VALIDO = UUID.fromString("8d58875d-2455-4075-8b5d-57c73fcf1241");
    public static final UUID ID_USUARIO_VALIDO = UUID.fromString("0d51b6fe-ff69-4e36-a6ee-7b6983237872");
    public static final UUID ID_AREA = UUID.fromString("462ff63d-412b-4a19-9c43-bc5969b15989");
    public static final UUID ID_PROJETO = UUID.fromString("1fc65f65-4862-4598-a30c-f317dfb3cbe7");


    public static Usuario createUsuario() {
        return Usuario.builder().email("email@email.com").status(StatusUsuario.PAUSA_LONGA).idUsuario(usuario1).build();
    }

    public static Tarefa createTarefa() {
        return Tarefa.builder().contagemPomodoro(1).idTarefa(UUID.fromString("06fb5521-9d5a-461a-82fb-e67e3bedc6eb"))
                .idUsuario(usuario1).descricao("descricao tarefa").statusAtivacao(StatusAtivacaoTarefa.INATIVA).build();
    }

    public static UsuarioNovoRequest getUsuarioRequest() {
        UsuarioNovoRequest userReq = new UsuarioNovoRequest("email@email.com", "12345");
        return userReq;
    }

    public static ConfiguracaoPadrao getConfig() {
        return ConfiguracaoPadrao.builder().tempoMinutosFoco(25).tempoMinutosPausaCurta(5).tempoMinutosPausaLonga(15)
                .repeticoesParaPausaLonga(3).build();
    }

    public static TarefaRequest getTarefaRequest() {
        Usuario usuario = createUsuario();

        TarefaRequest tarefaReq = new TarefaRequest("descricao", usuario.getIdUsuario(), null, null, 1);
        return tarefaReq;
    }

    public static List<Tarefa> createListTarefa() {
        return List.of(Tarefa.builder().idTarefa(UUID.randomUUID()).descricao("tarefa 1").idUsuario(usuario1).build(),
                Tarefa.builder().build(),
                Tarefa.builder().idTarefa(UUID.randomUUID()).descricao("tarefa 2").idUsuario(usuario1).build(),
                Tarefa.builder().build(),
                Tarefa.builder().idTarefa(UUID.randomUUID()).descricao("tarefa 3").idUsuario(usuario1).build(),
                Tarefa.builder().build(),
                Tarefa.builder().idTarefa(UUID.randomUUID()).descricao("tarefa 4").idUsuario(usuario1).build(),
                Tarefa.builder().build()

        );
    }

    public static Tarefa getTarefaForAtivaTarefa() {
        return Tarefa.builder()
                .contagemPomodoro(2)
                .descricao("teste concluído")
                .statusAtivacao(StatusAtivacaoTarefa.ATIVA)
                .idUsuario(UUID.fromString("b713162f-20a9-4db9-a85b-90cd51ab18f5"))
                .idArea(UUID.randomUUID()).build();
    }
    
    public static Usuario getUsuarioForAtivaTarefa() {
        return Usuario.builder().idUsuario(ID_USUARIO_VALIDO)
                .email(TOKEN_VALIDO)
                .configuracao(null)
                .status(StatusUsuario.FOCO)
                .quantidadePomodorosPausaCurta(3)
                .build();
    }
    
}