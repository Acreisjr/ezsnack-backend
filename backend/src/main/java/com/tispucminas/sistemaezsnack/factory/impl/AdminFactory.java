package com.tispucminas.sistemaezsnack.factory.impl;

import org.springframework.stereotype.Component;
import com.tispucminas.sistemaezsnack.dto.UsuarioCreateDTO;
import com.tispucminas.sistemaezsnack.factory.UsuarioFactory;
import com.tispucminas.sistemaezsnack.model.Admin;
import com.tispucminas.sistemaezsnack.model.Usuario;

@Component
public class AdminFactory implements UsuarioFactory {
    public Usuario createUsuario(UsuarioCreateDTO usuarioCreateDTO) {
        Admin admin = new Admin();
        admin.setUser(usuarioCreateDTO.getUser());
        return admin;
    }
}