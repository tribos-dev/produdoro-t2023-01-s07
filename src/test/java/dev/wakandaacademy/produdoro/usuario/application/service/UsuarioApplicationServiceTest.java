package dev.wakandaacademy.produdoro.usuario.application.service;

import dev.wakandaacademy.produdoro.credencial.application.service.CredencialService;
import dev.wakandaacademy.produdoro.pomodoro.application.service.PomodoroService;
import dev.wakandaacademy.produdoro.usuario.application.repository.UsuarioRepository;
import dev.wakandaacademy.produdoro.usuario.domain.Usuario;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;



@ContextConfiguration(classes = {UsuarioApplicationService.class })
@ExtendWith(SpringExtension.class)
@ExtendWith(MockitoExtension.class)
class UsuarioApplicationServiceTest {

    @MockBean
    private UsuarioRepository usuarioRepository;
    @Autowired
    private UsuarioApplicationService usuarioApplicationService;
    @MockBean
    private CredencialService credencialService;
    @MockBean
    private PomodoroService pomodoroService;
    @MockBean
    private Usuario usuario;


    private static final UUID usuario1 = UUID.fromString("466ac38d-c643-476b-80a1-fde044c63e3e");
    @Test
    @DisplayName("Testando pausa longa")
    void pausaLongaTeste(){
        when(usuarioRepository.salva((Usuario)any())).thenReturn(getUsuario());
        when(usuarioRepository.buscaUsuarioPorId((UUID)any())).thenReturn(getUsuario());
        usuarioApplicationService.mudaStatusPausaLongaId("vastiane1@gmail.com", UUID.randomUUID());
        verify(usuarioRepository).buscaUsuarioPorId((UUID)any());
        verify(usuarioRepository).salva((Usuario)any());
    }
    private Usuario getUsuario(){
        return Usuario.builder()
                .idUsuario(UUID.fromString("466ac38d-c643-476b-80a1-fde044c63e3e"))
                .email("vastiane1@gmail.com")
                .build();
    }
}