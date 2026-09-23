package br.insper.avaliacao.observer;

import br.insper.avaliacao.entity.Avaliacao;
import br.insper.avaliacao.entity.Log;
import br.insper.avaliacao.repository.AvaliacaoRepository;
import br.insper.avaliacao.repository.LogRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class AuditLoggerObserver implements AvaliacaoObserver {

    @Autowired
    private LogRepository logRepository;

    private static final Logger logger = LoggerFactory.getLogger(AuditLoggerObserver.class);

    @Override
    public void atualizar(long id, String acao, LocalDateTime time) {
        String mensagem = String.format(
                "AUDITORIA - Avaliacao ID: %d | Acao: %s | time: %s",
                id,
                acao,
                time
        );

        logger.info(mensagem);
    }
}