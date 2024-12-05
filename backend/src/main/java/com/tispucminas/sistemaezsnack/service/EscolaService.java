package com.tispucminas.sistemaezsnack.service;

import com.tispucminas.sistemaezsnack.dto.EscolaCreateDTO;
import com.tispucminas.sistemaezsnack.dto.EscolaUpdateDTO;
import com.tispucminas.sistemaezsnack.exception.NotFoundException;
import com.tispucminas.sistemaezsnack.model.Escola;
import com.tispucminas.sistemaezsnack.repository.EscolaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EscolaService {

    @Autowired
    private EscolaRepository escolaRepository;

    public List<Escola> findAll() {
        return escolaRepository.findAll();
    }

    public Escola getEscolaById(Long id) throws NotFoundException {
        return escolaRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("Escola não encontrada"));
    }

    public Escola create(EscolaCreateDTO dto) {
        Escola escola = new Escola();
    
        escola.setNome(dto.getNome());
        escola.setCnpj(dto.getCnpj());
        escola.setTelefone(dto.getTelefone());
        escola.setEstado(dto.getEstado());
        escola.setCidade(dto.getCidade());

        return escolaRepository.save(escola);
    }

    public Escola update(Long id, EscolaUpdateDTO dto) throws Exception {
        Escola escola = escolaRepository.findById(id)
            .orElseThrow(() -> new Exception("Escola não encontrada"));

        escola.setTelefone(dto.getTelefone());
        return escolaRepository.save(escola);
    }

    public void delete(Long id) throws Exception {
        Escola escola = escolaRepository.findById(id)
            .orElseThrow(() -> new Exception("Escola não encontrada"));
        escolaRepository.delete(escola);
    }
}
