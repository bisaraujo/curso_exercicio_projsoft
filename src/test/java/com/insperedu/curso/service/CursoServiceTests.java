package com.insperedu.service;

import com.insperedu.curso.Curso;
import com.insperedu.curso.repository.CursoRepository;
import com.insperedu.curso.service.CursoService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class CursoServiceTests {

	@InjectMocks
	private CursoService cursoService;

	@Mock
	private CursoRepository cursoRepository;

	@Test
	public void test_shouldCreateCurso() {
		Curso curso = new Curso();
		curso.setNome("Spring Boot");
		curso.setDescricao("Curso de Spring Boot");

		Mockito.when(cursoRepository.save(Mockito.any())).thenReturn(curso);

		Curso response = cursoService.criar(curso);

		Assertions.assertEquals("Spring Boot", response.getNome());
		Assertions.assertEquals("Curso de Spring Boot", response.getDescricao());
		Assertions.assertFalse(response.isDeletado());
	}

	@Test
	public void test_shouldListCursoWithoutFilter(){
		Curso curso1 = new Curso();
		curso1.setNome("Spring Boot");

		Curso curso2 = new Curso();
		curso2.setNome("Java");
		List<Curso> cursos = List.of(curso1, curso2);

		Mockito.when(cursoRepository.findByDeletadoFalse()).thenReturn(cursos);

		List<Curso> response = cursoService.listar(null);

		Assertions.assertEquals(2, response.size());
		Assertions.assertEquals("Spring Boot", response.get(0).getNome());
		Assertions.assertEquals("Java", response.get(1).getNome());
	}

	@Test
	public void test_shouldListCursoWithFilter(){
		Curso curso = new Curso();
		curso.setNome("Spring Boot");

		List<Curso> cursos = List.of(curso);

		Mockito.when(cursoRepository.findByNomeStartingWithAndDeletadoFalse("Spr")).thenReturn(cursos);
		List<Curso> response = cursoService.listar("Spr");

		Assertions.assertEquals(1, response.size());
		Assertions.assertEquals("Spring Boot", response.get(0).getNome());

	}

	@Test
	public void test_shouldListCursoFilterIsBlank(){
		Curso curso = new Curso();
		curso.setNome("Spring Boot");

		List<Curso> cursos = List.of(curso);

		Mockito.when(cursoRepository.findByDeletadoFalse()).thenReturn(cursos);
		List<Curso> response = cursoService.listar("");

		Assertions.assertEquals(1, response.size());
		Assertions.assertEquals("Spring Boot", response.get(0).getNome());
	}

	@Test
	public void test_shouldDeleteCurso(){
		Curso curso = new Curso();

		curso.setId(1L);
		curso.setNome("Spring Boot");
		curso.setDeletado(false);

		Mockito.when(cursoRepository.findById(1L)).thenReturn(Optional.of(curso));

		cursoService.deletar(curso.getId());

		Assertions.assertTrue(curso.isDeletado());

		Mockito.verify(cursoRepository).save(curso);

	}

	@Test
	public void test_shouldThrowWhenCursoDoesntExist(){
		Mockito.when(cursoRepository.findById(99L)).thenReturn(Optional.empty());

		Assertions.assertThrows(
				RuntimeException.class,
				() -> cursoService.deletar(99L)
		);
	}


}