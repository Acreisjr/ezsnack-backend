package com.tispucminas.sistemaezsnack.factory;

import static org.junit.jupiter.api.Assertions.*;
import com.tispucminas.sistemaezsnack.dto.UsuarioCreateDTO;
import com.tispucminas.sistemaezsnack.factory.impl.AdminFactory;
import com.tispucminas.sistemaezsnack.model.Admin;
import com.tispucminas.sistemaezsnack.model.Usuario;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class AdminFactoryTest {

    @InjectMocks
    private AdminFactory adminFactory;

    @Test
    void createUsuario_withValidUsuarioCreateDTO_shouldReturnAdmin() {
        UsuarioCreateDTO dto = new UsuarioCreateDTO();
        dto.setUser("adminUser");

        Admin expectedAdmin = new Admin();
        expectedAdmin.setUser(dto.getUser());

        Usuario result = adminFactory.createUsuario(dto);

        assertNotNull(result);
        assertTrue(result instanceof Admin);
        Admin admin = (Admin) result;
        assertEquals(dto.getUser(), admin.getUser());
    }

    @Test
    void createUsuario_withNullUsuarioCreateDTO_shouldThrowNullPointerException() {
        UsuarioCreateDTO dto = null;

        assertThrows(NullPointerException.class, () -> {
            adminFactory.createUsuario(dto);
        });
    }

    @Test
    void createUsuario_withNullUser_shouldReturnAdminWithNullUser() {
        UsuarioCreateDTO dto = new UsuarioCreateDTO();
        dto.setUser(null);

        Usuario result = adminFactory.createUsuario(dto);

        assertNotNull(result);
        assertTrue(result instanceof Admin);
        Admin admin = (Admin) result;
        assertNull(admin.getUser());
    }
}
