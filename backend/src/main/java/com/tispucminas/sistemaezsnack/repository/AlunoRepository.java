package com.tispucminas.sistemaezsnack.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tispucminas.sistemaezsnack.model.Aluno;

@Repository
public interface AlunoRepository extends JpaRepository<Aluno, Long>{
    @Query("SELECT a FROM Aluno a LEFT JOIN FETCH a.conta WHERE a.user = :user")
    Aluno findByUser(@Param("user") String user);
}
