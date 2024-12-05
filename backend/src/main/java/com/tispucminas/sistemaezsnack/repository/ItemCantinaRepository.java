package com.tispucminas.sistemaezsnack.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.tispucminas.sistemaezsnack.model.ItemEstoqueCantina;

public interface ItemCantinaRepository extends JpaRepository<ItemEstoqueCantina, Long> {
    List<ItemEstoqueCantina> findAllByEscolaId(Long escolaId);
}