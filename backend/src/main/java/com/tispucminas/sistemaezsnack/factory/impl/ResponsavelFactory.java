package com.tispucminas.sistemaezsnack.factory.impl;

import java.util.ArrayList;

import org.springframework.stereotype.Component;

import com.tispucminas.sistemaezsnack.dto.UsuarioCreateDTO;
import com.tispucminas.sistemaezsnack.factory.UsuarioFactory;
import com.tispucminas.sistemaezsnack.model.Aluno;
import com.tispucminas.sistemaezsnack.model.Responsavel;
import com.tispucminas.sistemaezsnack.model.Usuario;

@Component
public class ResponsavelFactory implements UsuarioFactory {

    public Usuario createUsuario(UsuarioCreateDTO usuarioCreateDTO) throws Exception {        
            Responsavel responsavel = new Responsavel();
            responsavel.setUser(usuarioCreateDTO.getUser());
            responsavel.setNome(usuarioCreateDTO.getNome());
            responsavel.setCpf(usuarioCreateDTO.getIdentificacao());
            responsavel.setAlunos(new ArrayList<Aluno>());
            return responsavel;
    }
}
