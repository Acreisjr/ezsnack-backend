package com.tispucminas.sistemaezsnack.controller;

import com.tispucminas.sistemaezsnack.model.Conta;
import com.tispucminas.sistemaezsnack.service.ContaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.tispucminas.sistemaezsnack.dto.ContaResponseDTO;
import com.tispucminas.sistemaezsnack.dto.ContaUpdateResponsavelDTO;
import com.tispucminas.sistemaezsnack.dto.ContaUpdateSaldoDTO;
import com.tispucminas.sistemaezsnack.exception.NotFoundException;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/conta")
public class ContaController {

    @Autowired
    private ContaService contaService;

    @GetMapping
    public ResponseEntity<List<ContaResponseDTO>> findAll() {
        List<ContaResponseDTO> contas = contaService.findAll();
        return ResponseEntity.ok(contas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ContaResponseDTO> findByID(@PathVariable Long id) {
        try {
            ContaResponseDTO conta = contaService.getById(id);
            return ResponseEntity.ok(conta);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PutMapping("/update/responsavel/{id}")
    public ResponseEntity<Conta> updateResponsavel(@PathVariable Long id, @RequestBody ContaUpdateResponsavelDTO dto) {
        try{
            Conta contaUpdated = contaService.updateResponsavel(id, dto);
            return ResponseEntity.ok(contaUpdated);
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PutMapping("/aumentar/saldo/{id}")
    public ResponseEntity<Conta> aumentarSaldo(@PathVariable Long id, @RequestBody ContaUpdateSaldoDTO dto) {
        try{
            Conta contaUpdated = contaService.aumentarSaldo(id, dto);
            return ResponseEntity.ok(contaUpdated);
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PutMapping("/diminuir/saldo/{id}")
    public ResponseEntity<Conta> diminuirSaldo(@PathVariable Long id, @RequestBody ContaUpdateSaldoDTO dto) {
        try{
            Conta contaUpdated = contaService.diminuirSaldo(id, dto);
            return ResponseEntity.ok(contaUpdated);
        } catch(Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PutMapping("/limite/ativar/{id}")
    public ResponseEntity<Conta> setTemLimiteDiario(@PathVariable Long id, @RequestParam boolean temLimiteDiario) {
        try {
            Conta contaUpdated = contaService.setTemLimiteDiario(id, temLimiteDiario);
            return ResponseEntity.ok(contaUpdated);
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch(Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PutMapping("/limite/atualizar/{id}")
    public ResponseEntity<Conta> updateLimiteDiario(@PathVariable Long id, @RequestBody BigDecimal novoLimite) {
        try {
            Conta contaUpdated = contaService.updateLimiteDiario(id, novoLimite);
            return ResponseEntity.ok(contaUpdated);
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch(Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

}
