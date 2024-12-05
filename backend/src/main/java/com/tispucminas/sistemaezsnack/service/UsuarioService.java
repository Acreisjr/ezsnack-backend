package com.tispucminas.sistemaezsnack.service;

import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.tispucminas.sistemaezsnack.dto.UsuarioCreateDTO;
import com.tispucminas.sistemaezsnack.dto.UsuarioResponseDTO;
import com.tispucminas.sistemaezsnack.dto.UsuarioUpdateDTO;
import com.tispucminas.sistemaezsnack.exception.NotFoundException;
import com.tispucminas.sistemaezsnack.exception.UnauthorizedException;
import com.tispucminas.sistemaezsnack.factory.impl.*;
import com.tispucminas.sistemaezsnack.mapper.UsuarioMapper;
import com.tispucminas.sistemaezsnack.factory.UsuarioFactory;
import com.tispucminas.sistemaezsnack.model.Usuario;
import com.tispucminas.sistemaezsnack.model.Enums.TipoUsuarioEnum;
import com.tispucminas.sistemaezsnack.repository.UsuarioRepository;

@Service
public class UsuarioService {

    @Autowired
    @Lazy
    private AdminFactory adminFactory;
    @Autowired
    @Lazy
    private AdminEscolaFactory adminEscolaFactory;
    @Autowired
    @Lazy
    private AlunoFactory alunoFactory;
    @Autowired
    @Lazy
    private ResponsavelFactory responsavelFactory;

    @Autowired
    private UsuarioRepository usuarioRepository;
    

     public UsuarioResponseDTO login(String user, String senha) throws NoSuchAlgorithmException {
        Usuario usuario = usuarioRepository.findByUser(user);
        if (usuario != null && Arrays.equals(usuario.getSenha(), SenhaUtilService.criptografarSenha(senha))) {
            return UsuarioMapper.INSTANCE.toUsuarioResponse(usuario);
        }
        return null;
    }

    public UsuarioResponseDTO create(UsuarioCreateDTO usuarioCreateDTO) throws Exception {

        UsuarioFactory factory = getFactory(TipoUsuarioEnum.valueOf(usuarioCreateDTO.getTipo()));
        Usuario usuarioCriado = factory.createUsuario(usuarioCreateDTO);

        usuarioCriado.setSenha(SenhaUtilService.criptografarSenha(usuarioCreateDTO.getSenha()));

        Usuario usuarioSalvo = saveUsuario(usuarioCriado);

        return UsuarioMapper.INSTANCE.toUsuarioResponse(usuarioSalvo);
    }

    public void update(Long id, UsuarioUpdateDTO usuarioUpdateDTO) throws NotFoundException, NoSuchAlgorithmException, UnauthorizedException {
        Optional<Usuario> usuarioOptional = usuarioRepository.findById(id);

        if(!usuarioOptional.isPresent()){
            throw new NotFoundException("Usuário não encontrado");
        }

        Usuario usuario = usuarioOptional.get();
        SenhaUtilService.verificaSenhaAtual(usuario, usuarioUpdateDTO.getSenhaAtual());
        usuario.setSenha(SenhaUtilService.criptografarSenha(usuarioUpdateDTO.getNovaSenha()));
        saveUsuario(usuario);
    }

    public void delete(Long id) throws NotFoundException{
        Optional<Usuario> usuarioOptional = usuarioRepository.findById(id);

        if(!usuarioOptional.isPresent()){
            throw new NotFoundException("Usuário não encontrado");
        }

        Usuario usuario = usuarioOptional.get();
        usuarioRepository.delete(usuario);
    }

    public Usuario getById(Long id) throws NotFoundException{
        Optional<Usuario> usuarioOptional = usuarioRepository.findById(id);

        if(usuarioOptional.isPresent()){
            return usuarioOptional.get();
        }

        throw new NotFoundException("Usuário não encontrado");
    }

    public Usuario saveUsuario(Usuario usuario){
       return usuarioRepository.save(usuario);
    }

    private UsuarioFactory getFactory(TipoUsuarioEnum tipo) {
        switch (tipo) {
            case ADMIN:
                return adminFactory;
            case ADMIN_ESCOLA:
                return adminEscolaFactory;
            case ALUNO:
                return alunoFactory;
            case RESPONSAVEL:
                return responsavelFactory;
            default:
                return null;
        }
    }

}
