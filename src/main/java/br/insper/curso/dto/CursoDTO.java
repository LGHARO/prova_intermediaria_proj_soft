package br.insper.curso.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class CursoDTO {
    private int quantidade_alunos;

    private String nome;

    private List<String> materias;
}
