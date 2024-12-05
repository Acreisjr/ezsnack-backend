package com.tispucminas.sistemaezsnack.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.tispucminas.sistemaezsnack.dto.ContaResponseDTO;
import com.tispucminas.sistemaezsnack.model.Conta;

@Mapper
public interface ContaMapper {
    ContaMapper INSTANCE = Mappers.getMapper(ContaMapper.class);

    @Mapping(source = "responsavel.id", target = "responsavelId")
    @Mapping(source = "aluno.id", target = "alunoId")
    ContaResponseDTO contaToContaResponseDTO(Conta conta);
}
