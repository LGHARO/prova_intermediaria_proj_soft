package br.insper.pagamento.dto;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
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
