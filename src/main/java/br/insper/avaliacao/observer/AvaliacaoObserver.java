package br.insper.avaliacao.observer;

import br.insper.avaliacao.entity.Avaliacao;

import java.time.LocalDateTime;

public interface AvaliacaoObserver {
    void atualizar (long id, String acao, LocalDateTime time);
}
