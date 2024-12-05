package com.tispucminas.sistemaezsnack.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.tispucminas.sistemaezsnack.dto.ItemResponseDTO;
import com.tispucminas.sistemaezsnack.dto.PedidoResponseDTO;
import com.tispucminas.sistemaezsnack.model.ItemPedido;
import com.tispucminas.sistemaezsnack.model.Pedido;
import java.util.List;

@Mapper
public interface PedidoMapper {
    PedidoMapper INSTANCE = Mappers.getMapper(PedidoMapper.class);

    List<PedidoResponseDTO> toResponseList(List<Pedido> pedidos);

    @Mapping(target = "nomeAluno", source = "pedido.conta.aluno.nome")
    @Mapping(target = "itensPedidos", source = "itensPedidos")
    PedidoResponseDTO toResponse(Pedido pedido);

    List<ItemResponseDTO> toItemResponseList(List<ItemPedido> items);

    @Mapping(target = "nome", source = "itemEstoqueCantina.nome")
    ItemResponseDTO toItemResponseDTO(ItemPedido itemPedido);


}
