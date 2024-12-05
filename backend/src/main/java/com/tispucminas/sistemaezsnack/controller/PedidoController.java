package com.tispucminas.sistemaezsnack.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tispucminas.sistemaezsnack.dto.CreatePedidoDTO;
import com.tispucminas.sistemaezsnack.dto.ErrorResponseDTO;
import com.tispucminas.sistemaezsnack.dto.PedidoResponseDTO;
import com.tispucminas.sistemaezsnack.exception.NotFoundException;
import com.tispucminas.sistemaezsnack.model.Pedido;
import com.tispucminas.sistemaezsnack.service.PedidoProducer;
import com.tispucminas.sistemaezsnack.service.PedidoService;

@RestController
@RequestMapping("/pedidos")
public class PedidoController {
    
    @Autowired
    private PedidoService pedidoService;

    @Autowired
    private PedidoProducer pedidoProducer;

    @PostMapping("/create")
    public ResponseEntity<String> createPedido(@RequestBody CreatePedidoDTO createPedidoDTO) {
        try {
            pedidoProducer.sendPedidoMessage(createPedidoDTO);
            return ResponseEntity.ok("Pedido enviado para a fila com sucesso!");
        } catch (Exception e) {
            System.err.println("Erro ao enviar pedido para a fila: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body("Erro ao enviar pedido para a fila: " + e.getMessage());
        }
    }

    @PutMapping("/{id}/negar")
    public ResponseEntity<Pedido> negarPedido(@PathVariable Long id) {
        try {
            Pedido pedidoNegado = pedidoService.negarPedido(id);
            return ResponseEntity.ok(pedidoNegado);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PutMapping("/{id}/aprovar")
    public ResponseEntity<Pedido> aprovarPedido(@PathVariable Long id) {
        try {
            Pedido pedidoAprovado = pedidoService.aprovarPedido(id);
            return ResponseEntity.ok(pedidoAprovado);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PutMapping("/{id}/entregue")
    public ResponseEntity<Pedido> marcarEntreguePedido(@PathVariable Long id) {
        try {
            Pedido pedidoEntregue = pedidoService.marcarEntreguePedido(id);
            return ResponseEntity.ok(pedidoEntregue);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PutMapping("/{id}/cancelar")
    public ResponseEntity<Pedido> cancelarPedido(@PathVariable Long id) {
        try {
            Pedido pedidoCancelado = pedidoService.cancelarPedido(id);
            return ResponseEntity.ok(pedidoCancelado);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getPedidoById(@PathVariable Long id) {
        try{
            return ResponseEntity.status(HttpStatus.OK).body(pedidoService.getPedidoById(id));
        }catch(NotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponseDTO(e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<List<PedidoResponseDTO>> getAllPedidos(@RequestParam(required = false) Long escolaId, @RequestParam(required = false) Long alunoId) {
        return ResponseEntity.ok(pedidoService.getAllPedidos(escolaId, alunoId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePedido(@PathVariable Long id) {
        pedidoService.deletePedido(id);
        return ResponseEntity.ok().build();
    }

}
