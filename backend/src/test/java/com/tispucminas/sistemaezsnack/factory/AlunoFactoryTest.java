package com.tispucminas.sistemaezsnack.factory;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

import java.security.NoSuchAlgorithmException;

import com.tispucminas.sistemaezsnack.dto.UsuarioCreateDTO;
import com.tispucminas.sistemaezsnack.exception.NotFoundException;
import com.tispucminas.sistemaezsnack.factory.impl.AlunoFactory;
import com.tispucminas.sistemaezsnack.model.Aluno;
import com.tispucminas.sistemaezsnack.model.Conta;
import com.tispucminas.sistemaezsnack.model.Escola;
import com.tispucminas.sistemaezsnack.model.Responsavel;
import com.tispucminas.sistemaezsnack.model.Usuario;
import com.tispucminas.sistemaezsnack.service.ContaService;
import com.tispucminas.sistemaezsnack.service.EscolaService;
import com.tispucminas.sistemaezsnack.service.SenhaUtilService;
import com.tispucminas.sistemaezsnack.service.UsuarioService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class AlunoFactoryTest {

    @InjectMocks
    private AlunoFactory alunoFactory;

    @Mock
    private EscolaService escolaService;

    @Mock
    private UsuarioService usuarioService;

    @Mock
    private ContaService contaService;

     @Test
    void createUsuario_withValidUsuarioCreateDTO_shouldReturnAluno() throws NotFoundException, NoSuchAlgorithmException {
        MockedStatic<SenhaUtilService> senhaUtilService = mockStatic(SenhaUtilService.class);
        UsuarioCreateDTO dto = new UsuarioCreateDTO();
        byte[] senhaCriptografada = new byte[10];
        dto.setUser("studentUser");
        dto.setNome("Student Name");
        dto.setIdentificacao("123456");
        dto.setEscolaId(1L);
        dto.setResponsavelId(2L);
        dto.setSenha("senha123");

        Escola escola = new Escola();
        escola.setId(1L);
        escola.setNome("Escola Teste");

        Responsavel responsavel = new Responsavel();
        responsavel.setId(2L);
        responsavel.setUser("responsavelUser");

        Conta conta = new Conta();
        conta.setId(3L);
        conta.setSaldo(new java.math.BigDecimal("100.00"));

        Aluno expectedAluno = new Aluno();
        expectedAluno.setNome(dto.getNome());
        expectedAluno.setUser(dto.getUser());
        expectedAluno.setMatricula(dto.getIdentificacao());
        expectedAluno.setResponsavel(responsavel);
        expectedAluno.setEscola(escola);
        expectedAluno.setConta(conta);

        when(escolaService.getEscolaById(dto.getEscolaId())).thenReturn(escola);
        when(usuarioService.getById(dto.getResponsavelId())).thenReturn(responsavel);
        when(contaService.create(any(Aluno.class))).thenReturn(conta);
        when(usuarioService.saveUsuario(any(Aluno.class))).thenReturn(expectedAluno);
        senhaUtilService.when(() -> SenhaUtilService.criptografarSenha(any())).thenReturn(senhaCriptografada);
    
        Usuario result = alunoFactory.createUsuario(dto);

        assertNotNull(result);
        assertTrue(result instanceof Aluno);
        Aluno aluno = (Aluno) result;
        assertEquals(dto.getUser(), aluno.getUser());
        assertEquals(dto.getNome(), aluno.getNome());
        assertEquals(dto.getIdentificacao(), aluno.getMatricula());
        assertEquals(responsavel, aluno.getResponsavel());
        assertEquals(escola, aluno.getEscola());
        assertEquals(conta, aluno.getConta());

        verify(escolaService, times(1)).getEscolaById(dto.getEscolaId());
        verify(usuarioService, times(1)).getById(dto.getResponsavelId());
        verify(contaService, times(1)).create(any(Aluno.class));
        senhaUtilService.close();
    }

    @Test
    void createUsuario_withNonExistentEscola_shouldThrowNotFoundException() throws NotFoundException {
        UsuarioCreateDTO dto = new UsuarioCreateDTO();
        dto.setUser("studentUser");
        dto.setNome("Student Name");
        dto.setIdentificacao("123456");
        dto.setEscolaId(1L);
        dto.setResponsavelId(2L);

        when(escolaService.getEscolaById(dto.getEscolaId())).thenThrow(new NotFoundException("Escola não encontrada"));

        assertThrows(NotFoundException.class, () -> {
            alunoFactory.createUsuario(dto);
        });

        verify(escolaService, times(1)).getEscolaById(dto.getEscolaId());
        verify(usuarioService, times(0)).getById(anyLong());
        verify(contaService, times(0)).create(any(Aluno.class));
    }

    @Test
    void createUsuario_withNonExistentResponsavel_shouldThrowNotFoundException() throws NotFoundException {
        UsuarioCreateDTO dto = new UsuarioCreateDTO();
        dto.setUser("studentUser");
        dto.setNome("Student Name");
        dto.setIdentificacao("123456");
        dto.setEscolaId(1L);
        dto.setResponsavelId(2L);

        Escola escola = new Escola();
        escola.setId(1L);
        escola.setNome("Escola Teste");

        when(escolaService.getEscolaById(dto.getEscolaId())).thenReturn(escola);
        when(usuarioService.getById(dto.getResponsavelId())).thenThrow(new NotFoundException("Responsavel não encontrado"));


        assertThrows(NotFoundException.class, () -> {
            alunoFactory.createUsuario(dto);
        });


        verify(escolaService, times(1)).getEscolaById(dto.getEscolaId());
        verify(usuarioService, times(1)).getById(dto.getResponsavelId());
        verify(contaService, times(0)).create(any(Aluno.class));
    }

    @Test
    void createUsuario_withNullUsuarioCreateDTO_shouldThrowNullPointerException() throws NotFoundException {
        UsuarioCreateDTO dto = null;

        assertThrows(NullPointerException.class, () -> {
            alunoFactory.createUsuario(dto);
        });

        verify(escolaService, times(0)).getEscolaById(anyLong());
        verify(usuarioService, times(0)).getById(anyLong());
        verify(contaService, times(0)).create(any(Aluno.class));
    }
}
