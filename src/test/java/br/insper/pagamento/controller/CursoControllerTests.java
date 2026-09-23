package br.insper.pagamento.controller;

import br.insper.pagamento.dto.CursoDTO;
import br.insper.pagamento.entity.Curso;
import br.insper.pagamento.repository.CursoRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
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

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
public class CursoControllerTests {

    private static final String URL = "/cursos";

    @Container
    static PostgreSQLContainer<?> postgres =
            new PostgreSQLContainer<>("postgres:15-alpine")
                    .withDatabaseName("pagamento_test")
                    .withUsername("test")
                    .withPassword("test");

    @DynamicPropertySource
    static void configureProperties(
            DynamicPropertyRegistry registry
    ) {
        registry.add(
                "spring.datasource.url",
                postgres::getJdbcUrl
        );
        registry.add(
                "spring.datasource.username",
                postgres::getUsername
        );
        registry.add(
                "spring.datasource.password",
                postgres::getPassword
        );
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CursoRepository cursoRepository;

    @BeforeEach
    public void limparBanco() {
        cursoRepository.deleteAll();
    }

    @Test
    public void test_shouldCreateCourse() throws Exception {

        CursoDTO dto = new CursoDTO();
        dto.setNome("Ciência da Computação");
        dto.setQuantidade_alunos(40);
        dto.setMaterias(List.of(
                "Programação",
                "Banco de Dados"
        ));

        MvcResult result = mockMvc.perform(
                        post(URL)
                                .contentType("application/json")
                                .content(
                                        objectMapper.writeValueAsString(dto)
                                )
                )
                .andExpect(status().isCreated())
                .andReturn();

        Curso curso = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                Curso.class
        );

        Assertions.assertNotNull(curso.getId());
        Assertions.assertEquals(
                "Ciência da Computação",
                curso.getNome()
        );
        Assertions.assertEquals(
                40,
                curso.getQuantidadeAlunos()
        );
        Assertions.assertEquals(
                2,
                curso.getMaterias().size()
        );
        Assertions.assertFalse(curso.isDeletado());
    }

    @Test
    public void test_shouldListOnlyNotDeletedCourses() throws Exception {

        Curso ativo = criarCurso(
                "Ciência da Computação",
                false
        );

        Curso deletado = criarCurso(
                "Administração",
                true
        );

        cursoRepository.saveAll(List.of(ativo, deletado));

        mockMvc.perform(get(URL))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(
                        jsonPath("$[0].nome")
                                .value("Ciência da Computação")
                )
                .andExpect(
                        jsonPath("$[0].deletado")
                                .value(false)
                );
    }

    @Test
    public void test_shouldFilterCoursesStartingWithName()
            throws Exception {

        cursoRepository.save(
                criarCurso(
                        "Engenharia de Computação",
                        false
                )
        );

        cursoRepository.save(
                criarCurso(
                        "Engenharia Mecânica",
                        false
                )
        );

        cursoRepository.save(
                criarCurso(
                        "Ciência da Computação",
                        false
                )
        );

        mockMvc.perform(
                        get(URL)
                                .param("nome", "Eng")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(
                        jsonPath("$[0].nome")
                                .value(
                                        org.hamcrest.Matchers
                                                .startsWith("Eng")
                                )
                )
                .andExpect(
                        jsonPath("$[1].nome")
                                .value(
                                        org.hamcrest.Matchers
                                                .startsWith("Eng")
                                )
                );
    }

    @Test
    public void test_shouldIgnoreDeletedCourseWhenFiltering()
            throws Exception {

        cursoRepository.save(
                criarCurso(
                        "Engenharia de Computação",
                        false
                )
        );

        cursoRepository.save(
                criarCurso(
                        "Engenharia Mecânica",
                        true
                )
        );

        mockMvc.perform(
                        get(URL)
                                .param("nome", "Eng")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(
                        jsonPath("$[0].nome")
                                .value("Engenharia de Computação")
                );
    }

    @Test
    public void test_shouldLogicallyDeleteCourse()
            throws Exception {

        Curso curso = cursoRepository.save(
                criarCurso(
                        "Ciência da Computação",
                        false
                )
        );

        mockMvc.perform(
                        delete(URL + "/{id}", curso.getId())
                )
                .andExpect(status().isNoContent());

        Curso cursoAtualizado = cursoRepository
                .findById(curso.getId())
                .orElseThrow();

        Assertions.assertTrue(cursoAtualizado.isDeletado());

        // O registro continua fisicamente no banco.
        Assertions.assertTrue(
                cursoRepository.existsById(curso.getId())
        );
    }

    @Test
    public void test_shouldNotListCourseAfterDelete()
            throws Exception {

        Curso curso = cursoRepository.save(
                criarCurso(
                        "Ciência da Computação",
                        false
                )
        );

        mockMvc.perform(
                        delete(URL + "/{id}", curso.getId())
                )
                .andExpect(status().isNoContent());

        mockMvc.perform(get(URL))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    public void test_shouldReturnNotFoundWhenDeletingUnknownCourse()
            throws Exception {

        mockMvc.perform(
                        delete(URL + "/{id}", 99999L)
                )
                .andExpect(status().isNotFound());
    }

    private Curso criarCurso(
            String nome,
            boolean deletado
    ) {
        Curso curso = new Curso();

        curso.setNome(nome);
        curso.setQuantidadeAlunos(30);
        curso.setMaterias(List.of(
                "Programação",
                "Matemática"
        ));
        curso.setDeletado(deletado);

        return curso;
    }
}