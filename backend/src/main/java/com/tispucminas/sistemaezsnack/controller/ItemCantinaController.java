package com.tispucminas.sistemaezsnack.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import com.tispucminas.sistemaezsnack.dto.ItemCantinaCreateDTO;
import com.tispucminas.sistemaezsnack.dto.ItemCantinaUpdateDTO;
import com.tispucminas.sistemaezsnack.model.ItemEstoqueCantina;
import com.tispucminas.sistemaezsnack.service.ItemCantinaService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/itemcantina")
@RequiredArgsConstructor
public class ItemCantinaController {

    private final ItemCantinaService itemCantinaService;

    @GetMapping
    public ResponseEntity<List<ItemEstoqueCantina>> findAll(@RequestParam(value = "escolaId", required = false) Long escolaId) {
        
        List<ItemEstoqueCantina> itensCantina = itemCantinaService.findAll(escolaId);
        
        return ResponseEntity.ok(itensCantina);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ItemEstoqueCantina> findById(@PathVariable Long id) {
        ItemEstoqueCantina itemCantina = itemCantinaService.findById(id);
        return ResponseEntity.ok(itemCantina);
    }

    @PostMapping
    public ResponseEntity<ItemEstoqueCantina> create(@RequestBody ItemCantinaCreateDTO dto) {
        ItemEstoqueCantina itemCantina = itemCantinaService.create(dto);
        return ResponseEntity.ok(itemCantina);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ItemEstoqueCantina> update(@PathVariable Long id, @RequestBody ItemCantinaUpdateDTO dto) {
            ItemEstoqueCantina itemCantinaUpdated = itemCantinaService.update(id, dto);
            return ResponseEntity.ok(itemCantinaUpdated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
            itemCantinaService.delete(id);
            return ResponseEntity.noContent().build(); 
    }
    
}