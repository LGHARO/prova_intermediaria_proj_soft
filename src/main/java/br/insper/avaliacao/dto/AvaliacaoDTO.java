package br.insper.avaliacao.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class AvaliacaoDTO {
    private int nota; //tem q ir de um a 5

    private String autor;

    private String conteudo;

    private LocalDate dataAvaliacao;

}
