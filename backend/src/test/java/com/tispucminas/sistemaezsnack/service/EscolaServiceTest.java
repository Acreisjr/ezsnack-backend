package com.tispucminas.sistemaezsnack.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.tispucminas.sistemaezsnack.dto.EscolaCreateDTO;
import com.tispucminas.sistemaezsnack.dto.EscolaUpdateDTO;
import com.tispucminas.sistemaezsnack.exception.NotFoundException;
import com.tispucminas.sistemaezsnack.model.Escola;
import com.tispucminas.sistemaezsnack.repository.EscolaRepository;

@ExtendWith(MockitoExtension.class)
public class EscolaServiceTest {

    @InjectMocks
    private EscolaService escolaService;

    @Mock
    private EscolaRepository escolaRepository;

    @Test
    void findAll_shouldReturnList() {
        Escola escola1 = new Escola();
        Escola escola2 = new Escola();
        List<Escola> escolas = Arrays.asList(escola1, escola2);

        when(escolaRepository.findAll()).thenReturn(escolas);

        List<Escola> result = escolaService.findAll();

        assertEquals(2, result.size());
        verify(escolaRepository, times(1)).findAll();
    }

    @Test
    void getEscolaById_withExistentId_shouldReturnEscola() throws NotFoundException {
        Long id = 1L;
        Escola escola = new Escola();
        when(escolaRepository.findById(id)).thenReturn(Optional.of(escola));

        Escola result = escolaService.getEscolaById(id);

        assertNotNull(result);
        verify(escolaRepository, times(1)).findById(id);
    }

    @Test
    void getEscolaById_withNonExistentId_shouldThrowNotFoundException() {
        Long id = 1L;
        when(escolaRepository.findById(id)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            escolaService.getEscolaById(id);
        });

        assertEquals("Escola não encontrada", exception.getMessage());
        verify(escolaRepository, times(1)).findById(id);
    }

    @Test
    void create_shouldSaveAndReturnEscola() {
        EscolaCreateDTO dto = new EscolaCreateDTO();
        dto.setNome("Escola A");
        dto.setCnpj("00.000.000/0001-00");
        dto.setTelefone("123456789");
        dto.setEstado("MG");
        dto.setCidade("Belo Horizonte");

        Escola escola = new Escola();
        escola.setNome(dto.getNome());
        escola.setCnpj(dto.getCnpj());
        escola.setTelefone(dto.getTelefone());
        escola.setEstado(dto.getEstado());
        escola.setCidade(dto.getCidade());

        when(escolaRepository.save(any(Escola.class))).thenReturn(escola);

        Escola result = escolaService.create(dto);

        assertNotNull(result);
        assertEquals("Escola A", result.getNome());
        verify(escolaRepository, times(1)).save(any(Escola.class));
    }

    @Test
    void update_withExistentId_shouldUpdateAndReturnEscola() throws Exception {
        Long id = 1L;
        EscolaUpdateDTO dto = new EscolaUpdateDTO();
        dto.setTelefone("987654321");

        Escola escola = new Escola();
        escola.setId(id);
        escola.setTelefone("123456789");

        when(escolaRepository.findById(id)).thenReturn(Optional.of(escola));
        when(escolaRepository.save(escola)).thenReturn(escola);

        Escola result = escolaService.update(id, dto);

        assertNotNull(result);
        assertEquals("987654321", result.getTelefone());
        verify(escolaRepository, times(1)).findById(id);
        verify(escolaRepository, times(1)).save(escola);
    }

    @Test
    void update_withNonExistentId_shouldThrowException() {
        Long id = 1L;
        EscolaUpdateDTO dto = new EscolaUpdateDTO();
        dto.setTelefone("987654321");

        when(escolaRepository.findById(id)).thenReturn(Optional.empty());

        Exception exception = assertThrows(Exception.class, () -> {
            escolaService.update(id, dto);
        });

        assertEquals("Escola não encontrada", exception.getMessage());
        verify(escolaRepository, times(1)).findById(id);
        verify(escolaRepository, times(0)).save(any(Escola.class));
    }

    @Test
    void delete_withExistentId_shouldDeleteEscola() throws Exception {
        Long id = 1L;
        Escola escola = new Escola();
        escola.setId(id);

        when(escolaRepository.findById(id)).thenReturn(Optional.of(escola));
        doNothing().when(escolaRepository).delete(escola);

        escolaService.delete(id);

        verify(escolaRepository, times(1)).findById(id);
        verify(escolaRepository, times(1)).delete(escola);
    }

    @Test
    void delete_withNonExistentId_shouldThrowException() {
        Long id = 1L;

        when(escolaRepository.findById(id)).thenReturn(Optional.empty());

        Exception exception = assertThrows(Exception.class, () -> {
            escolaService.delete(id);
        });

        assertEquals("Escola não encontrada", exception.getMessage());
        verify(escolaRepository, times(1)).findById(id);
        verify(escolaRepository, times(0)).delete(any(Escola.class));
    }
}
