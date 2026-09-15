package com.insperedu.curso.controller;
import com.insperedu.curso.repository.CursoRepository;
import org.junit.jupiter.api.Assertions;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import com.insperedu.curso.Curso;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import tools.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
public class CursoControllerTests {

    @Container
    static PostgreSQLContainer<?> postgres =
            new PostgreSQLContainer<>("postgres:16-alpine")
                    .withDatabaseName("curso_test")
                    .withUsername("test")
                    .withPassword("test");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CursoRepository cursoRepository;

    @Test
    public void test_shouldCreateCurso() throws Exception {
        Curso curso = new Curso();
        curso.setNome("Spring Boot");
        curso.setDescricao("Curso de Spring Boot");

        MvcResult result = mockMvc.perform(
                        post("/cursos")
                                .contentType("application/json")
                                .content(objectMapper.writeValueAsString(curso))
                )
                .andExpect(status().isCreated())
                .andReturn();

        Curso response = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                Curso.class
        );

        // asserts
        Assertions.assertNotNull(response.getId());
        Assertions.assertEquals("Spring Boot", response.getNome());
        Assertions.assertEquals("Curso de Spring Boot", response.getDescricao());
        Assertions.assertFalse(response.isDeletado());
    }

    @Test
    public void test_shouldListCurso() throws Exception {
        Curso curso = new Curso();
        curso.setNome("Spring Boot");
        curso.setDescricao("Curso de Spring Boot");
        curso.setDeletado(false);

        // prepara o banco
        cursoRepository.save(curso);

        // chamada
        MvcResult result = mockMvc.perform(
                        get("/cursos")
                )
                .andExpect(status().isOk())
                .andReturn();

        // resposta do GET é uma LISTA
        Curso[] response = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                Curso[].class
        );

        // asserts
        Assertions.assertTrue(response.length > 0);
        Assertions.assertEquals("Spring Boot", response[0].getNome());
        Assertions.assertFalse(response[0].isDeletado());
    }
    @Test
    public void test_shouldDeleteCurso() throws Exception {

        Curso curso = new Curso();
        curso.setNome("Spring Boot");
        curso.setDescricao("Curso de Spring Boot");
        curso.setDeletado(false);

        curso = cursoRepository.save(curso);

        mockMvc.perform(
                        delete("/cursos/{id}", curso.getId())
                )
                .andExpect(status().isNoContent());

        Curso cursoDeletado = cursoRepository
                .findById(curso.getId())
                .orElseThrow();

        Assertions.assertTrue(cursoDeletado.isDeletado());
    }





}