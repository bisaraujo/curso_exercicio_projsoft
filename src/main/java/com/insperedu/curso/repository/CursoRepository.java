package com.insperedu.curso.repository;

import com.insperedu.curso.Curso;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CursoRepository extends JpaRepository<Curso, Long> {
    List<Curso> findByDeletadoFalse();

    List<Curso> findByNomeStartingWithAndDeletadoFalse(String nome);
}
