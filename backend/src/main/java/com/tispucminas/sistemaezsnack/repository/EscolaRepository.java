package com.tispucminas.sistemaezsnack.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tispucminas.sistemaezsnack.model.Escola;

@Repository
public interface EscolaRepository extends JpaRepository<Escola, Long> {
}