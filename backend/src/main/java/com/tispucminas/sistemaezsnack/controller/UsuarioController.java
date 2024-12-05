package com.tispucminas.sistemaezsnack.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tispucminas.sistemaezsnack.dto.ErrorResponseDTO;
import com.tispucminas.sistemaezsnack.dto.LoginDTO;
import com.tispucminas.sistemaezsnack.dto.UsuarioCreateDTO;
import com.tispucminas.sistemaezsnack.dto.UsuarioResponseDTO;
import com.tispucminas.sistemaezsnack.dto.UsuarioUpdateDTO;
import com.tispucminas.sistemaezsnack.exception.NotFoundException;
import com.tispucminas.sistemaezsnack.exception.UnauthorizedException;
import com.tispucminas.sistemaezsnack.service.UsuarioService;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody LoginDTO credenciais) {
        try {
            UsuarioResponseDTO usuario;
            usuario = usuarioService.login(credenciais.getUser(), credenciais.getSenha());
            if (usuario != null) {
                return ResponseEntity.ok(usuario);
            }
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponseDTO("Erro interno do servidor!"));
        }
    }

    @Validated
    @PostMapping("/create")
    public <T> ResponseEntity<Object> create(@RequestBody UsuarioCreateDTO usuarioCreateDTO){
        try {
           UsuarioResponseDTO usuario = usuarioService.create(usuarioCreateDTO);
           return ResponseEntity.ok(usuario);
        } catch (NotFoundException e) {
             return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponseDTO(e.getMessage()));
        }catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponseDTO("Nome de usuário indisponível!"));
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponseDTO(e.getMessage()));
        }
    }

    @PutMapping("/update/{id}")
    public <T> ResponseEntity<Object> update(@PathVariable Long id, @RequestBody UsuarioUpdateDTO usuarioUpdateDTO){
        try {
           usuarioService.update(id, usuarioUpdateDTO);
           return ResponseEntity.ok().build();
        }catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponseDTO(e.getMessage()));
        }catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponseDTO(e.getMessage()));
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponseDTO("Erro interno do servidor!"));
        }
    }

    @DeleteMapping("/delete/{id}")
    public <T> ResponseEntity<Object> delete(@PathVariable Long id){
        try {
           usuarioService.delete(id);
           return ResponseEntity.noContent().build();
        }catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponseDTO(e.getMessage()));
        }
    }
}