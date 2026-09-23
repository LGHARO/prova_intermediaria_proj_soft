//package br.insper.avaliacao.controller;
//
//import br.insper.avaliacao.dto.AvaliacaoDTO;
//import br.insper.avaliacao.entity.Avaliacao;
//import br.insper.avaliacao.repository.AvaliacaoRepository;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
//import org.springframework.test.context.DynamicPropertyRegistry;
//import org.springframework.test.context.DynamicPropertySource;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.MvcResult;
//import org.testcontainers.containers.PostgreSQLContainer;
//import org.testcontainers.junit.jupiter.Container;
//import org.testcontainers.junit.jupiter.Testcontainers;
//import tools.jackson.databind.ObjectMapper;
//
//import java.time.LocalDate;
//import java.util.List;
//
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@SpringBootTest
//@AutoConfigureMockMvc
//@Testcontainers
//public class AvaliacaoControllerTests {
//
//    private static final String URL = "/avaliacao";
//
//    @Container
//    static PostgreSQLContainer<?> postgres =
//            new PostgreSQLContainer<>("postgres:15-alpine")
//                    .withDatabaseName("pagamento_test")
//                    .withUsername("test")
//                    .withPassword("test");
//
//    @DynamicPropertySource
//    static void configureProperties(
//            DynamicPropertyRegistry registry
//    ) {
//        registry.add(
//                "spring.datasource.url",
//                postgres::getJdbcUrl
//        );
//        registry.add(
//                "spring.datasource.username",
//                postgres::getUsername
//        );
//        registry.add(
//                "spring.datasource.password",
//                postgres::getPassword
//        );
//        registry.add(
//                "spring.jpa.hibernate.ddl-auto",
//                () -> "create-drop"
//        );
//    }
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    @Autowired
//    private AvaliacaoRepository avaliacaoRepository;
//
//    @BeforeEach
//    public void limparBanco() {
//        avaliacaoRepository.deleteAll();
//    }
//
//    @Test
//    public void test_shouldCreateCourse() throws Exception {
//
//        AvaliacaoDTO dto = new AvaliacaoDTO();
//        dto.setAutor("zambom");
//        dto.setNota(3);
//        dto.setConteudo("java");
//        dto.setDataAvaliacao(LocalDate.parse("2000-10-10"));
//
//        MvcResult result = mockMvc.perform(
//                        post(URL)
//                                .contentType("application/json")
//                                .content(
//                                        objectMapper.writeValueAsString(dto)
//                                )
//                )
//                .andExpect(status().isCreated())
//                .andReturn();
//
//        Avaliacao avaliacao = objectMapper.readValue(
//                result.getResponse().getContentAsString(),
//                Avaliacao.class
//        );
//
//        Assertions.assertNotNull(avaliacao.getId());
//        Assertions.assertEquals(
//                "Ciência da Computação",
//                avaliacao.getAutor()
//        );
//        Assertions.assertEquals(
//                3,
//                avaliacao.getNota()
//        );
//        Assertions.assertEquals(
//                "java",
//                avaliacao.getConteudo()
//        );
//
//    }
//
//}