package com.tispucminas.sistemaezsnack.factory;

import static org.junit.jupiter.api.Assertions.*;
import com.tispucminas.sistemaezsnack.dto.UsuarioCreateDTO;
import com.tispucminas.sistemaezsnack.factory.impl.ResponsavelFactory;
import com.tispucminas.sistemaezsnack.model.Aluno;
import com.tispucminas.sistemaezsnack.model.Responsavel;
import com.tispucminas.sistemaezsnack.model.Usuario;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;

@ExtendWith(MockitoExtension.class)
public class ResponsavelFactoryTest {

    @InjectMocks
    private ResponsavelFactory responsavelFactory;

    @Test
    void createUsuario_withValidUsuarioCreateDTO_shouldReturnResponsavel() throws Exception {
        UsuarioCreateDTO dto = new UsuarioCreateDTO();
        dto.setUser("responsavelUser");
        dto.setNome("Responsavel Name");
        dto.setIdentificacao("987654321");

        Responsavel expectedResponsavel = new Responsavel();
        expectedResponsavel.setUser(dto.getUser());
        expectedResponsavel.setNome(dto.getNome());
        expectedResponsavel.setCpf(dto.getIdentificacao());
        expectedResponsavel.setAlunos(new ArrayList<Aluno>());

        Usuario result = responsavelFactory.createUsuario(dto);

        assertNotNull(result);
        assertTrue(result instanceof Responsavel);
        Responsavel responsavel = (Responsavel) result;
        assertEquals(dto.getUser(), responsavel.getUser());
        assertEquals(dto.getNome(), responsavel.getNome());
        assertEquals(dto.getIdentificacao(), responsavel.getCpf());
        assertNotNull(responsavel.getAlunos());
        assertTrue(responsavel.getAlunos().isEmpty());
    }

    @Test
    void createUsuario_withNullUsuarioCreateDTO_shouldThrowNullPointerException() {
        UsuarioCreateDTO dto = null;

        assertThrows(NullPointerException.class, () -> {
            responsavelFactory.createUsuario(dto);
        });
    }

    @Test
    void createUsuario_withNullFields_shouldReturnResponsavelWithNullFields() throws Exception {
        UsuarioCreateDTO dto = new UsuarioCreateDTO();
        dto.setUser(null);
        dto.setNome(null);
        dto.setIdentificacao(null);

        Usuario result = responsavelFactory.createUsuario(dto);

        assertNotNull(result);
        assertTrue(result instanceof Responsavel);
        Responsavel responsavel = (Responsavel) result;
        assertNull(responsavel.getUser());
        assertNull(responsavel.getNome());
        assertNull(responsavel.getCpf());
        assertNotNull(responsavel.getAlunos());
        assertTrue(responsavel.getAlunos().isEmpty());
    }
}
