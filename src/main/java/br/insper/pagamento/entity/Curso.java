package br.insper.pagamento.entity;

import br.insper.pagamento.dto.CursoDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "cursos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Curso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int quantidadeAlunos;

    private String nome;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "curso_materias",
            joinColumns = @JoinColumn(name = "curso_id")
    )
    @Column(name = "materia")
    private List<String> materias = new ArrayList<>();

    private boolean deletado = false;

    public static Curso fromDTO(CursoDTO dto) {
        Curso curso = new Curso();

        curso.setNome(dto.getNome());
        curso.setMaterias(dto.getMaterias());
        curso.setQuantidadeAlunos(dto.getQuantidade_alunos());
        curso.setDeletado(false);

        return curso;
    }
}