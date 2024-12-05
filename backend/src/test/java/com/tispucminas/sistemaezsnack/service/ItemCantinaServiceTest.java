package com.tispucminas.sistemaezsnack.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.tispucminas.sistemaezsnack.dto.ItemCantinaCreateDTO;
import com.tispucminas.sistemaezsnack.dto.ItemCantinaUpdateDTO;
import com.tispucminas.sistemaezsnack.model.Escola;
import com.tispucminas.sistemaezsnack.model.ItemEstoqueCantina;
import com.tispucminas.sistemaezsnack.repository.EscolaRepository;
import com.tispucminas.sistemaezsnack.repository.ItemCantinaRepository;

@ExtendWith(MockitoExtension.class)
public class ItemCantinaServiceTest {

    @InjectMocks
    private ItemCantinaService itemCantinaService;

    @Mock
    private ItemCantinaRepository itemCantinaRepository;

    @Mock
    private EscolaRepository escolaRepository;

    @Test
    void findAll_withEscolaId_shouldReturnFilteredList() {
        Long escolaId = 1L;
        ItemEstoqueCantina item1 = new ItemEstoqueCantina();
        ItemEstoqueCantina item2 = new ItemEstoqueCantina();
        List<ItemEstoqueCantina> itens = Arrays.asList(item1, item2);

        when(itemCantinaRepository.findAllByEscolaId(escolaId)).thenReturn(itens);

        List<ItemEstoqueCantina> result = itemCantinaService.findAll(escolaId);

        assertEquals(2, result.size());
        verify(itemCantinaRepository, times(1)).findAllByEscolaId(escolaId);
    }

    @Test
    void findAll_withoutEscolaId_shouldReturnAllList() {
        ItemEstoqueCantina item1 = new ItemEstoqueCantina();
        ItemEstoqueCantina item2 = new ItemEstoqueCantina();
        List<ItemEstoqueCantina> itens = Arrays.asList(item1, item2);

        when(itemCantinaRepository.findAll()).thenReturn(itens);

        List<ItemEstoqueCantina> result = itemCantinaService.findAll(null);

        assertEquals(2, result.size());
        verify(itemCantinaRepository, times(1)).findAll();
    }

    @Test
    void findById_withExistentId_shouldReturnItem() {
        Long id = 1L;
        ItemEstoqueCantina item = new ItemEstoqueCantina();

        when(itemCantinaRepository.findById(id)).thenReturn(Optional.of(item));

        ItemEstoqueCantina result = itemCantinaService.findById(id);

        assertNotNull(result);
        verify(itemCantinaRepository, times(1)).findById(id);
    }

    @Test
    void findById_withNonExistentId_shouldReturnNull() {
        Long id = 1L;

        when(itemCantinaRepository.findById(id)).thenReturn(Optional.empty());

        ItemEstoqueCantina result = itemCantinaService.findById(id);

        assertNull(result);
        verify(itemCantinaRepository, times(1)).findById(id);
    }

    @Test
    void create_withValidData_shouldReturnSavedItem() {
        ItemCantinaCreateDTO dto = new ItemCantinaCreateDTO();
        dto.setNome("Sandwich");
        dto.setTipo("Alimentação");
        dto.setDisponibilidade(true);
        dto.setPreco(new BigDecimal("5.50"));
        dto.setEscolaId(1L);

        Escola escola = new Escola();
        escola.setId(1L);

        ItemEstoqueCantina item = new ItemEstoqueCantina();
        item.setId(1L);
        item.setNome(dto.getNome());
        item.setTipo(dto.getTipo());
        item.setDisponivel(dto.getDisponibilidade());
        item.setPreco(dto.getPreco());
        item.setEscola(escola);

        when(escolaRepository.findById(dto.getEscolaId())).thenReturn(Optional.of(escola));
        when(itemCantinaRepository.save(any(ItemEstoqueCantina.class))).thenReturn(item);

        ItemEstoqueCantina result = itemCantinaService.create(dto);

        assertNotNull(result);
        assertEquals("Sandwich", result.getNome());
        verify(escolaRepository, times(1)).findById(dto.getEscolaId());
        verify(itemCantinaRepository, times(1)).save(any(ItemEstoqueCantina.class));
    }

    @Test
    void create_withNonExistentEscola_shouldReturnNull() {
        ItemCantinaCreateDTO dto = new ItemCantinaCreateDTO();
        dto.setNome("Sandwich");
        dto.setTipo("Alimentação");
        dto.setDisponibilidade(true);
        dto.setPreco(new BigDecimal("5.50"));
        dto.setEscolaId(1L);

        when(escolaRepository.findById(dto.getEscolaId())).thenReturn(Optional.empty());

        ItemEstoqueCantina result = itemCantinaService.create(dto);

        assertNull(result);
        verify(escolaRepository, times(1)).findById(dto.getEscolaId());
        verify(itemCantinaRepository, times(0)).save(any(ItemEstoqueCantina.class));
    }

    @Test
    void create_withException_shouldReturnNull() {
        ItemCantinaCreateDTO dto = new ItemCantinaCreateDTO();
        dto.setNome("Sandwich");
        dto.setTipo("Alimentação");
        dto.setDisponibilidade(true);
        dto.setPreco(new BigDecimal("5.50"));
        dto.setEscolaId(1L);

        when(escolaRepository.findById(dto.getEscolaId())).thenThrow(new RuntimeException("Erro"));

        ItemEstoqueCantina result = itemCantinaService.create(dto);

        assertNull(result);
        verify(escolaRepository, times(1)).findById(dto.getEscolaId());
        verify(itemCantinaRepository, times(0)).save(any(ItemEstoqueCantina.class));
    }

    @Test
    void update_withExistentId_shouldReturnUpdatedItem() {
        Long id = 1L;
        ItemCantinaUpdateDTO dto = new ItemCantinaUpdateDTO();
        dto.setNome("Suco");
        dto.setTipo("Bebida");
        dto.setDisponibilidade(false);
        dto.setPreco(new BigDecimal("3.00"));

        ItemEstoqueCantina existingItem = new ItemEstoqueCantina();
        existingItem.setId(id);
        existingItem.setNome("Sandwich");
        existingItem.setTipo("Alimentação");
        existingItem.setDisponivel(true);
        existingItem.setPreco(new BigDecimal("5.50"));

        ItemEstoqueCantina updatedItem = new ItemEstoqueCantina();
        updatedItem.setId(id);
        updatedItem.setNome(dto.getNome());
        updatedItem.setTipo(dto.getTipo());
        updatedItem.setDisponivel(dto.getDisponibilidade());
        updatedItem.setPreco(dto.getPreco());

        when(itemCantinaRepository.findById(id)).thenReturn(Optional.of(existingItem));
        when(itemCantinaRepository.save(existingItem)).thenReturn(updatedItem);

        ItemEstoqueCantina result = itemCantinaService.update(id, dto);

        assertNotNull(result);
        assertEquals("Suco", result.getNome());
        assertEquals("Bebida", result.getTipo());
        assertFalse(result.getDisponivel());
        assertEquals(new BigDecimal("3.00"), result.getPreco());
        verify(itemCantinaRepository, times(1)).findById(id);
        verify(itemCantinaRepository, times(1)).save(existingItem);
    }

    @Test
    void update_withNonExistentId_shouldReturnNull() {
        Long id = 1L;
        ItemCantinaUpdateDTO dto = new ItemCantinaUpdateDTO();
        dto.setNome("Suco");
        dto.setTipo("Bebida");
        dto.setDisponibilidade(false);
        dto.setPreco(new BigDecimal("3.00"));

        when(itemCantinaRepository.findById(id)).thenReturn(Optional.empty());

        ItemEstoqueCantina result = itemCantinaService.update(id, dto);

        assertNull(result);
        verify(itemCantinaRepository, times(1)).findById(id);
        verify(itemCantinaRepository, times(0)).save(any(ItemEstoqueCantina.class));
    }

    @Test
    void update_withException_shouldReturnNull() {
        Long id = 1L;
        ItemCantinaUpdateDTO dto = new ItemCantinaUpdateDTO();
        dto.setNome("Suco");
        dto.setTipo("Bebida");
        dto.setDisponibilidade(false);
        dto.setPreco(new BigDecimal("3.00"));

        when(itemCantinaRepository.findById(id)).thenThrow(new RuntimeException("Erro"));

        ItemEstoqueCantina result = itemCantinaService.update(id, dto);

        assertNull(result);
        verify(itemCantinaRepository, times(1)).findById(id);
        verify(itemCantinaRepository, times(0)).save(any(ItemEstoqueCantina.class));
    }

    @Test
    void delete_withExistentId_shouldDeleteItem() {
        Long id = 1L;
        when(itemCantinaRepository.existsById(id)).thenReturn(true);
        doNothing().when(itemCantinaRepository).deleteById(id);

        itemCantinaService.delete(id);

        verify(itemCantinaRepository, times(1)).existsById(id);
        verify(itemCantinaRepository, times(1)).deleteById(id);
    }

    @Test
    void delete_withNonExistentId_shouldLogWarning() {
        Long id = 1L;
        when(itemCantinaRepository.existsById(id)).thenReturn(false);

        itemCantinaService.delete(id);

        verify(itemCantinaRepository, times(1)).existsById(id);
        verify(itemCantinaRepository, times(0)).deleteById(id);
    }

    @Test
    void delete_withException_shouldHandleException() {
        Long id = 1L;
        when(itemCantinaRepository.existsById(id)).thenThrow(new RuntimeException("Erro"));

        itemCantinaService.delete(id);

        verify(itemCantinaRepository, times(1)).existsById(id);
        verify(itemCantinaRepository, times(0)).deleteById(id);
    }
}
