package br.insper.avaliacao.observer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;

public class GradeNotifierObserver implements AvaliacaoObserver {

    @Override
    public void atualizar(long id, String acao, LocalDateTime time) {
        String mensagem = String.format(
                "AVALIACAO NEGATIVA - Avaliacao ID: %d | Acao: %s | time: %s",
                id,
                acao,
                time
        );
        System.out.println(mensagem);
    }
}
