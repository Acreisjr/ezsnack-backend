    package com.tispucminas.sistemaezsnack.controller;

    import com.tispucminas.sistemaezsnack.model.Escola;
    import com.tispucminas.sistemaezsnack.service.EscolaService;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.http.HttpStatus;
    import org.springframework.http.ResponseEntity;
    import org.springframework.web.bind.annotation.*;
    import com.tispucminas.sistemaezsnack.dto.EscolaCreateDTO;
    import com.tispucminas.sistemaezsnack.dto.EscolaUpdateDTO;
    import java.util.List;

    @RestController
    @RequestMapping("/escola")
    public class EscolaController {

        @Autowired
        private EscolaService escolaService;

        @GetMapping
        public ResponseEntity<List<Escola>> findAll() {
            List<Escola> escolas = escolaService.findAll();
            return ResponseEntity.ok(escolas);
        }

        @GetMapping("/{id}")
        public ResponseEntity<Escola> findByID(@PathVariable Long id) {
            try {
                Escola escola = escolaService.getEscolaById(id);
                return ResponseEntity.ok(escola);
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
        }

        @PostMapping
        public ResponseEntity<Escola> create(@RequestBody EscolaCreateDTO dto) {
                Escola escola = escolaService.create(dto);
                return ResponseEntity.status(HttpStatus.CREATED).body(escola);
        }

        @PutMapping("/{id}")
        public ResponseEntity<Escola> update(@PathVariable Long id, @RequestBody EscolaUpdateDTO dto) {
            try{
                Escola escolaUpdated = escolaService.update(id, dto);
                return ResponseEntity.ok(escolaUpdated);
            }catch(Exception e){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
        }

        @DeleteMapping("/{id}")
        public void delete(@PathVariable Long id) {
            try{
                escolaService.delete(id);
            }catch(Exception e){
                System.out.println("Erro: " + e.getMessage());
            }
        }
    }
