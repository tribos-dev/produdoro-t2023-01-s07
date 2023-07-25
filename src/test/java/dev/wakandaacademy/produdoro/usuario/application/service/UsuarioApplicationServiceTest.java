package dev.wakandaacademy.produdoro.usuario.application.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.UUID;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import dev.wakandaacademy.produdoro.DataHelper;
import dev.wakandaacademy.produdoro.credencial.application.service.CredencialService;
import dev.wakandaacademy.produdoro.handler.APIException;
import dev.wakandaacademy.produdoro.pomodoro.application.service.PomodoroService;
import dev.wakandaacademy.produdoro.usuario.application.repository.UsuarioRepository;
import dev.wakandaacademy.produdoro.usuario.domain.Usuario;

@ContextConfiguration(classes = { UsuarioApplicationService.class })
@ExtendWith(SpringExtension.class)
@ExtendWith(MockitoExtension.class)
class UsuarioApplicationServiceTest {
	@MockBean
	private Usuario usuario;
	@Autowired
	private UsuarioApplicationService usuarioApplicationService;
	@MockBean
	private UsuarioRepository usuarioRepository;
	private static final UUID usuario1 = UUID.fromString("a713162f-20a9-4db9-a85b-90cd51ab18f4");
	@MockBean
	private CredencialService credencialService;
	@MockBean
	private PomodoroService pomodoroService;

	private Usuario getUser() {
		return Usuario.builder().idUsuario(UUID.fromString("71cf6a3c-a4e4-4a08-bc8b-05da9065e984"))
				.email("marily@gmail.com").build();
	}

	@Test
	@DisplayName("Muda Status para foco")
	void mudaStatusParaFocoTeste() {
		Usuario usuario = DataHelper.createUsuario();
		UUID idUsuario = usuario.getIdUsuario();
		String usuarioEmail = usuario.getEmail();
		when(usuarioRepository.buscaUsuarioPorEmail(anyString())).thenReturn(usuario);
		usuarioApplicationService.mudaStatusParaFoco(usuarioEmail, idUsuario);
		verify(usuarioRepository, times(1)).salva(any(Usuario.class));
	}

	@Test
	@DisplayName("Foco Exception")
	void mudaStatusParaFocoReturnException() {
		Usuario usuario = DataHelper.createUsuario();
		UUID idUsuario = UUID.randomUUID();
		String usuarioEmail = usuario.getEmail();
		when(usuarioRepository.buscaUsuarioPorEmail(anyString())).thenReturn(usuario);
		APIException ex = Assertions.assertThrows(APIException.class, () -> {
			usuarioApplicationService.mudaStatusParaFoco(usuarioEmail, idUsuario);
		});
		Assertions.assertEquals("Id não pertence ao usuario encontrado!", ex.getMessage());
	}

	@Test
	@DisplayName("Testando Pausa Curta")
	void testePausaCurta() {
		when(usuarioRepository.salva((Usuario) any())).thenReturn(getUsuario());
		when(usuarioRepository.buscaUsuarioPorId((UUID) any())).thenReturn(getUsuario());
		usuarioApplicationService.pausaCurta("marily@gmail.com", UUID.randomUUID());
		verify(usuarioRepository).buscaUsuarioPorId((UUID) any());
		verify(usuarioRepository).salva((Usuario) any());
	}

	private Usuario getUsuario() {
		return Usuario.builder().idUsuario(UUID.fromString("ca0e1b98-42c3-4b7f-98a1-b1c8d450d82e"))
				.email("marily@gmail.com").build();
	}
}