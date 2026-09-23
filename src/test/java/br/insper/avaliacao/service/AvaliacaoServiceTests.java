package br.insper.avaliacao.service;

import br.insper.avaliacao.dto.AvaliacaoDTO;
import br.insper.avaliacao.entity.Avaliacao;
import br.insper.avaliacao.repository.AvaliacaoRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class AvaliacaoServiceTests {

    @InjectMocks
    private AvaliacaoService avaliacaoService;

    @Mock
    private AvaliacaoRepository avaliacaoRepository;

    @Test
    public void test_shouldReturnTwoAvaliacaoWhenListar() {

        //mocks
        List<Avaliacao> avaliacaos = List.of(
                new Avaliacao(),
                new Avaliacao()
        );

        Mockito.when(avaliacaoRepository.findAll())
                .thenReturn(avaliacaos);

        //chamada
        List<Avaliacao> response = avaliacaoService.listar();

        Assertions.assertEquals(2, response.size());

    }

    @Test
    public void test_shouldCreateCourse() {

        AvaliacaoDTO dto = new AvaliacaoDTO();
        dto.setAutor("Eduardo Zambom");
        dto.setNota(3);
        dto.setConteudo("java");
        dto.setDataAvaliacao(LocalDate.parse("2000-10-10"));

        Avaliacao avaliacao = new Avaliacao();
        avaliacao.setId(1L);
        avaliacao.setAutor("Eduardo Zambom");
        avaliacao.setNota(3);
        avaliacao.setConteudo("java");
        avaliacao.setDataAvaliacao(LocalDate.parse("2000-10-10"));

        Mockito.when(avaliacaoRepository.save(Mockito.any(Avaliacao.class)))
                .thenReturn(avaliacao);

        Avaliacao response = avaliacaoService.criar(dto);

        Assertions.assertEquals(1L, response.getId());
        Assertions.assertEquals("Eduardo Zambom", response.getAutor());
        Assertions.assertEquals(3, response.getNota());
        Assertions.assertEquals("java", response.getConteudo());
        Assertions.assertEquals(
                LocalDate.parse("2000-10-10"),
                response.getDataAvaliacao()
        );

    }



    @Test
    public void test_shouldReturnCourseWhenGet() {

        Avaliacao avaliacao = new Avaliacao();
        avaliacao.setId(1L);
        avaliacao.setAutor("Eduardo Zambom");
        avaliacao.setNota(3);
        avaliacao.setConteudo("java");
        avaliacao.setDataAvaliacao(LocalDate.parse("2000-10-10"));
        Mockito.when(avaliacaoRepository.findById(1L))
                .thenReturn(avaliacao);

        Avaliacao response = avaliacaoService.get(1L);

        Assertions.assertEquals("Eduardo Zambom",response.getAutor());
        Assertions.assertEquals(3, response.getNota());
        Assertions.assertEquals("java", response.getConteudo());
        Assertions.assertEquals(LocalDate.parse("2000-10-10"), response.getDataAvaliacao());
}



}