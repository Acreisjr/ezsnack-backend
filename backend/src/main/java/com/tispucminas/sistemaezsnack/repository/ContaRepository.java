package com.tispucminas.sistemaezsnack.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.tispucminas.sistemaezsnack.model.Conta;

public interface ContaRepository extends JpaRepository<Conta, Long> {
    
}