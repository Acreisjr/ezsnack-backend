package com.tispucminas.sistemaezsnack.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.tispucminas.sistemaezsnack.dto.EscolaCreateDTO;
import com.tispucminas.sistemaezsnack.dto.EscolaUpdateDTO;
import com.tispucminas.sistemaezsnack.exception.NotFoundException;
import com.tispucminas.sistemaezsnack.model.Escola;
import com.tispucminas.sistemaezsnack.service.EscolaService;

@ExtendWith(MockitoExtension.class)
public class EscolaControllerTest {

    @InjectMocks
    private EscolaController escolaController;

    @Mock
    private EscolaService escolaService;

    @Test
    void findAll_shouldReturnList() {
        Escola escola = mock(Escola.class);

        when(escolaService.findAll()).thenReturn(List.of(escola));

        ResponseEntity<List<Escola>> response = escolaController.findAll();

        assertEquals(HttpStatus.OK, response.getStatusCode());

        List<Escola> body = response.getBody();

        assertNotNull(body);
        assertFalse(body.isEmpty());
        assertTrue(body.get(0) instanceof Escola);
    }

    @Test
    void findByID_withExistentId_shouldReturn() throws NotFoundException {
        Escola escola = mock(Escola.class);

        when(escolaService.getEscolaById(anyLong())).thenReturn(escola);

        ResponseEntity<Escola> response = escolaController.findByID(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        Escola body = response.getBody();

        assertNotNull(body);
        assertTrue(body instanceof Escola);
    }

    @Test
    void findByID_withNotExistentId_shouldReturnNotFound() throws NotFoundException {
        when(escolaService.getEscolaById(anyLong())).thenThrow(NotFoundException.class);

        ResponseEntity<Escola> response = escolaController.findByID(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void create_withValidData_shouldReturnCreated() throws NotFoundException {
        EscolaCreateDTO dto = mock(EscolaCreateDTO.class);
        Escola escola = mock(Escola.class);

        when(escolaService.create(any(EscolaCreateDTO.class))).thenReturn(escola);

        ResponseEntity<Escola> response = escolaController.create(dto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        Escola body = response.getBody();

        assertNotNull(body);
        assertTrue(body instanceof Escola);
    }

    @Test
    void update_withExistentId_shouldReturn() throws Exception {
        EscolaUpdateDTO dto = mock(EscolaUpdateDTO.class);
        Escola escolaUpdated = mock(Escola.class);

        when(escolaService.update(anyLong(), any(EscolaUpdateDTO.class))).thenReturn(escolaUpdated);

        ResponseEntity<Escola> response = escolaController.update(1L, dto);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        Escola body = response.getBody();

        assertNotNull(body);
        assertTrue(body instanceof Escola);
    }

    @Test
    void update_withNotExistentId_shouldReturnNotFound() throws Exception {
        EscolaUpdateDTO dto = mock(EscolaUpdateDTO.class);

        when(escolaService.update(anyLong(), any(EscolaUpdateDTO.class))).thenThrow(NotFoundException.class);

        ResponseEntity<Escola> response = escolaController.update(1L, dto);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void delete_withExistentId_shouldSucceed() throws Exception {
        doNothing().when(escolaService).delete(anyLong());

        assertDoesNotThrow(() -> escolaController.delete(1L));

        verify(escolaService, times(1)).delete(1L);
    }

    @Test
    void delete_withNotExistentId_shouldHandleException() throws Exception {
        doThrow(NotFoundException.class).when(escolaService).delete(anyLong());

        assertDoesNotThrow(() -> escolaController.delete(1L));

        verify(escolaService, times(1)).delete(1L);
    }
}
