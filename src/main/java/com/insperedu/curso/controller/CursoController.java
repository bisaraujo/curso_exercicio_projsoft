package com.insperedu.curso.controller;

import com.insperedu.curso.Curso;
import com.insperedu.curso.service.CursoService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cursos")
public class CursoController {
    private final CursoService cursoService;

    public CursoController(CursoService cursoService) {
        this.cursoService = cursoService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Curso criar(@RequestBody Curso curso) {
        return cursoService.criar(curso);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Curso> listar(@RequestParam(required=false) String nome) {
        return cursoService.listar(nome);
    }


}
