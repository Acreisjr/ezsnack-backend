package com.tispucminas.sistemaezsnack.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.tispucminas.sistemaezsnack.dto.ContaResponseDTO;
import com.tispucminas.sistemaezsnack.dto.ContaUpdateResponsavelDTO;
import com.tispucminas.sistemaezsnack.dto.ContaUpdateSaldoDTO;
import com.tispucminas.sistemaezsnack.exception.IllegalOperationException;
import com.tispucminas.sistemaezsnack.exception.NotFoundException;
import com.tispucminas.sistemaezsnack.model.Aluno;
import com.tispucminas.sistemaezsnack.model.Conta;
import com.tispucminas.sistemaezsnack.model.Responsavel;
import com.tispucminas.sistemaezsnack.repository.ContaRepository;

@ExtendWith(MockitoExtension.class)
public class ContaServiceTest {

    @InjectMocks
    private ContaService contaService;

    @Mock
    private UsuarioService usuarioService;

    @Mock
    private ContaRepository contaRepository;

    @Test
    void getById_withExistingId_shouldReturnConta() throws NotFoundException {
        Long contaId = 1L;
        Conta conta = mock(Conta.class);
        when(contaRepository.findById(contaId)).thenReturn(Optional.of(conta));

        contaService.getById(contaId);

        verify(contaRepository, times(1)).findById(contaId);
    }

    @Test
    void getById_withNonExistingId_shouldThrowNotFoundException() {
        Long contaId = 1L;
        when(contaRepository.findById(contaId)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            contaService.getById(contaId);
        });

        assertEquals("Conta não encontrada!", exception.getMessage());
        verify(contaRepository, times(1)).findById(contaId);
    }

    @Test
    void findAll_shouldReturnListOfContas() {
        Conta conta1 = mock(Conta.class);
        Conta conta2 = mock(Conta.class);

        when(contaRepository.findAll()).thenReturn(List.of(conta1, conta2));

        List<ContaResponseDTO> result = contaService.findAll();

        assertEquals(2, result.size());
        verify(contaRepository, times(1)).findAll();
    }

    @Test
    void create_withAluno_shouldReturnSavedConta() {
        Aluno aluno = mock(Aluno.class);
        Conta conta = mock(Conta.class);
        when(contaRepository.save(any(Conta.class))).thenReturn(conta);

        Conta result = contaService.create(aluno);

        assertEquals(conta, result);
        verify(contaRepository, times(1)).save(any(Conta.class));
    }

    @Test
    void updateResponsavel_withExistingConta_shouldReturnUpdatedConta() throws Exception {
        Long contaId = 1L;
        ContaUpdateResponsavelDTO dto = mock(ContaUpdateResponsavelDTO.class);
        when(dto.getResponsavelId()).thenReturn(2L);
        Responsavel responsavel = mock(Responsavel.class);
        Conta conta = mock(Conta.class);

        when(contaRepository.findById(contaId)).thenReturn(Optional.of(conta));
        when(usuarioService.getById(2L)).thenReturn(responsavel);
        when(contaRepository.save(conta)).thenReturn(conta);

        Conta result = contaService.updateResponsavel(contaId, dto);

        assertEquals(conta, result);
        verify(contaRepository, times(1)).findById(contaId);
        verify(usuarioService, times(1)).getById(2L);
        verify(conta, times(1)).setResponsavel(responsavel);
        verify(contaRepository, times(1)).save(conta);
    }

    @Test
    void updateResponsavel_withNonExistingConta_shouldThrowException() throws Exception {
        Long contaId = 1L;
        ContaUpdateResponsavelDTO dto = mock(ContaUpdateResponsavelDTO.class);

        when(contaRepository.findById(contaId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(Exception.class, () -> {
            contaService.updateResponsavel(contaId, dto);
        });

        assertEquals("Conta não encontrada!", exception.getMessage());
        verify(contaRepository, times(1)).findById(contaId);
        verify(usuarioService, never()).getById(anyLong());
        verify(contaRepository, never()).save(any(Conta.class));
    }

    @Test
    void aumentarSaldo_withExistingConta_shouldReturnUpdatedConta() throws Exception {
        Long contaId = 1L;
        ContaUpdateSaldoDTO dto = mock(ContaUpdateSaldoDTO.class);
        BigDecimal aumento = BigDecimal.valueOf(100.0);
        when(dto.getSaldo()).thenReturn(aumento);
        Conta conta = mock(Conta.class);

        when(contaRepository.findById(contaId)).thenReturn(Optional.of(conta));
        when(contaRepository.save(conta)).thenReturn(conta);

        Conta result = contaService.aumentarSaldo(contaId, dto);

        assertEquals(conta, result);
        verify(contaRepository, times(1)).findById(contaId);
        verify(conta, times(1)).aumentarSaldo(aumento);
        verify(contaRepository, times(1)).save(conta);
    }

    @Test
    void aumentarSaldo_withNonExistingConta_shouldThrowException() throws Exception {
        Long contaId = 1L;
        ContaUpdateSaldoDTO dto = mock(ContaUpdateSaldoDTO.class);

        when(contaRepository.findById(contaId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(Exception.class, () -> {
            contaService.aumentarSaldo(contaId, dto);
        });

        assertEquals("Conta não encontrada!", exception.getMessage());
        verify(contaRepository, times(1)).findById(contaId);
        verify(contaRepository, never()).save(any(Conta.class));
    }

    @Test
    void diminuirSaldo_withExistingConta_shouldReturnUpdatedConta() throws Exception {
        Long contaId = 1L;
        ContaUpdateSaldoDTO dto = mock(ContaUpdateSaldoDTO.class);
        BigDecimal diminuicao = BigDecimal.valueOf(50.0);
        when(dto.getSaldo()).thenReturn(diminuicao);
        Conta conta = mock(Conta.class);

        when(contaRepository.findById(contaId)).thenReturn(Optional.of(conta));
        when(contaRepository.save(conta)).thenReturn(conta);

        Conta result = contaService.diminuirSaldo(contaId, dto);

        assertEquals(conta, result);
        verify(contaRepository, times(1)).findById(contaId);
        verify(conta, times(1)).diminuirSaldo(diminuicao);
        verify(contaRepository, times(1)).save(conta);
    }

    @Test
    void diminuirSaldo_withNonExistingConta_shouldThrowException() throws Exception {
        Long contaId = 1L;
        ContaUpdateSaldoDTO dto = mock(ContaUpdateSaldoDTO.class);

        when(contaRepository.findById(contaId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(Exception.class, () -> {
            contaService.diminuirSaldo(contaId, dto);
        });

        assertEquals("Conta não encontrada!", exception.getMessage());
        verify(contaRepository, times(1)).findById(contaId);
        verify(contaRepository, never()).save(any(Conta.class));
    }

    @Test
    void debitaValorTotal_withSufficientBalance_shouldDebitaValor() throws Exception {
        Long contaId = 1L;
        BigDecimal valorTotal = BigDecimal.valueOf(50.0);
        Conta conta = mock(Conta.class);

        when(contaRepository.findById(contaId)).thenReturn(Optional.of(conta));
        when(conta.getSaldo()).thenReturn(BigDecimal.valueOf(100.0));

        contaService.debitaValorTotal(contaId, valorTotal);

        verify(contaRepository, times(1)).findById(contaId);
        verify(conta, times(1)).setSaldo(BigDecimal.valueOf(50.0));
        verify(contaRepository, times(1)).save(conta);
    }

    @Test
    void debitaValorTotal_withInsufficientBalance_shouldThrowIllegalOperationException() throws Exception {
        Long contaId = 1L;
        BigDecimal valorTotal = BigDecimal.valueOf(150.0);
        Conta conta = mock(Conta.class);

        when(contaRepository.findById(contaId)).thenReturn(Optional.of(conta));
        when(conta.getSaldo()).thenReturn(BigDecimal.valueOf(100.0));

        IllegalOperationException exception = assertThrows(IllegalOperationException.class, () -> {
            contaService.debitaValorTotal(contaId, valorTotal);
        });

        assertEquals("Saldo insuficiente !", exception.getMessage());
        verify(contaRepository, times(1)).findById(contaId);
        verify(conta, times(1)).getSaldo();
        verify(conta, never()).setSaldo(any(BigDecimal.class));
        verify(contaRepository, never()).save(any(Conta.class));
    }

    @Test
    void debitaValorTotal_withNonExistingConta_shouldThrowNotFoundException() throws Exception {
        Long contaId = 1L;
        BigDecimal valorTotal = BigDecimal.valueOf(50.0);

        when(contaRepository.findById(contaId)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            contaService.debitaValorTotal(contaId, valorTotal);
        });

        assertEquals("Conta não encontrada!", exception.getMessage());
        verify(contaRepository, times(1)).findById(contaId);
        verify(contaRepository, never()).save(any(Conta.class));
    }

    @Test
    void verificaSaldoSuficiente_withSufficientBalance_shouldReturnConta() throws Exception {
        Long contaId = 1L;
        BigDecimal valorTotal = BigDecimal.valueOf(50.0);
        Conta conta = mock(Conta.class);

        when(contaRepository.findById(contaId)).thenReturn(Optional.of(conta));
        when(conta.getSaldo()).thenReturn(BigDecimal.valueOf(100.0));

        Conta result = contaService.verificaSaldoSuficiente(contaId, valorTotal);

        assertEquals(conta, result);
        verify(contaRepository, times(1)).findById(contaId);
        verify(conta, times(1)).getSaldo();
    }

    @Test
    void verificaSaldoSuficiente_withInsufficientBalance_shouldThrowIllegalOperationException() throws Exception {
        Long contaId = 1L;
        BigDecimal valorTotal = BigDecimal.valueOf(150.0);
        Conta conta = mock(Conta.class);

        when(contaRepository.findById(contaId)).thenReturn(Optional.of(conta));
        when(conta.getSaldo()).thenReturn(BigDecimal.valueOf(100.0));

        IllegalOperationException exception = assertThrows(IllegalOperationException.class, () -> {
            contaService.verificaSaldoSuficiente(contaId, valorTotal);
        });

        assertEquals("Saldo insuficiente !", exception.getMessage());
        verify(contaRepository, times(1)).findById(contaId);
        verify(conta, times(1)).getSaldo();
    }

    @Test
    void verificaSaldoSuficiente_withNonExistingConta_shouldThrowNotFoundException() throws Exception {
        Long contaId = 1L;
        BigDecimal valorTotal = BigDecimal.valueOf(50.0);

        when(contaRepository.findById(contaId)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            contaService.verificaSaldoSuficiente(contaId, valorTotal);
        });

        assertEquals("Conta não encontrada!", exception.getMessage());
        verify(contaRepository, times(1)).findById(contaId);
    }
}
