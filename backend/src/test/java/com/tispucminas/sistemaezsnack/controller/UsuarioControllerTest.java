package com.tispucminas.sistemaezsnack.controller;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.security.NoSuchAlgorithmException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.tispucminas.sistemaezsnack.dto.ErrorResponseDTO;
import com.tispucminas.sistemaezsnack.dto.LoginDTO;
import com.tispucminas.sistemaezsnack.dto.UsuarioCreateDTO;
import com.tispucminas.sistemaezsnack.dto.UsuarioResponseDTO;
import com.tispucminas.sistemaezsnack.dto.UsuarioUpdateDTO;
import com.tispucminas.sistemaezsnack.exception.NotFoundException;
import com.tispucminas.sistemaezsnack.exception.UnauthorizedException;
import com.tispucminas.sistemaezsnack.service.UsuarioService;

@ExtendWith(MockitoExtension.class)
public class UsuarioControllerTest {

    @InjectMocks
    private UsuarioController usuarioController;

    @Mock
    private UsuarioService usuarioService;

    @Test
    void login_withValidCredentials_shouldReturnOk() throws Exception {
        LoginDTO loginDTO = mock(LoginDTO.class);
        when(loginDTO.getUser()).thenReturn("usuario");
        when(loginDTO.getSenha()).thenReturn("senha123");

        UsuarioResponseDTO usuarioResponse = mock(UsuarioResponseDTO.class);

        when(usuarioService.login("usuario", "senha123")).thenReturn(usuarioResponse);

        ResponseEntity<Object> response = usuarioController.login(loginDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(usuarioResponse, response.getBody());
        verify(usuarioService, times(1)).login("usuario", "senha123");
    }

    @Test
    void login_withInvalidCredentials_shouldReturnUnauthorized() throws Exception {
        LoginDTO loginDTO = mock(LoginDTO.class);
        when(loginDTO.getUser()).thenReturn("usuario");
        when(loginDTO.getSenha()).thenReturn("senhaErrada");

        when(usuarioService.login("usuario", "senhaErrada")).thenReturn(null);

        ResponseEntity<Object> response = usuarioController.login(loginDTO);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertNull(response.getBody());
        verify(usuarioService, times(1)).login("usuario", "senhaErrada");
    }

    @Test
    void login_withException_shouldReturnInternalServerError() throws Exception {
        LoginDTO loginDTO = mock(LoginDTO.class);
        when(loginDTO.getUser()).thenReturn("usuario");
        when(loginDTO.getSenha()).thenReturn("senha123");

        when(usuarioService.login("usuario", "senha123")).thenThrow(new NoSuchAlgorithmException("Erro interno do servidor!"));

        ResponseEntity<Object> response = usuarioController.login(loginDTO);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof ErrorResponseDTO);
        assertEquals("Erro interno do servidor!", ((ErrorResponseDTO) response.getBody()).getMessage());
        verify(usuarioService, times(1)).login("usuario", "senha123");
    }

    @Test
    void create_withValidData_shouldReturnOk() throws Exception {
        UsuarioCreateDTO usuarioCreateDTO = mock(UsuarioCreateDTO.class);
        UsuarioResponseDTO usuarioResponse = mock(UsuarioResponseDTO.class);

        when(usuarioService.create(usuarioCreateDTO)).thenReturn(usuarioResponse);

        ResponseEntity<Object> response = usuarioController.create(usuarioCreateDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(usuarioResponse, response.getBody());
        verify(usuarioService, times(1)).create(usuarioCreateDTO);
    }
    
    @Test
    void create_withNotFoundException_shouldReturnNotFound() throws Exception {
        UsuarioCreateDTO usuarioCreateDTO = mock(UsuarioCreateDTO.class);
        String errorMessage = "Recurso não encontrado";

        when(usuarioService.create(usuarioCreateDTO)).thenThrow(new NotFoundException(errorMessage));

        ResponseEntity<Object> response = usuarioController.create(usuarioCreateDTO);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof ErrorResponseDTO);
        assertEquals(errorMessage, ((ErrorResponseDTO) response.getBody()).getMessage());
        verify(usuarioService, times(1)).create(usuarioCreateDTO);
    }

    @Test
    void create_withDataIntegrityViolationException_shouldReturnBadRequest() throws Exception {
        UsuarioCreateDTO usuarioCreateDTO = mock(UsuarioCreateDTO.class);

        when(usuarioService.create(usuarioCreateDTO)).thenThrow(new DataIntegrityViolationException("Constraint violation"));

        ResponseEntity<Object> response = usuarioController.create(usuarioCreateDTO);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof ErrorResponseDTO);
        assertEquals("Nome de usuário indisponível!", ((ErrorResponseDTO) response.getBody()).getMessage());
        verify(usuarioService, times(1)).create(usuarioCreateDTO);
    }

    @Test
    void create_withGenericException_shouldReturnInternalServerError() throws Exception {
        UsuarioCreateDTO usuarioCreateDTO = mock(UsuarioCreateDTO.class);

        when(usuarioService.create(usuarioCreateDTO)).thenThrow(new Exception("Erro genérico"));

        ResponseEntity<Object> response = usuarioController.create(usuarioCreateDTO);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof ErrorResponseDTO);
        assertEquals("Erro genérico", ((ErrorResponseDTO) response.getBody()).getMessage());
        verify(usuarioService, times(1)).create(usuarioCreateDTO);
    }

    @Test
    void update_withValidData_shouldReturnOk() throws Exception {
        Long usuarioId = 1L;
        UsuarioUpdateDTO usuarioUpdateDTO = mock(UsuarioUpdateDTO.class);

        doNothing().when(usuarioService).update(usuarioId, usuarioUpdateDTO);

        ResponseEntity<Object> response = usuarioController.update(usuarioId, usuarioUpdateDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNull(response.getBody());
        verify(usuarioService, times(1)).update(usuarioId, usuarioUpdateDTO);
    }

    @Test
    void update_withNotFoundException_shouldReturnNotFound() throws Exception {
        Long usuarioId = 1L;
        UsuarioUpdateDTO usuarioUpdateDTO = mock(UsuarioUpdateDTO.class);
        String errorMessage = "Usuário não encontrado";

        doThrow(new NotFoundException(errorMessage)).when(usuarioService).update(usuarioId, usuarioUpdateDTO);

        ResponseEntity<Object> response = usuarioController.update(usuarioId, usuarioUpdateDTO);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof ErrorResponseDTO);
        assertEquals(errorMessage, ((ErrorResponseDTO) response.getBody()).getMessage());
        verify(usuarioService, times(1)).update(usuarioId, usuarioUpdateDTO);
    }

    @Test
    void update_withUnauthorizedException_shouldReturnUnauthorized() throws Exception {
        Long usuarioId = 1L;
        UsuarioUpdateDTO usuarioUpdateDTO = mock(UsuarioUpdateDTO.class);
        String errorMessage = "Usuário não autorizado";

        doThrow(new UnauthorizedException(errorMessage)).when(usuarioService).update(usuarioId, usuarioUpdateDTO);

        ResponseEntity<Object> response = usuarioController.update(usuarioId, usuarioUpdateDTO);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof ErrorResponseDTO);
        assertEquals(errorMessage, ((ErrorResponseDTO) response.getBody()).getMessage());
        verify(usuarioService, times(1)).update(usuarioId, usuarioUpdateDTO);
    }

    @Test
    void update_withGenericException_shouldReturnInternalServerError() throws Exception {
        Long usuarioId = 1L;
        UsuarioUpdateDTO usuarioUpdateDTO = mock(UsuarioUpdateDTO.class);

        doThrow(new NoSuchAlgorithmException("Erro interno do servidor!")).when(usuarioService).update(usuarioId, usuarioUpdateDTO);

        ResponseEntity<Object> response = usuarioController.update(usuarioId, usuarioUpdateDTO);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof ErrorResponseDTO);
        assertEquals("Erro interno do servidor!", ((ErrorResponseDTO) response.getBody()).getMessage());
        verify(usuarioService, times(1)).update(usuarioId, usuarioUpdateDTO);
    }

    @Test
    void delete_withValidId_shouldReturnNoContent() throws Exception {
        Long usuarioId = 1L;

        doNothing().when(usuarioService).delete(usuarioId);

        ResponseEntity<Object> response = usuarioController.delete(usuarioId);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
        verify(usuarioService, times(1)).delete(usuarioId);
    }

    @Test
    void delete_withNotFoundException_shouldReturnNotFound() throws Exception {
        Long usuarioId = 1L;
        String errorMessage = "Usuário não encontrado";

        doThrow(new NotFoundException(errorMessage)).when(usuarioService).delete(usuarioId);

        ResponseEntity<Object> response = usuarioController.delete(usuarioId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof ErrorResponseDTO);
        assertEquals(errorMessage, ((ErrorResponseDTO) response.getBody()).getMessage());
        verify(usuarioService, times(1)).delete(usuarioId);
    }

    @Test
    void delete_withGenericException_shouldReturnInternalServerError() throws Exception {
        Long usuarioId = 1L;

        doThrow(new NotFoundException("Usuário não encontrado")).when(usuarioService).delete(usuarioId);

        ResponseEntity<Object> response = usuarioController.delete(usuarioId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof ErrorResponseDTO);
        assertEquals("Usuário não encontrado", ((ErrorResponseDTO) response.getBody()).getMessage());
        verify(usuarioService, times(1)).delete(usuarioId);
    }
}
