package com.tispucminas.sistemaezsnack.service;

import com.tispucminas.sistemaezsnack.dto.CreatePedidoDTO;
import com.tispucminas.sistemaezsnack.exception.IllegalOperationException;
import com.tispucminas.sistemaezsnack.exception.NotFoundException;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PedidoConsumer {

    @Autowired
    private PedidoService pedidoService;

    @RabbitListener(queues = "${spring.rabbitmq.queue}")
    public void receiveMessage(CreatePedidoDTO novoPedidoDTO) {

        System.out.println("Pedido recebido: " + novoPedidoDTO);
        
        if (novoPedidoDTO.getItens() == null || novoPedidoDTO.getItens().isEmpty()) {
            System.err.println("Erro: Pedido inválido - Nenhum item encontrado no DTO.");
            return;
        }

        try {
            pedidoService.createPedido(novoPedidoDTO);
            System.out.println("Pedido criado com sucesso");
        } catch (NotFoundException e) {
            System.err.println("Erro: Item ou conta não encontrado - " + e.getMessage());
        } catch (IllegalOperationException e) {
            System.err.println("Erro: Operação ilegal - " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Erro inesperado ao criar pedido - " + e.getMessage());
        }

    }
}
  