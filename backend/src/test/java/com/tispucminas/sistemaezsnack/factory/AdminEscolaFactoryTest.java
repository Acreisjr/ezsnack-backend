package com.tispucminas.sistemaezsnack.factory;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

import com.tispucminas.sistemaezsnack.dto.UsuarioCreateDTO;
import com.tispucminas.sistemaezsnack.exception.NotFoundException;
import com.tispucminas.sistemaezsnack.factory.impl.AdminEscolaFactory;
import com.tispucminas.sistemaezsnack.model.AdminEscola;
import com.tispucminas.sistemaezsnack.model.Escola;
import com.tispucminas.sistemaezsnack.model.Usuario;
import com.tispucminas.sistemaezsnack.service.EscolaService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class AdminEscolaFactoryTest {

    @InjectMocks
    private AdminEscolaFactory adminEscolaFactory;

    @Mock
    private EscolaService escolaService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createUsuario_withValidUsuarioCreateDTO_shouldReturnAdminEscola() throws NotFoundException {
        UsuarioCreateDTO dto = new UsuarioCreateDTO();
        dto.setUser("adminEscolaUser");
        dto.setEscolaId(1L);

        Escola escola = new Escola();
        escola.setId(1L);
        escola.setNome("Escola Teste");

        AdminEscola expectedAdminEscola = new AdminEscola();
        expectedAdminEscola.setUser(dto.getUser());
        expectedAdminEscola.setEscola(escola);

        when(escolaService.getEscolaById(dto.getEscolaId())).thenReturn(escola);

        Usuario result = adminEscolaFactory.createUsuario(dto);

        assertNotNull(result);
        assertTrue(result instanceof AdminEscola);
        AdminEscola adminEscola = (AdminEscola) result;
        assertEquals(dto.getUser(), adminEscola.getUser());
        assertEquals(escola, adminEscola.getEscola());

        verify(escolaService, times(1)).getEscolaById(dto.getEscolaId());
    }

    @Test
    void createUsuario_withNonExistentEscola_shouldReturnNull() throws NotFoundException {
        UsuarioCreateDTO dto = new UsuarioCreateDTO();
        dto.setUser("adminEscolaUser");
        dto.setEscolaId(2L);

        when(escolaService.getEscolaById(dto.getEscolaId())).thenThrow(new NotFoundException("Escola não encontrada"));

        Usuario result = adminEscolaFactory.createUsuario(dto);

        assertNull(result);
        verify(escolaService, times(1)).getEscolaById(dto.getEscolaId());
    }

    @Test
    void createUsuario_withNullUsuarioCreateDTO_shouldThrowNullPointerException() throws NotFoundException {
        UsuarioCreateDTO dto = null;

        assertThrows(NullPointerException.class, () -> {
            adminEscolaFactory.createUsuario(dto);
        });

        verify(escolaService, times(0)).getEscolaById(anyLong());
    }

    @Test
    void createUsuario_withNullUser_shouldReturnAdminEscolaWithNullUser() throws NotFoundException {
        UsuarioCreateDTO dto = new UsuarioCreateDTO();
        dto.setUser(null);
        dto.setEscolaId(1L);

        Escola escola = new Escola();
        escola.setId(1L);
        escola.setNome("Escola Teste");

        when(escolaService.getEscolaById(dto.getEscolaId())).thenReturn(escola);

        Usuario result = adminEscolaFactory.createUsuario(dto);

        assertNotNull(result);
        assertTrue(result instanceof AdminEscola);
        AdminEscola adminEscola = (AdminEscola) result;
        assertNull(adminEscola.getUser());
        assertEquals(escola, adminEscola.getEscola());

        verify(escolaService, times(1)).getEscolaById(dto.getEscolaId());
    }

    @Test
    void createUsuario_withNullEscolaId_shouldThrowIllegalArgumentException() throws NotFoundException {
        UsuarioCreateDTO dto = new UsuarioCreateDTO();
        dto.setUser("adminEscolaUser");
        dto.setEscolaId(null);

        when(escolaService.getEscolaById(dto.getEscolaId())).thenThrow(new IllegalArgumentException("Escola ID não pode ser null"));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            adminEscolaFactory.createUsuario(dto);
        });

        assertEquals("Escola ID não pode ser null", exception.getMessage());
        verify(escolaService, times(1)).getEscolaById(dto.getEscolaId());
    }
}
