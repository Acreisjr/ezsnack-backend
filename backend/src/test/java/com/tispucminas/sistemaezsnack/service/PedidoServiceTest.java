package com.tispucminas.sistemaezsnack.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.tispucminas.sistemaezsnack.dto.CreatePedidoDTO;
import com.tispucminas.sistemaezsnack.dto.PedidoResponseDTO;
import com.tispucminas.sistemaezsnack.dto.ItemPedidoDTO;
import com.tispucminas.sistemaezsnack.exception.IllegalOperationException;
import com.tispucminas.sistemaezsnack.exception.NotFoundException;
import com.tispucminas.sistemaezsnack.model.Conta;
import com.tispucminas.sistemaezsnack.model.ItemEstoqueCantina;
import com.tispucminas.sistemaezsnack.model.ItemPedido;
import com.tispucminas.sistemaezsnack.model.Pedido;
import com.tispucminas.sistemaezsnack.model.Enums.PedidoStatusEnum;
import com.tispucminas.sistemaezsnack.repository.ContaRepository;
import com.tispucminas.sistemaezsnack.repository.ItemEstoqueCantinaRepository;
import com.tispucminas.sistemaezsnack.repository.PedidoRepository;

@ExtendWith(MockitoExtension.class)
public class PedidoServiceTest {

    @InjectMocks
    private PedidoService pedidoService;

    @Mock
    private PedidoRepository pedidoRepository;

    @Mock
    private ItemEstoqueCantinaRepository itemRepository;

    @Mock
    private ContaRepository contaRepository;

    @Mock
    private ContaService contaService;

    @Test
    void createPedido_withValidData_shouldReturnPedidoResponseDTO() throws NotFoundException, IllegalOperationException {
        CreatePedidoDTO dto = new CreatePedidoDTO();
        dto.setContaId(1L);
        ItemPedidoDTO itemDto = new ItemPedidoDTO();
        itemDto.setItemId(1L);
        itemDto.setQuantidade(2);
        dto.setItens(Arrays.asList(itemDto));

        ItemEstoqueCantina item = new ItemEstoqueCantina();
        item.setId(1L);
        item.setPreco(new BigDecimal("10.00"));

        Pedido pedido = new Pedido();
        pedido.setId(1L);
        pedido.setStatus(PedidoStatusEnum.PENDENTE.getDescricao());
        pedido.setData(LocalDateTime.now());

        ItemPedido itemPedido = new ItemPedido();
        itemPedido.setPedido(pedido);
        itemPedido.setQuantidade(2);
        itemPedido.setItemEstoqueCantina(item);
        itemPedido.setValorTotal(new BigDecimal("20.00"));
        pedido.setItensPedidos(Arrays.asList(itemPedido));

        Conta conta = new Conta();
        conta.setId(1L);
        conta.setSaldo(new BigDecimal("100.00"));

        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));
        when(pedidoRepository.save(any())).thenReturn(pedido);
        when(contaService.verificaSaldoSuficiente(1L, new BigDecimal("20.00"))).thenReturn(conta);

        pedidoService.createPedido(dto);

        verify(itemRepository, times(1)).findById(1L);
        verify(pedidoRepository, times(1)).save(any(Pedido.class));
        verify(contaService, times(1)).verificaSaldoSuficiente(1L, new BigDecimal("20.00"));
    }

    @Test
    void createPedido_withItemNotFound_shouldThrowRuntimeException() throws NotFoundException, IllegalOperationException {
        CreatePedidoDTO dto = new CreatePedidoDTO();
        dto.setContaId(1L);
        ItemPedidoDTO itemDto = new ItemPedidoDTO();
        itemDto.setItemId(1L);
        itemDto.setQuantidade(2);
        dto.setItens(Arrays.asList(itemDto));

        when(itemRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            pedidoService.createPedido(dto);
        });

        assertEquals("Item não encontrado!", exception.getMessage());
        verify(itemRepository, times(1)).findById(1L);
        verify(pedidoRepository, times(0)).save(any(Pedido.class));
        verify(contaService, times(0)).verificaSaldoSuficiente(anyLong(), any(BigDecimal.class));
    }

    @Test
    void createPedido_withContaNotFound_shouldThrowNotFoundException() throws NotFoundException, IllegalOperationException {
        CreatePedidoDTO dto = new CreatePedidoDTO();
        dto.setContaId(1L);
        ItemPedidoDTO itemDto = new ItemPedidoDTO();
        itemDto.setItemId(1L);
        itemDto.setQuantidade(2);
        dto.setItens(Arrays.asList(itemDto));

        ItemEstoqueCantina item = new ItemEstoqueCantina();
        item.setId(1L);
        item.setPreco(new BigDecimal("10.00"));

        Pedido pedido = new Pedido();
        pedido.setId(1L);
        pedido.setStatus(PedidoStatusEnum.PENDENTE.getDescricao());
        pedido.setData(LocalDateTime.now());

        ItemPedido itemPedido = new ItemPedido();
        itemPedido.setPedido(pedido);
        itemPedido.setQuantidade(2);
        itemPedido.setItemEstoqueCantina(item);
        itemPedido.setValorTotal(new BigDecimal("20.00"));
        pedido.setItensPedidos(Arrays.asList(itemPedido));

        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));
        when(contaService.verificaSaldoSuficiente(1L, new BigDecimal("20.00"))).thenThrow(new NotFoundException("Conta não encontrada!"));

        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            pedidoService.createPedido(dto);
        });

        assertEquals("Conta não encontrada!", exception.getMessage());
        verify(itemRepository, times(1)).findById(1L);
        verify(pedidoRepository, never()).save(any());
        verify(contaService, times(1)).verificaSaldoSuficiente(1L, new BigDecimal("20.00"));
    }

    @Test
    void createPedido_withIllegalOperationException_shouldThrowIllegalOperationException() throws NotFoundException, IllegalOperationException {
        CreatePedidoDTO dto = new CreatePedidoDTO();
        dto.setContaId(1L);
        ItemPedidoDTO itemDto = new ItemPedidoDTO();
        itemDto.setItemId(1L);
        itemDto.setQuantidade(2);
        dto.setItens(Arrays.asList(itemDto));

        ItemEstoqueCantina item = new ItemEstoqueCantina();
        item.setId(1L);
        item.setPreco(new BigDecimal("10.00"));

        Pedido pedido = new Pedido();
        pedido.setId(1L);
        pedido.setStatus(PedidoStatusEnum.PENDENTE.getDescricao());
        pedido.setData(LocalDateTime.now());

        ItemPedido itemPedido = new ItemPedido();
        itemPedido.setPedido(pedido);
        itemPedido.setQuantidade(2);
        itemPedido.setItemEstoqueCantina(item);
        itemPedido.setValorTotal(new BigDecimal("20.00"));
        pedido.setItensPedidos(Arrays.asList(itemPedido));

        Conta conta = new Conta();
        conta.setId(1L);
        conta.setSaldo(new BigDecimal("10.00"));

        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));
        when(contaService.verificaSaldoSuficiente(1L, new BigDecimal("20.00"))).thenThrow(new IllegalOperationException("Saldo insuficiente"));

        IllegalOperationException exception = assertThrows(IllegalOperationException.class, () -> {
            pedidoService.createPedido(dto);
        });

        assertEquals("Saldo insuficiente", exception.getMessage());
        verify(itemRepository, times(1)).findById(1L);
        verify(pedidoRepository, never()).save(any(Pedido.class));
        verify(contaService, times(1)).verificaSaldoSuficiente(1L, new BigDecimal("20.00"));
    }

    @Test
    void negarPedido_withExistentIdAndPendenteStatus_shouldReturnNegadoPedido() throws Exception {
        Long id = 1L;
        Pedido pedido = new Pedido();
        pedido.setId(id);
        pedido.setStatus(PedidoStatusEnum.PENDENTE.getDescricao());

        when(pedidoRepository.findById(id)).thenReturn(Optional.of(pedido));
        when(pedidoRepository.save(pedido)).thenReturn(pedido);

        Pedido result = pedidoService.negarPedido(id);

        assertNotNull(result);
        assertEquals(PedidoStatusEnum.NEGADO.getDescricao(), result.getStatus());
        verify(pedidoRepository, times(1)).findById(id);
        verify(pedidoRepository, times(1)).save(pedido);
    }

    @Test
    void negarPedido_withNonExistentId_shouldThrowException() {
        Long id = 1L;

        when(pedidoRepository.findById(id)).thenReturn(Optional.empty());

        Exception exception = assertThrows(Exception.class, () -> {
            pedidoService.negarPedido(id);
        });

        assertEquals("Pedido não encontrado", exception.getMessage());
        verify(pedidoRepository, times(1)).findById(id);
        verify(pedidoRepository, times(0)).save(any(Pedido.class));
    }

    @Test
    void negarPedido_withNonPendenteStatus_shouldThrowException() {
        Long id = 1L;
        Pedido pedido = new Pedido();
        pedido.setId(id);
        pedido.setStatus(PedidoStatusEnum.APROVADO.getDescricao());

        when(pedidoRepository.findById(id)).thenReturn(Optional.of(pedido));

        Exception exception = assertThrows(Exception.class, () -> {
            pedidoService.negarPedido(id);
        });

        assertEquals("Apenas pedidos pendentes podem ser negados.", exception.getMessage());
        verify(pedidoRepository, times(1)).findById(id);
        verify(pedidoRepository, times(0)).save(any(Pedido.class));
    }

    @Test
    void aprovarPedido_withExistentIdAndPendenteStatusAndSufficientSaldo_shouldReturnAprovadoPedido() throws Exception {
        Long id = 1L;
        Pedido pedido = new Pedido();
        pedido.setId(id);
        pedido.setStatus(PedidoStatusEnum.PENDENTE.getDescricao());
        pedido.setPrecoTotal(new BigDecimal("50.00"));
        Conta conta = new Conta();
        conta.setSaldo(new BigDecimal("100.00"));
        pedido.setConta(conta);

        when(pedidoRepository.findById(id)).thenReturn(Optional.of(pedido));
        when(contaRepository.save(conta)).thenReturn(conta);
        when(pedidoRepository.save(pedido)).thenReturn(pedido);

        Pedido result = pedidoService.aprovarPedido(id);

        assertNotNull(result);
        assertEquals(PedidoStatusEnum.APROVADO.getDescricao(), result.getStatus());
        assertEquals(new BigDecimal("50.00"), conta.getSaldo());
        verify(pedidoRepository, times(1)).findById(id);
        verify(pedidoRepository, times(1)).save(pedido);
        verify(contaRepository, times(1)).save(conta);
    }

    @Test
    void aprovarPedido_withNonExistentId_shouldThrowException() {
        Long id = 1L;

        when(pedidoRepository.findById(id)).thenReturn(Optional.empty());

        Exception exception = assertThrows(Exception.class, () -> {
            pedidoService.aprovarPedido(id);
        });

        assertEquals("Pedido não encontrado", exception.getMessage());
        verify(pedidoRepository, times(1)).findById(id);
        verify(contaRepository, times(0)).save(any(Conta.class));
        verify(pedidoRepository, times(0)).save(any(Pedido.class));
    }

    @Test
    void aprovarPedido_withNonPendenteStatus_shouldThrowException() {
        Long id = 1L;
        Pedido pedido = new Pedido();
        pedido.setId(id);
        pedido.setStatus(PedidoStatusEnum.APROVADO.getDescricao());

        when(pedidoRepository.findById(id)).thenReturn(Optional.of(pedido));

        Exception exception = assertThrows(Exception.class, () -> {
            pedidoService.aprovarPedido(id);
        });

        assertEquals("Apenas pedidos pendentes podem ser aprovados.", exception.getMessage());
        verify(pedidoRepository, times(1)).findById(id);
        verify(contaRepository, times(0)).save(any(Conta.class));
        verify(pedidoRepository, times(0)).save(any(Pedido.class));
    }

    @Test
    void aprovarPedido_withInsufficientSaldo_shouldThrowIllegalOperationException() throws Exception {
        Long id = 1L;
        Pedido pedido = new Pedido();
        pedido.setId(id);
        pedido.setStatus(PedidoStatusEnum.PENDENTE.getDescricao());
        pedido.setPrecoTotal(new BigDecimal("150.00"));
        Conta conta = new Conta();
        conta.setSaldo(new BigDecimal("100.00"));
        pedido.setConta(conta);

        when(pedidoRepository.findById(id)).thenReturn(Optional.of(pedido));

        IllegalOperationException exception = assertThrows(IllegalOperationException.class, () -> {
            pedidoService.aprovarPedido(id);
        });

        assertEquals("Saldo insuficiente do aluno para realizar pedido.", exception.getMessage());
        verify(pedidoRepository, times(1)).findById(id);
        verify(contaRepository, times(0)).save(any(Conta.class));
        verify(pedidoRepository, times(0)).save(any(Pedido.class));
    }

    @Test
    void marcarEntreguePedido_withExistentIdAndAprovadoStatus_shouldReturnEntreguePedido() throws Exception {
        Long id = 1L;
        Pedido pedido = new Pedido();
        pedido.setId(id);
        pedido.setStatus(PedidoStatusEnum.APROVADO.getDescricao());
        pedido.setPrecoTotal(new BigDecimal("50.00"));
        Conta conta = new Conta();
        conta.setSaldo(new BigDecimal("50.00"));
        pedido.setConta(conta);

        when(pedidoRepository.findById(id)).thenReturn(Optional.of(pedido));
        when(contaRepository.save(conta)).thenReturn(conta);
        when(pedidoRepository.save(pedido)).thenReturn(pedido);

        Pedido result = pedidoService.marcarEntreguePedido(id);

        assertNotNull(result);
        assertEquals(PedidoStatusEnum.ENTREGUE.getDescricao(), result.getStatus());
        assertEquals(new BigDecimal("0.00"), conta.getSaldo());
        verify(pedidoRepository, times(1)).findById(id);
        verify(contaRepository, times(1)).save(conta);
        verify(pedidoRepository, times(1)).save(pedido);
    }

    @Test
    void marcarEntreguePedido_withNonExistentId_shouldThrowNotFoundException() {
        Long id = 1L;

        when(pedidoRepository.findById(id)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            pedidoService.marcarEntreguePedido(id);
        });

        assertEquals("Pedido não encontrado", exception.getMessage());
        verify(pedidoRepository, times(1)).findById(id);
        verify(contaRepository, times(0)).save(any(Conta.class));
        verify(pedidoRepository, times(0)).save(any(Pedido.class));
    }

    @Test
    void marcarEntreguePedido_withNonAprovadoStatus_shouldThrowException() throws Exception {
        Long id = 1L;
        Pedido pedido = new Pedido();
        pedido.setId(id);
        pedido.setStatus(PedidoStatusEnum.PENDENTE.getDescricao());

        when(pedidoRepository.findById(id)).thenReturn(Optional.of(pedido));

        Exception exception = assertThrows(Exception.class, () -> {
            pedidoService.marcarEntreguePedido(id);
        });

        assertEquals("Apenas pedidos aprovados podem ser marcados como entregue.", exception.getMessage());
        verify(pedidoRepository, times(1)).findById(id);
        verify(contaRepository, times(0)).save(any(Conta.class));
        verify(pedidoRepository, times(0)).save(any(Pedido.class));
    }

    @Test
    void cancelarPedido_withExistentIdAndAprovadoStatus_shouldReturnCanceladoPedido() throws Exception {
        Long id = 1L;
        Pedido pedido = new Pedido();
        pedido.setId(id);
        pedido.setStatus(PedidoStatusEnum.APROVADO.getDescricao());
        pedido.setPrecoTotal(new BigDecimal("50.00"));
        Conta conta = new Conta();
        conta.setSaldo(new BigDecimal("50.00"));
        pedido.setConta(conta);

        when(pedidoRepository.findById(id)).thenReturn(Optional.of(pedido));
        when(contaRepository.save(conta)).thenReturn(conta);
        when(pedidoRepository.save(pedido)).thenReturn(pedido);

        Pedido result = pedidoService.cancelarPedido(id);

        assertNotNull(result);
        assertEquals(PedidoStatusEnum.CANCELADO.getDescricao(), result.getStatus());
        assertEquals(new BigDecimal("100.00"), conta.getSaldo());
        verify(pedidoRepository, times(1)).findById(id);
        verify(contaRepository, times(1)).save(conta);
        verify(pedidoRepository, times(1)).save(pedido);
    }

    @Test
    void cancelarPedido_withNonExistentId_shouldThrowException() {
        Long id = 1L;

        when(pedidoRepository.findById(id)).thenReturn(Optional.empty());

        Exception exception = assertThrows(Exception.class, () -> {
            pedidoService.cancelarPedido(id);
        });

        assertEquals("Pedido não encontrado", exception.getMessage());
        verify(pedidoRepository, times(1)).findById(id);
        verify(contaRepository, times(0)).save(any(Conta.class));
        verify(pedidoRepository, times(0)).save(any(Pedido.class));
    }

    @Test
    void cancelarPedido_withNonAprovadoStatus_shouldThrowException() throws Exception {
        Long id = 1L;
        Pedido pedido = new Pedido();
        pedido.setId(id);
        pedido.setStatus(PedidoStatusEnum.PENDENTE.getDescricao());

        when(pedidoRepository.findById(id)).thenReturn(Optional.of(pedido));

        Exception exception = assertThrows(Exception.class, () -> {
            pedidoService.cancelarPedido(id);
        });

        assertEquals("Apenas pedidos aprovados podem ser cancelados.", exception.getMessage());
        verify(pedidoRepository, times(1)).findById(id);
        verify(contaRepository, times(0)).save(any(Conta.class));
        verify(pedidoRepository, times(0)).save(any(Pedido.class));
    }

    @Test
    void getPedidoById_withExistentId_shouldReturnPedidoResponseDTO() throws NotFoundException {
        Long id = 1L;
        Pedido pedido = new Pedido();
        pedido.setId(id);

        when(pedidoRepository.findById(id)).thenReturn(Optional.of(pedido));

        PedidoResponseDTO result = pedidoService.getPedidoById(id);

        assertNotNull(result);
        verify(pedidoRepository, times(1)).findById(id);
    }

    @Test
    void getPedidoById_withNonExistentId_shouldThrowNotFoundException() {
        Long id = 1L;

        when(pedidoRepository.findById(id)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            pedidoService.getPedidoById(id);
        });

        assertEquals("Pedido não encontrado", exception.getMessage());
        verify(pedidoRepository, times(1)).findById(id);
    }

    @Test
    void getAllPedidos_withEscolaId_shouldReturnListPedidoResponseDTO() {
        Long escolaId = 1L;
        Pedido pedido1 = new Pedido();
        Pedido pedido2 = new Pedido();
        List<Pedido> pedidos = Arrays.asList(pedido1, pedido2);

        when(pedidoRepository.findByEscolaId(escolaId)).thenReturn(pedidos);
        List<PedidoResponseDTO> result = pedidoService.getAllPedidos(escolaId, null);

        assertEquals(2, result.size());
        verify(pedidoRepository, times(1)).findByEscolaId(escolaId);
    }

    @Test
    void getAllPedidos_withoutEscolaId_shouldReturnAllPedidosResponseDTO() {
        Pedido pedido1 = new Pedido();
        Pedido pedido2 = new Pedido();
        List<Pedido> pedidos = Arrays.asList(pedido1, pedido2);

        when(pedidoRepository.findAll()).thenReturn(pedidos);

        List<PedidoResponseDTO> result = pedidoService.getAllPedidos(null, null);

        assertEquals(2, result.size());
        verify(pedidoRepository, times(1)).findAll();
    }

    @Test
    void deletePedido_shouldDeletePedido() {
        Long id = 1L;

        doNothing().when(pedidoRepository).deleteById(id);

        pedidoService.deletePedido(id);

        verify(pedidoRepository, times(1)).deleteById(id);
    }
}
