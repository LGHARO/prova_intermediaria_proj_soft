package br.insper.avaliacao.observer;

import br.insper.avaliacao.entity.Avaliacao;

import java.time.LocalDateTime;

public interface AvaliacaoObservable {
    void notificarObservadores(long id, String acao, LocalDateTime time);
}
