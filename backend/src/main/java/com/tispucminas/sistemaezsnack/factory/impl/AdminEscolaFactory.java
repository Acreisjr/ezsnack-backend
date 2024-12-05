package com.tispucminas.sistemaezsnack.factory.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import com.tispucminas.sistemaezsnack.dto.UsuarioCreateDTO;
import com.tispucminas.sistemaezsnack.exception.NotFoundException;
import com.tispucminas.sistemaezsnack.factory.UsuarioFactory;
import com.tispucminas.sistemaezsnack.model.AdminEscola;
import com.tispucminas.sistemaezsnack.model.Escola;
import com.tispucminas.sistemaezsnack.model.Usuario;
import com.tispucminas.sistemaezsnack.service.EscolaService;

@Component
@DependsOn("escolaService")
public class AdminEscolaFactory implements UsuarioFactory {
    
    @Autowired
    private EscolaService escolaService;

    public Usuario createUsuario(UsuarioCreateDTO usuarioCreateDTO) {
        
        try {
            AdminEscola adminEscola = new AdminEscola();
            Escola escola = escolaService.getEscolaById(usuarioCreateDTO.getEscolaId());
            adminEscola.setUser(usuarioCreateDTO.getUser());
            adminEscola.setEscola(escola);
            return adminEscola;
        } catch (NotFoundException e) {
            return null;
        }
        
    }
}