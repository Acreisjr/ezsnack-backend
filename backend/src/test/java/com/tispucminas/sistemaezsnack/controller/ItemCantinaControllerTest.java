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

import com.tispucminas.sistemaezsnack.dto.ItemCantinaCreateDTO;
import com.tispucminas.sistemaezsnack.dto.ItemCantinaUpdateDTO;
import com.tispucminas.sistemaezsnack.exception.NotFoundException;
import com.tispucminas.sistemaezsnack.model.ItemEstoqueCantina;
import com.tispucminas.sistemaezsnack.service.ItemCantinaService;

@ExtendWith(MockitoExtension.class)
public class ItemCantinaControllerTest {

    @InjectMocks
    private ItemCantinaController itemCantinaController;

    @Mock
    private ItemCantinaService itemCantinaService;

    @Test
    void findAll_shouldReturnList_withoutEscolaId() {
        ItemEstoqueCantina item = mock(ItemEstoqueCantina.class);

        when(itemCantinaService.findAll(null)).thenReturn(List.of(item));

        ResponseEntity<List<ItemEstoqueCantina>> response = itemCantinaController.findAll(null);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        List<ItemEstoqueCantina> body = response.getBody();

        assertNotNull(body);
        assertFalse(body.isEmpty());
        assertTrue(body.get(0) instanceof ItemEstoqueCantina);
    }

    @Test
    void findAll_shouldReturnList_withEscolaId() {
        Long escolaId = 1L;
        ItemEstoqueCantina item = mock(ItemEstoqueCantina.class);

        when(itemCantinaService.findAll(escolaId)).thenReturn(List.of(item));

        ResponseEntity<List<ItemEstoqueCantina>> response = itemCantinaController.findAll(escolaId);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        List<ItemEstoqueCantina> body = response.getBody();

        assertNotNull(body);
        assertFalse(body.isEmpty());
        assertTrue(body.get(0) instanceof ItemEstoqueCantina);
    }

    @Test
    void findById_withExistentId_shouldReturn() throws NotFoundException {
        ItemEstoqueCantina item = mock(ItemEstoqueCantina.class);

        when(itemCantinaService.findById(anyLong())).thenReturn(item);

        ResponseEntity<ItemEstoqueCantina> response = itemCantinaController.findById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        ItemEstoqueCantina body = response.getBody();

        assertNotNull(body);
        assertTrue(body instanceof ItemEstoqueCantina);
    }
    
    @Test
    void create_withValidData_shouldReturnCreated() {
        ItemCantinaCreateDTO dto = mock(ItemCantinaCreateDTO.class);
        ItemEstoqueCantina item = mock(ItemEstoqueCantina.class);

        when(itemCantinaService.create(any(ItemCantinaCreateDTO.class))).thenReturn(item);

        ResponseEntity<ItemEstoqueCantina> response = itemCantinaController.create(dto);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        ItemEstoqueCantina body = response.getBody();

        assertNotNull(body);
        assertTrue(body instanceof ItemEstoqueCantina);
    }

    @Test
    void update_withExistentId_shouldReturn() throws NotFoundException {
        ItemCantinaUpdateDTO dto = mock(ItemCantinaUpdateDTO.class);
        ItemEstoqueCantina itemUpdated = mock(ItemEstoqueCantina.class);

        when(itemCantinaService.update(anyLong(), any(ItemCantinaUpdateDTO.class))).thenReturn(itemUpdated);

        ResponseEntity<ItemEstoqueCantina> response = itemCantinaController.update(1L, dto);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        ItemEstoqueCantina body = response.getBody();

        assertNotNull(body);
        assertTrue(body instanceof ItemEstoqueCantina);
    }

    @Test
    void delete_withExistentId_shouldReturnNoContent() throws NotFoundException {
        doNothing().when(itemCantinaService).delete(anyLong());

        ResponseEntity<Void> response = itemCantinaController.delete(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());

        verify(itemCantinaService, times(1)).delete(1L);
    }
}

