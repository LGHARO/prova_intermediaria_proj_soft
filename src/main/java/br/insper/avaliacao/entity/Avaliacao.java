package br.insper.avaliacao.entity;

import br.insper.avaliacao.dto.AvaliacaoDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;


@Entity
@Table(name = "avaliacoes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Avaliacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int nota; //tem q ir de um a 5

    private String autor;

    private String conteudo;

    private LocalDate dataAvaliacao;


    public static Avaliacao fromDTO(AvaliacaoDTO dto) {
        Avaliacao avaliacao = new Avaliacao();

        avaliacao.setNota(dto.getNota());
        avaliacao.setAutor(dto.getAutor());
        avaliacao.setConteudo(dto.getConteudo());
        avaliacao.setDataAvaliacao(dto.getDataAvaliacao());

        return avaliacao;
    }
}