package com.tispucminas.sistemaezsnack.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.tispucminas.sistemaezsnack.model.Pedido;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    
    @Query("SELECT p FROM Pedido p WHERE p.conta.aluno.escola.id = :escolaId")
    List<Pedido> findByEscolaId(Long escolaId);

    @Query("SELECT p FROM Pedido p WHERE p.conta.aluno.escola.id = :escolaId AND p.conta.aluno.id = :alunoId")
    List<Pedido> findByEscolaIdAndAlunoId(Long escolaId, Long alunoId);
}