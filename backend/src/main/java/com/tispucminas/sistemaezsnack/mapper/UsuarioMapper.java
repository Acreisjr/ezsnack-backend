package com.tispucminas.sistemaezsnack.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import com.tispucminas.sistemaezsnack.dto.AlunoResponseDTO;
import com.tispucminas.sistemaezsnack.dto.EscolaResponseDTO;
import com.tispucminas.sistemaezsnack.dto.UsuarioResponseDTO;
import com.tispucminas.sistemaezsnack.model.Admin;
import com.tispucminas.sistemaezsnack.model.AdminEscola;
import com.tispucminas.sistemaezsnack.model.Aluno;
import com.tispucminas.sistemaezsnack.model.Escola;
import com.tispucminas.sistemaezsnack.model.Responsavel;
import com.tispucminas.sistemaezsnack.model.Usuario;
import com.tispucminas.sistemaezsnack.model.Enums.TipoUsuarioEnum;

@Mapper
public interface UsuarioMapper {
    UsuarioMapper INSTANCE = Mappers.getMapper(UsuarioMapper.class);
    
    @Mapping(target="tipo", source="usuario", qualifiedByName = "tipoConverter")
    @Mapping(target = "escola", source = "usuario", qualifiedByName = "mapEscola")
    @Mapping(target = "alunos", source = "usuario", qualifiedByName = "mapAluno")
    @Mapping(target = "nome", source = "usuario", qualifiedByName = "mapNome")
    @Mapping(target = "matricula", source = "usuario", qualifiedByName = "mapMatricula")
    @Mapping(target = "cpf", source = "usuario", qualifiedByName = "mapCpf")
    UsuarioResponseDTO toUsuarioResponse(Usuario usuario);

    @Named("tipoConverter")
    default String tipoConverter(Usuario usuario){
        if(usuario instanceof AdminEscola){
            return TipoUsuarioEnum.ADMIN_ESCOLA.name();
        }else if(usuario instanceof Admin){
            return TipoUsuarioEnum.ADMIN.name();
        }else if(usuario instanceof Aluno){
            return TipoUsuarioEnum.ALUNO.name();
        }else if(usuario instanceof Responsavel){
            return TipoUsuarioEnum.RESPONSAVEL.name();
        }
        return "";
    }

    @Named("mapEscola")
    default EscolaResponseDTO mapEscola(Usuario usuario){

        Escola escola;

        if(usuario instanceof AdminEscola){

            escola = ((AdminEscola) usuario).getEscola();

            return EscolaResponseDTO.builder()
                .id(escola.getId())
                .cidade(escola.getCidade())
                .cnpj(escola.getCnpj())
                .estado(escola.getEstado())
                .nome(escola.getNome())
                .telefone(escola.getTelefone()).build();

        }else if(usuario instanceof Responsavel){

            boolean hasAluno = ((Responsavel) usuario).getAlunos().stream().collect(Collectors.toList()).size() > 0;

            if(hasAluno){
                escola = ((Responsavel) usuario).getAlunos().stream().findFirst().get().getEscola();
                return EscolaResponseDTO.builder()
                .id(escola.getId())
                .cidade(escola.getCidade())
                .cnpj(escola.getCnpj())
                .estado(escola.getEstado())
                .nome(escola.getNome())
                .telefone(escola.getTelefone()).build();
            }
            
            return null;

        }else if(usuario instanceof Aluno){
            escola = ((Aluno) usuario).getEscola();
            return EscolaResponseDTO.builder()
                .id(escola.getId())
                .cidade(escola.getCidade())
                .cnpj(escola.getCnpj())
                .estado(escola.getEstado())
                .nome(escola.getNome())
                .telefone(escola.getTelefone()).build();
        }

        return null;
    }

    @Named("mapAluno")
    default List<AlunoResponseDTO> mapAluno(Usuario usuario){

        Aluno aluno;

        if(usuario instanceof Aluno){

            aluno = ((Aluno) usuario);

            return List.of(AlunoResponseDTO.builder()
                .id(aluno.getId())
                .nome(aluno.getNome())
                .matricula(aluno.getMatricula())
                .user(aluno.getUser())
                .contaId(aluno.getConta().getId()).build());

        }else if(usuario instanceof Responsavel){

            boolean hasAluno = ((Responsavel) usuario).getAlunos().stream().collect(Collectors.toList()).size() > 0;

            if(hasAluno){

                var alunos = ((Responsavel) usuario).getAlunos();
                
                List<AlunoResponseDTO> alunoDtos = alunos.stream()
                    .map(alunoEntity -> AlunoResponseDTO.builder()
                        .id(alunoEntity.getId())
                        .nome(alunoEntity.getNome())
                        .matricula(alunoEntity.getMatricula())
                        .user(alunoEntity.getUser())
                        .contaId(alunoEntity.getConta().getId())
                        .build())
                    .collect(Collectors.toList());
                    return alunoDtos;
            }
            
            return null;
            
        }

        return null;
    }

    @Named("mapNome")
    default String mapNome(Usuario usuario){

        if(usuario instanceof Aluno){

            return ((Aluno) usuario).getNome();

        }else if(usuario instanceof Responsavel){
            return ((Responsavel) usuario).getNome();
        }

        return null;
    }

    @Named("mapCpf")
    default String mapCpf(Usuario usuario){

        if(usuario instanceof Responsavel){
            return ((Responsavel) usuario).getCpf();
        }

        return null;
    }

    @Named("mapMatricula")
    default String mapMatricula(Usuario usuario){

        if(usuario instanceof Aluno){

            return ((Aluno) usuario).getMatricula();

        }

        return null;
    }

}
