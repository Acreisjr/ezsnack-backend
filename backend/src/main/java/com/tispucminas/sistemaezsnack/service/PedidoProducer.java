package com.tispucminas.sistemaezsnack.service;

import com.tispucminas.sistemaezsnack.dto.CreatePedidoDTO;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class PedidoProducer {

    private final RabbitTemplate rabbitTemplate;

    @Value("${spring.rabbitmq.queue}")
    private String queueName;

    public PedidoProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendPedidoMessage(CreatePedidoDTO createPedidoDTO) {
        rabbitTemplate.convertAndSend(queueName, createPedidoDTO);
        System.out.println("Pedido enviado para a fila: " + createPedidoDTO);
    }
}
