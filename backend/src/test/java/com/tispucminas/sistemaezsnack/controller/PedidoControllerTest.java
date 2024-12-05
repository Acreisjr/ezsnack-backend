package com.tispucminas.sistemaezsnack.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.tispucminas.sistemaezsnack.dto.CreatePedidoDTO;
import com.tispucminas.sistemaezsnack.dto.ErrorResponseDTO;
import com.tispucminas.sistemaezsnack.dto.ItemPedidoDTO;
import com.tispucminas.sistemaezsnack.dto.PedidoResponseDTO;
import com.tispucminas.sistemaezsnack.exception.NotFoundException;
import com.tispucminas.sistemaezsnack.model.Pedido;
import com.tispucminas.sistemaezsnack.service.PedidoProducer;
import com.tispucminas.sistemaezsnack.service.PedidoService;

import java.util.Arrays;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class PedidoControllerTest {

    @InjectMocks
    private PedidoController pedidoController;

    @Mock
    private PedidoService pedidoService;

    @Mock
    private PedidoProducer pedidoProducer;

    
    @Test
    void createPedido_shouldReturnOk_whenMessageIsSentSuccessfully() {

        ItemPedidoDTO item1 = new ItemPedidoDTO();
        item1.setItemId(1L);
        item1.setQuantidade(2);

        ItemPedidoDTO item2 = new ItemPedidoDTO();
        item2.setItemId(2L);
        item2.setQuantidade(1);

        CreatePedidoDTO createPedidoDTO = new CreatePedidoDTO();
        createPedidoDTO.setContaId(1L);
        createPedidoDTO.setItens(Arrays.asList(item1, item2));

        doNothing().when(pedidoProducer).sendPedidoMessage(createPedidoDTO);

        // Chama o endpoint e verifica a resposta
        ResponseEntity<String> response = pedidoController.createPedido(createPedidoDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Pedido enviado para a fila com sucesso!", response.getBody());
        verify(pedidoProducer, times(1)).sendPedidoMessage(createPedidoDTO);
    }

    @Test
    void createPedido_shouldReturnInternalServerError_whenMessageFailsToSend() {

        ItemPedidoDTO item1 = new ItemPedidoDTO();
        item1.setItemId(1L);
        item1.setQuantidade(2);

        ItemPedidoDTO item2 = new ItemPedidoDTO();
        item2.setItemId(2L);
        item2.setQuantidade(1);

        CreatePedidoDTO createPedidoDTO = new CreatePedidoDTO();
        createPedidoDTO.setContaId(1L);
        createPedidoDTO.setItens(Arrays.asList(item1, item2));

        doThrow(new RuntimeException("Erro ao enviar mensagem")).when(pedidoProducer).sendPedidoMessage(createPedidoDTO);

        ResponseEntity<String> response = pedidoController.createPedido(createPedidoDTO);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Erro ao enviar pedido para a fila: Erro ao enviar mensagem", response.getBody());
        verify(pedidoProducer, times(1)).sendPedidoMessage(createPedidoDTO);
    }
    @Test
    void negarPedido_withExistentId_shouldReturnOk() throws Exception {
        Long pedidoId = 1L;
        Pedido pedidoNegado = mock(Pedido.class);

        when(pedidoService.negarPedido(pedidoId)).thenReturn(pedidoNegado);

        ResponseEntity<Pedido> response = pedidoController.negarPedido(pedidoId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(pedidoNegado, response.getBody());
        verify(pedidoService, times(1)).negarPedido(pedidoId);
    }

    @Test
    void negarPedido_withNotExistentId_shouldReturnNotFound() throws Exception {
        Long pedidoId = 1L;

        when(pedidoService.negarPedido(pedidoId)).thenThrow(new NotFoundException("Pedido não encontrado"));

        ResponseEntity<Pedido> response = pedidoController.negarPedido(pedidoId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(pedidoService, times(1)).negarPedido(pedidoId);
    }

    @Test
    void aprovarPedido_withExistentId_shouldReturnOk() throws Exception {
        Long pedidoId = 1L;
        Pedido pedidoAprovado = mock(Pedido.class);

        when(pedidoService.aprovarPedido(pedidoId)).thenReturn(pedidoAprovado);

        ResponseEntity<Pedido> response = pedidoController.aprovarPedido(pedidoId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(pedidoAprovado, response.getBody());
        verify(pedidoService, times(1)).aprovarPedido(pedidoId);
    }

    @Test
    void aprovarPedido_withNotExistentId_shouldReturnNotFound() throws Exception {
        Long pedidoId = 1L;

        when(pedidoService.aprovarPedido(pedidoId)).thenThrow(new NotFoundException("Pedido não encontrado"));

        ResponseEntity<Pedido> response = pedidoController.aprovarPedido(pedidoId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(pedidoService, times(1)).aprovarPedido(pedidoId);
    }

    @Test
    void marcarEntreguePedido_withExistentId_shouldReturnOk() throws Exception {
        Long pedidoId = 1L;
        Pedido pedidoEntregue = mock(Pedido.class);

        when(pedidoService.marcarEntreguePedido(pedidoId)).thenReturn(pedidoEntregue);

        ResponseEntity<Pedido> response = pedidoController.marcarEntreguePedido(pedidoId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(pedidoEntregue, response.getBody());
        verify(pedidoService, times(1)).marcarEntreguePedido(pedidoId);
    }

    @Test
    void marcarEntreguePedido_withNotExistentId_shouldReturnNotFound() throws Exception {
        Long pedidoId = 1L;

        when(pedidoService.marcarEntreguePedido(pedidoId)).thenThrow(new NotFoundException("Pedido não encontrado"));

        ResponseEntity<Pedido> response = pedidoController.marcarEntreguePedido(pedidoId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(pedidoService, times(1)).marcarEntreguePedido(pedidoId);
    }

    @Test
    void cancelarPedido_withExistentId_shouldReturnOk() throws Exception {
        Long pedidoId = 1L;
        Pedido pedidoCancelado = mock(Pedido.class);

        when(pedidoService.cancelarPedido(pedidoId)).thenReturn(pedidoCancelado);

        ResponseEntity<Pedido> response = pedidoController.cancelarPedido(pedidoId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(pedidoCancelado, response.getBody());
        verify(pedidoService, times(1)).cancelarPedido(pedidoId);
    }

    @Test
    void cancelarPedido_withNotExistentId_shouldReturnNotFound() throws Exception {
        Long pedidoId = 1L;

        when(pedidoService.cancelarPedido(pedidoId)).thenThrow(new NotFoundException("Pedido não encontrado"));

        ResponseEntity<Pedido> response = pedidoController.cancelarPedido(pedidoId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(pedidoService, times(1)).cancelarPedido(pedidoId);
    }

    @Test
    void getPedidoById_withExistentId_shouldReturnOk() throws Exception {
        Long pedidoId = 1L;
        PedidoResponseDTO pedido = mock(PedidoResponseDTO.class);

        when(pedidoService.getPedidoById(pedidoId)).thenReturn(pedido);

        ResponseEntity<Object> response = pedidoController.getPedidoById(pedidoId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(pedido, response.getBody());
        verify(pedidoService, times(1)).getPedidoById(pedidoId);
    }

    @Test
    void getPedidoById_withNotExistentId_shouldReturnNotFound() throws Exception {
        Long pedidoId = 1L;
        String errorMessage = "Pedido não encontrado";

        when(pedidoService.getPedidoById(pedidoId)).thenThrow(new NotFoundException(errorMessage));

        ResponseEntity<Object> response = pedidoController.getPedidoById(pedidoId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof ErrorResponseDTO);
        assertEquals(errorMessage, ((ErrorResponseDTO) response.getBody()).getMessage());
        verify(pedidoService, times(1)).getPedidoById(pedidoId);
    }
    

    @Test
    void getPedidoById_withException_shouldReturnInternalServerError() throws Exception {
        Long pedidoId = 1L;
        String errorMessage = "Não encontrado";

        when(pedidoService.getPedidoById(pedidoId)).thenThrow(new NotFoundException(errorMessage));

        ResponseEntity<Object> response = pedidoController.getPedidoById(pedidoId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof ErrorResponseDTO);
        assertEquals(errorMessage, ((ErrorResponseDTO) response.getBody()).getMessage());
        verify(pedidoService, times(1)).getPedidoById(pedidoId);
    }


    @Test
    void getAllPedidos_withEscolaId_shouldReturnOk() {
        Long escolaId = 1L;
        PedidoResponseDTO pedidoResponse = mock(PedidoResponseDTO.class);

        when(pedidoService.getAllPedidos(escolaId, null)).thenReturn(List.of(pedidoResponse));

        ResponseEntity<List<PedidoResponseDTO>> response = pedidoController.getAllPedidos(escolaId, null);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().isEmpty());
        assertTrue(response.getBody().get(0) instanceof PedidoResponseDTO);
        verify(pedidoService, times(1)).getAllPedidos(escolaId, null);
    }

    @Test
    void getAllPedidos_withoutEscolaId_shouldReturnOk() {
        PedidoResponseDTO pedidoResponse = mock(PedidoResponseDTO.class);

        when(pedidoService.getAllPedidos(null, null)).thenReturn(List.of(pedidoResponse));

        ResponseEntity<List<PedidoResponseDTO>> response = pedidoController.getAllPedidos(null, null);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().isEmpty());
        assertTrue(response.getBody().get(0) instanceof PedidoResponseDTO);
        verify(pedidoService, times(1)).getAllPedidos(null, null);
    }


    @Test
    void deletePedido_withExistentId_shouldReturnOk() throws Exception {
        Long pedidoId = 1L;

        doNothing().when(pedidoService).deletePedido(pedidoId);

        ResponseEntity<Void> response = pedidoController.deletePedido(pedidoId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNull(response.getBody());
        verify(pedidoService, times(1)).deletePedido(pedidoId);
    }
}
