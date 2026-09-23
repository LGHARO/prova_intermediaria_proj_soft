package br.insper.pagamento.service;

import br.insper.pagamento.dto.CursoDTO;
import br.insper.pagamento.entity.Curso;
import br.insper.pagamento.repository.CursoRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class CursoServiceTests {

    @InjectMocks
    private CursoService cursoService;

    @Mock
    private CursoRepository cursoRepository;

    @Test
    public void test_shouldReturnTwoCoursesWhenListarWithoutFilter() {

        List<Curso> cursos = List.of(
                new Curso(),
                new Curso()
        );

        Mockito.when(cursoRepository.findByDeletadoFalse())
                .thenReturn(cursos);

        List<Curso> response = cursoService.listar(null);

        Assertions.assertEquals(2, response.size());

        Mockito.verify(cursoRepository).findByDeletadoFalse();
        Mockito.verify(
                cursoRepository,
                Mockito.never()
        ).findByDeletadoFalseAndNomeStartingWithIgnoreCase(
                Mockito.anyString()
        );
    }

    @Test
    public void test_shouldReturnCoursesStartingWithName() {

        Curso curso = new Curso();
        curso.setNome("Engenharia de Computação");

        Mockito.when(
                cursoRepository
                        .findByDeletadoFalseAndNomeStartingWithIgnoreCase("Eng")
        ).thenReturn(List.of(curso));

        List<Curso> response = cursoService.listar("Eng");

        Assertions.assertEquals(1, response.size());
        Assertions.assertEquals(
                "Engenharia de Computação",
                response.get(0).getNome()
        );

        Mockito.verify(cursoRepository)
                .findByDeletadoFalseAndNomeStartingWithIgnoreCase("Eng");
    }

    @Test
    public void test_shouldListAllCoursesWhenFilterIsBlank() {

        Mockito.when(cursoRepository.findByDeletadoFalse())
                .thenReturn(List.of());

        List<Curso> response = cursoService.listar(" ");

        Assertions.assertTrue(response.isEmpty());
        Mockito.verify(cursoRepository).findByDeletadoFalse();
    }

    @Test
    public void test_shouldCreateCourse() {

        CursoDTO dto = new CursoDTO();
        dto.setNome("Ciência da Computação");
        dto.setQuantidade_alunos(40);
        dto.setMaterias(List.of(
                "Programação",
                "Banco de Dados"
        ));

        Mockito.when(cursoRepository.save(Mockito.any(Curso.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Curso response = cursoService.criar(dto);

        Assertions.assertEquals(
                "Ciência da Computação",
                response.getNome()
        );
        Assertions.assertEquals(40, response.getQuantidadeAlunos());
        Assertions.assertEquals(2, response.getMaterias().size());
        Assertions.assertFalse(response.isDeletado());

        Mockito.verify(cursoRepository)
                .save(Mockito.any(Curso.class));
    }

    @Test
    public void test_shouldMapDtoBeforeSavingCourse() {

        CursoDTO dto = new CursoDTO();
        dto.setNome("Engenharia");
        dto.setQuantidade_alunos(30);
        dto.setMaterias(List.of("Cálculo"));

        Mockito.when(cursoRepository.save(Mockito.any(Curso.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        cursoService.criar(dto);

        ArgumentCaptor<Curso> captor =
                ArgumentCaptor.forClass(Curso.class);

        Mockito.verify(cursoRepository).save(captor.capture());

        Curso cursoSalvo = captor.getValue();

        Assertions.assertEquals("Engenharia", cursoSalvo.getNome());
        Assertions.assertEquals(30, cursoSalvo.getQuantidadeAlunos());
        Assertions.assertEquals(
                List.of("Cálculo"),
                cursoSalvo.getMaterias()
        );
        Assertions.assertFalse(cursoSalvo.isDeletado());
    }

    @Test
    public void test_shouldLogicallyDeleteCourse() {

        Curso curso = new Curso();
        curso.setId(1L);
        curso.setNome("Ciência da Computação");
        curso.setDeletado(false);

        Mockito.when(cursoRepository.findById(1L))
                .thenReturn(Optional.of(curso));

        cursoService.deletar(1L);

        Assertions.assertTrue(curso.isDeletado());

        Mockito.verify(cursoRepository).findById(1L);
        Mockito.verify(cursoRepository).save(curso);
        Mockito.verify(cursoRepository, Mockito.never())
                .delete(Mockito.any());
    }

    @Test
    public void test_shouldThrowExceptionWhenCourseDoesNotExist() {

        Mockito.when(cursoRepository.findById(99L))
                .thenReturn(Optional.empty());

        ResponseStatusException exception =
                Assertions.assertThrows(
                        ResponseStatusException.class,
                        () -> cursoService.deletar(99L)
                );

        Assertions.assertEquals(404, exception.getStatusCode().value());

        Mockito.verify(cursoRepository, Mockito.never())
                .save(Mockito.any(Curso.class));
    }

    @Test
    public void test_shouldThrowExceptionWhenCourseIsAlreadyDeleted() {

        Curso curso = new Curso();
        curso.setId(1L);
        curso.setDeletado(true);

        Mockito.when(cursoRepository.findById(1L))
                .thenReturn(Optional.of(curso));

        Assertions.assertThrows(
                ResponseStatusException.class,
                () -> cursoService.deletar(1L)
        );

        Mockito.verify(cursoRepository, Mockito.never())
                .save(Mockito.any(Curso.class));
    }
}