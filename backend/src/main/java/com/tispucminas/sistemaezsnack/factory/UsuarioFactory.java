package com.tispucminas.sistemaezsnack.factory;

import com.tispucminas.sistemaezsnack.dto.UsuarioCreateDTO;
import com.tispucminas.sistemaezsnack.model.*;

public interface UsuarioFactory {
    Usuario createUsuario(UsuarioCreateDTO usuarioCreateDTO) throws Exception;
}
