package br.insper.curso.repository;

import br.insper.curso.entity.Curso;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CursoRepository extends JpaRepository<Curso, Long> {

    List<Curso> findByDeletadoFalse();

    List<Curso> findByDeletadoFalseAndNomeStartingWithIgnoreCase(String nome);
}