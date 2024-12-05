package com.tispucminas.sistemaezsnack.repository;

import com.tispucminas.sistemaezsnack.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Usuario findByUser(@Param("user") String user);
}
