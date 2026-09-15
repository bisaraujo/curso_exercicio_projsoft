package com.insperedu.curso.service;

import com.insperedu.curso.Curso;
import com.insperedu.curso.repository.CursoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CursoService {
    private final CursoRepository cursoRepository;

    public CursoService(CursoRepository cursoRepository) {
        this.cursoRepository = cursoRepository;
    }

    public Curso criar(Curso curso){
        curso.setDeletado(false);
        return cursoRepository.save(curso);
    }

    public List<Curso> listar(String nome){
        if(nome == null || nome.isBlank()){
            return cursoRepository.findByDeletadoFalse();
        }

        return cursoRepository.findByNomeStartingWithAndDeletadoFalse(nome);
    }

    public void deletar(Long id){
        Curso curso = cursoRepository.findById(id).orElse(null);
        curso.setDeletado(true);
        cursoRepository.save(curso);
    }
}
