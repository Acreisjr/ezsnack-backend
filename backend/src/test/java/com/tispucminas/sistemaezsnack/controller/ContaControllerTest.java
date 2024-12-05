package com.tispucminas.sistemaezsnack.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import com.tispucminas.sistemaezsnack.dto.ContaResponseDTO;
import com.tispucminas.sistemaezsnack.dto.ContaUpdateResponsavelDTO;
import com.tispucminas.sistemaezsnack.dto.ContaUpdateSaldoDTO;
import com.tispucminas.sistemaezsnack.exception.NotFoundException;
import com.tispucminas.sistemaezsnack.model.Conta;
import com.tispucminas.sistemaezsnack.service.ContaService;

@ExtendWith(MockitoExtension.class)
public class ContaControllerTest {

    @InjectMocks
    private ContaController contaController;

    @Mock
    private ContaService contaService;

    @Test
    void findAll_shouldReturnList(){

        ContaResponseDTO conta = mock(ContaResponseDTO.class);

        when(contaService.findAll()).thenReturn(List.of(conta));

        var response = contaController.findAll();

        assertEquals(HttpStatus.OK, response.getStatusCode());

        var body = response.getBody();

        assertTrue(body.size() > 0);

        assertTrue(body.get(0) instanceof ContaResponseDTO);
    }

    @Test
    void findByID_withExistentId_shouldReturn() throws NotFoundException {
        ContaResponseDTO conta = mock(ContaResponseDTO.class);

        when(contaService.getById(anyLong())).thenReturn(conta);

        var response = contaController.findByID(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        var body = response.getBody();

        assertTrue(body instanceof ContaResponseDTO);
    }

    @Test
    void findByID_withNotExistentId_shouldThrowNotFound() throws NotFoundException {

        doThrow(NotFoundException.class).when(contaService).getById(anyLong());

        var response = contaController.findByID(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void updateResponsavel_withNotExistentId_shouldThrowNotFound() throws Exception {

        var updateUsuarioDTO = mock(ContaUpdateResponsavelDTO.class);

        doThrow(NotFoundException.class).when(contaService).updateResponsavel(anyLong(), any(ContaUpdateResponsavelDTO.class));

        var response = contaController.updateResponsavel(1L, updateUsuarioDTO);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void updateResponsavel_withExistentId_shouldReturn() throws Exception {

        var updateUsuarioDTO = mock(ContaUpdateResponsavelDTO.class);

        var conta = mock(Conta.class);

        when(contaService.updateResponsavel(anyLong(), any(ContaUpdateResponsavelDTO.class))).thenReturn(conta);

        var response = contaController.updateResponsavel(1L, updateUsuarioDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        var body = response.getBody();
        
        assertTrue(body instanceof Conta);
    }

    @Test
    void aumentarSaldo_withExistentId_shouldReturn() throws Exception {

        var updateSaldoDTO = mock(ContaUpdateSaldoDTO.class);

        var conta = mock(Conta.class);

        when(contaService.aumentarSaldo(anyLong(), any(ContaUpdateSaldoDTO.class))).thenReturn(conta);

        var response = contaController.aumentarSaldo(1L, updateSaldoDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        var body = response.getBody();

        assertTrue(body instanceof Conta);
    }

    @Test
    void aumentarSaldo_withNotExistentId_shouldThrowNotFound() throws Exception {

        var updateSaldoDTO = mock(ContaUpdateSaldoDTO.class);

        doThrow(NotFoundException.class).when(contaService).aumentarSaldo(anyLong(), any(ContaUpdateSaldoDTO.class));

        var response = contaController.aumentarSaldo(1L, updateSaldoDTO);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }


    @Test
    void diminuirSaldo_withNotExistentId_shouldThrowNotFound() throws Exception {

        var updateSaldoDTO = mock(ContaUpdateSaldoDTO.class);

        doThrow(NotFoundException.class).when(contaService).diminuirSaldo(anyLong(), any(ContaUpdateSaldoDTO.class));

        var response = contaController.diminuirSaldo(1L, updateSaldoDTO);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void diminuirSaldo_withExistentId_shouldReturn() throws Exception {

        var updateSaldoDTO = mock(ContaUpdateSaldoDTO.class);

        var conta = mock(Conta.class);

        when(contaService.diminuirSaldo(anyLong(), any(ContaUpdateSaldoDTO.class))).thenReturn(conta);

        var response = contaController.diminuirSaldo(1L, updateSaldoDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        var body = response.getBody();

        assertTrue(body instanceof Conta);
    }
}

