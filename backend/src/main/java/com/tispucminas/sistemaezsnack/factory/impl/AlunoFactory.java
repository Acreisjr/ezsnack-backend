package com.tispucminas.sistemaezsnack.factory.impl;

import java.security.NoSuchAlgorithmException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;
import com.tispucminas.sistemaezsnack.dto.UsuarioCreateDTO;
import com.tispucminas.sistemaezsnack.exception.NotFoundException;
import com.tispucminas.sistemaezsnack.factory.UsuarioFactory;
import com.tispucminas.sistemaezsnack.model.Aluno;
import com.tispucminas.sistemaezsnack.model.Conta;
import com.tispucminas.sistemaezsnack.model.Escola;
import com.tispucminas.sistemaezsnack.model.Responsavel;
import com.tispucminas.sistemaezsnack.model.Usuario;
import com.tispucminas.sistemaezsnack.service.ContaService;
import com.tispucminas.sistemaezsnack.service.EscolaService;
import com.tispucminas.sistemaezsnack.service.SenhaUtilService;
import com.tispucminas.sistemaezsnack.service.UsuarioService;


@Component
@DependsOn({"escolaService", "usuarioService"})
public class AlunoFactory implements UsuarioFactory {

    @Autowired
    private EscolaService escolaService;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private ContaService contaService;

    public Usuario createUsuario(UsuarioCreateDTO usuarioCreateDTO) throws NotFoundException, NoSuchAlgorithmException {
    
            Aluno aluno = new Aluno();
            Escola escola = escolaService.getEscolaById(usuarioCreateDTO.getEscolaId());
            Responsavel responsavel = (Responsavel) usuarioService.getById(usuarioCreateDTO.getResponsavelId());
            aluno.setNome(usuarioCreateDTO.getNome());
            aluno.setUser(usuarioCreateDTO.getUser());
            aluno.setMatricula(usuarioCreateDTO.getIdentificacao());
            aluno.setResponsavel(responsavel);
            aluno.setEscola(escola);
            aluno.setSenha(SenhaUtilService.criptografarSenha(usuarioCreateDTO.getSenha()));
            Usuario usuarioSalvo = usuarioService.saveUsuario(aluno);
            Conta conta = contaService.create((Aluno) usuarioSalvo);
            aluno.setId(usuarioSalvo.getId());
            aluno.setConta(conta);
            usuarioService.saveUsuario(aluno);
            return aluno;
    }
}