package com.tispucminas.sistemaezsnack.service;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import com.tispucminas.sistemaezsnack.dto.UsuarioCreateDTO;
import com.tispucminas.sistemaezsnack.dto.UsuarioResponseDTO;
import com.tispucminas.sistemaezsnack.dto.UsuarioUpdateDTO;
import com.tispucminas.sistemaezsnack.exception.NotFoundException;
import com.tispucminas.sistemaezsnack.exception.UnauthorizedException;
import com.tispucminas.sistemaezsnack.factory.impl.AdminEscolaFactory;
import com.tispucminas.sistemaezsnack.factory.impl.AdminFactory;
import com.tispucminas.sistemaezsnack.factory.impl.AlunoFactory;
import com.tispucminas.sistemaezsnack.factory.impl.ResponsavelFactory;
import com.tispucminas.sistemaezsnack.model.Usuario;
import com.tispucminas.sistemaezsnack.repository.UsuarioRepository;

@ExtendWith(MockitoExtension.class)
public class UsuarioServiceTest {

    @InjectMocks
    private UsuarioService usuarioService;

    @Mock
    private AdminFactory adminFactory;

    @Mock
    private AdminEscolaFactory adminEscolaFactory;

    @Mock
    private AlunoFactory alunoFactory;

    @Mock
    private ResponsavelFactory responsavelFactory;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Test
    void login_withValidCredentials_shouldReturnUsuarioResponseDTO() throws NoSuchAlgorithmException {
        MockedStatic<SenhaUtilService> senhaUtilService = mockStatic(SenhaUtilService.class);
        String user = "testUser";
        String senha = "password123";
        byte[] senhaCriptografada = criptografarSenha(senha);

        Usuario usuario = new Usuario();
        usuario.setUser(user);
        usuario.setSenha(senhaCriptografada);

        when(usuarioRepository.findByUser(user)).thenReturn(usuario);
        senhaUtilService.when(() -> SenhaUtilService.criptografarSenha(any())).thenReturn(senhaCriptografada);
    

        UsuarioResponseDTO result = usuarioService.login(user, senha);

        assertNotNull(result);
        verify(usuarioRepository, times(1)).findByUser(user);
        senhaUtilService.close();
    }

    @Test
    void login_withInvalidCredentials_shouldReturnNull() throws NoSuchAlgorithmException {
        String user = "testUser";
        String senha = "wrongPassword";

        Usuario usuario = new Usuario();
        usuario.setUser(user);
        usuario.setSenha(criptografarSenha("password123"));

        when(usuarioRepository.findByUser(user)).thenReturn(usuario);

        UsuarioResponseDTO result = usuarioService.login(user, senha);

        assertNull(result);
        verify(usuarioRepository, times(1)).findByUser(user);
    }

    @Test
    void login_withUserNotFound_shouldReturnNull() throws NoSuchAlgorithmException {
        String user = "nonExistentUser";
        String senha = "password123";

        when(usuarioRepository.findByUser(user)).thenReturn(null);

        UsuarioResponseDTO result = usuarioService.login(user, senha);

        assertNull(result);
        verify(usuarioRepository, times(1)).findByUser(user);
    }

    @Test
    void create_withValidData_shouldReturnUsuarioResponseDTO() throws Exception {
        UsuarioCreateDTO dto = new UsuarioCreateDTO();
        dto.setTipo("ADMIN");
        dto.setSenha("password123");

        Usuario usuarioCriado = new Usuario();
        usuarioCriado.setId(1L);
        usuarioCriado.setSenha(criptografarSenha(dto.getSenha()));

        when(adminFactory.createUsuario(dto)).thenReturn(usuarioCriado);
        when(usuarioRepository.save(usuarioCriado)).thenReturn(usuarioCriado);

        UsuarioResponseDTO result = usuarioService.create(dto);

        assertNotNull(result);
        verify(adminFactory, times(1)).createUsuario(dto);
        verify(usuarioRepository, times(1)).save(usuarioCriado);
    }

    @Test
    void create_withNotExistentUserType_shouldReturnUsuarioResponseDTO() throws Exception {
        UsuarioCreateDTO dto = new UsuarioCreateDTO();
        dto.setTipo("TIPO_INEXISTENTE");
        dto.setSenha("password123");

        Usuario usuarioCriado = new Usuario();
        usuarioCriado.setId(1L);
        usuarioCriado.setSenha(criptografarSenha(dto.getSenha()));


        assertThrows(IllegalArgumentException.class, () -> {
            usuarioService.create(dto);
        });

        verify(usuarioRepository, never()).save(usuarioCriado);
    }

    @Test
    void create_withUnknownTipoUsuario_shouldThrowIllegalArgumentException() {
        UsuarioCreateDTO dto = new UsuarioCreateDTO();
        dto.setTipo("UNKNOWN");
        dto.setSenha("password123");

        assertThrows(IllegalArgumentException.class, () -> {
            usuarioService.create(dto);
        });

        verify(usuarioRepository, times(0)).save(any(Usuario.class));
    }

    @Test
    void create_withFactoryException_shouldThrowException() throws Exception {
        UsuarioCreateDTO dto = new UsuarioCreateDTO();
        dto.setTipo("ADMIN");
        dto.setSenha("password123");

        when(adminFactory.createUsuario(dto)).thenThrow(new Exception("Erro na fábrica"));

        Exception exception = assertThrows(Exception.class, () -> {
            usuarioService.create(dto);
        });

        assertEquals("Erro na fábrica", exception.getMessage());
        verify(adminFactory, times(1)).createUsuario(dto);
        verify(usuarioRepository, times(0)).save(any(Usuario.class));
    }

    @Test
    void update_withValidData_shouldUpdateUsuario() throws Exception {
        Long id = 1L;
        UsuarioUpdateDTO dto = new UsuarioUpdateDTO();
        dto.setSenhaAtual("password123");
        dto.setNovaSenha("newPassword456");

        Usuario usuario = new Usuario();
        usuario.setId(id);
        usuario.setSenha(criptografarSenha(dto.getSenhaAtual()));

        when(usuarioRepository.findById(id)).thenReturn(Optional.of(usuario));
        when(usuarioRepository.save(usuario)).thenReturn(usuario);

        usuarioService.update(id, dto);

        assertArrayEquals(criptografarSenha(dto.getNovaSenha()), usuario.getSenha());
        verify(usuarioRepository, times(1)).findById(id);
        verify(usuarioRepository, times(1)).save(usuario);
    }

    @Test
    void update_withUserNotFound_shouldThrowNotFoundException() throws Exception {
        Long id = 1L;
        UsuarioUpdateDTO dto = new UsuarioUpdateDTO();
        dto.setSenhaAtual("password123");
        dto.setNovaSenha("newPassword456");

        when(usuarioRepository.findById(id)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            usuarioService.update(id, dto);
        });

        assertEquals("Usuário não encontrado", exception.getMessage());
        verify(usuarioRepository, times(1)).findById(id);
        verify(usuarioRepository, times(0)).save(any(Usuario.class));
    }

    @Test
    void update_withIncorrectSenhaAtual_shouldThrowUnauthorizedException() throws Exception {
        Long id = 1L;
        UsuarioUpdateDTO dto = new UsuarioUpdateDTO();
        dto.setSenhaAtual("wrongPassword");
        dto.setNovaSenha("newPassword456");

        Usuario usuario = new Usuario();
        usuario.setId(id);
        usuario.setSenha(criptografarSenha("password123"));

        when(usuarioRepository.findById(id)).thenReturn(Optional.of(usuario));

        UnauthorizedException exception = assertThrows(UnauthorizedException.class, () -> {
            usuarioService.update(id, dto);
        });

        assertEquals("A senha antiga está incorreta!", exception.getMessage());
        verify(usuarioRepository, times(1)).findById(id);
        verify(usuarioRepository, times(0)).save(any(Usuario.class));
    }

    @Test
    void delete_withExistentId_shouldDeleteUsuario() throws NotFoundException {
        Long id = 1L;
        Usuario usuario = new Usuario();
        usuario.setId(id);

        when(usuarioRepository.findById(id)).thenReturn(Optional.of(usuario));
        doNothing().when(usuarioRepository).delete(usuario);

        usuarioService.delete(id);

        verify(usuarioRepository, times(1)).findById(id);
        verify(usuarioRepository, times(1)).delete(usuario);
    }

    @Test
    void delete_withNonExistentId_shouldThrowNotFoundException() throws NotFoundException {
        Long id = 1L;

        when(usuarioRepository.findById(id)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            usuarioService.delete(id);
        });

        assertEquals("Usuário não encontrado", exception.getMessage());
        verify(usuarioRepository, times(1)).findById(id);
        verify(usuarioRepository, times(0)).delete(any(Usuario.class));
    }

    @Test
    void getById_withExistentId_shouldReturnUsuario() throws NotFoundException {
        Long id = 1L;
        Usuario usuario = new Usuario();
        usuario.setId(id);

        when(usuarioRepository.findById(id)).thenReturn(Optional.of(usuario));

        Usuario result = usuarioService.getById(id);

        assertNotNull(result);
        assertEquals(id, result.getId());
        verify(usuarioRepository, times(1)).findById(id);
    }

    @Test
    void getById_withNonExistentId_shouldThrowNotFoundException() throws NotFoundException {
        Long id = 1L;

        when(usuarioRepository.findById(id)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            usuarioService.getById(id);
        });

        assertEquals("Usuário não encontrado", exception.getMessage());
        verify(usuarioRepository, times(1)).findById(id);
    }

    private byte[] criptografarSenha(String senha) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        return digest.digest(senha.getBytes());
    }

    @Test
    void create_withAlunoFactory_shouldReturnUsuarioResponseDTO() throws Exception {
        UsuarioCreateDTO dto = new UsuarioCreateDTO();
        dto.setTipo("ALUNO");
        dto.setSenha("studentPass");

        Usuario usuarioCriado = new Usuario();
        usuarioCriado.setId(2L);
        usuarioCriado.setSenha(criptografarSenha(dto.getSenha()));


        when(alunoFactory.createUsuario(dto)).thenReturn(usuarioCriado);
        when(usuarioRepository.save(usuarioCriado)).thenReturn(usuarioCriado);

        UsuarioResponseDTO result = usuarioService.create(dto);

        assertNotNull(result);
        verify(alunoFactory, times(1)).createUsuario(dto);
        verify(usuarioRepository, times(1)).save(usuarioCriado);
    }

    @Test
    void create_withResponsavelFactory_shouldReturnUsuarioResponseDTO() throws Exception {
        UsuarioCreateDTO dto = new UsuarioCreateDTO();
        dto.setTipo("RESPONSAVEL");
        dto.setSenha("responsavelPass");

        Usuario usuarioCriado = new Usuario();
        usuarioCriado.setId(3L);
        usuarioCriado.setSenha(criptografarSenha(dto.getSenha()));

        when(responsavelFactory.createUsuario(dto)).thenReturn(usuarioCriado);
        when(usuarioRepository.save(usuarioCriado)).thenReturn(usuarioCriado);

        UsuarioResponseDTO result = usuarioService.create(dto);

        assertNotNull(result);
        verify(responsavelFactory, times(1)).createUsuario(dto);
        verify(usuarioRepository, times(1)).save(usuarioCriado);
    }

    @Test
    void create_withAdminEscolaFactory_shouldReturnUsuarioResponseDTO() throws Exception {
        UsuarioCreateDTO dto = new UsuarioCreateDTO();
        dto.setTipo("ADMIN_ESCOLA");
        dto.setSenha("adminEscolaPass");

        Usuario usuarioCriado = new Usuario();
        usuarioCriado.setId(4L);
        usuarioCriado.setSenha(criptografarSenha(dto.getSenha()));

        when(adminEscolaFactory.createUsuario(dto)).thenReturn(usuarioCriado);
        when(usuarioRepository.save(usuarioCriado)).thenReturn(usuarioCriado);

        UsuarioResponseDTO result = usuarioService.create(dto);

        assertNotNull(result);
        verify(adminEscolaFactory, times(1)).createUsuario(dto);
        verify(usuarioRepository, times(1)).save(usuarioCriado);
    }

    @Test
    void create_withFactoryUnknownTipoUsuario_shouldThrowIllegalArgumentException() {
        UsuarioCreateDTO dto = new UsuarioCreateDTO();
        dto.setTipo("UNKNOWN_TYPE");
        dto.setSenha("password123");

        assertThrows(IllegalArgumentException.class, () -> {
            usuarioService.create(dto);
        });

        verify(usuarioRepository, times(0)).save(any(Usuario.class));
    }
}
