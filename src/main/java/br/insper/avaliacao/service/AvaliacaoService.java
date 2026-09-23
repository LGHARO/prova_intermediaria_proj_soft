package br.insper.avaliacao.service;

import br.insper.avaliacao.dto.AvaliacaoDTO;
import br.insper.avaliacao.entity.Avaliacao;
import br.insper.avaliacao.observer.AvaliacaoObservable;
import br.insper.avaliacao.observer.AvaliacaoObserver;
import br.insper.avaliacao.repository.AvaliacaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class AvaliacaoService implements AvaliacaoObservable {

	@Autowired
	private AvaliacaoRepository avaliacaoRepository;

	@Autowired(required = false)
	private List<AvaliacaoObserver> observers;

	@Override
	public void notificarObservadores(long id, String acao, LocalDateTime time) {
		if (observers != null) {
			for (AvaliacaoObserver observer : observers) {
				observer.atualizar(id, acao, time);
			}
		}
	}

	public List<Avaliacao> listar() {
		return avaliacaoRepository.findAll();
	}

	public Avaliacao criar(AvaliacaoDTO dto) {
		Avaliacao avaliacao = Avaliacao.fromDTO(dto);

		if (avaliacao.getNota() > 5 || avaliacao.getNota() < 0) {
			throw new ResponseStatusException(
					HttpStatus.FORBIDDEN,
					"a nota deve ser entre 1 a 5"
			);
		}

		Avaliacao savedAvaliacao = avaliacaoRepository.save(avaliacao);

		notificarObservadores(
				savedAvaliacao.getId(),
				"CREATE",
				LocalDateTime.now()
		);

		return savedAvaliacao;
	}

	public Avaliacao get(long id){
		return avaliacaoRepository.findById(id);
	}

	public void deletar(Long id) {
		if (avaliacaoRepository.existsById(id)) {
			avaliacaoRepository.deleteById(id);
			notificarObservadores(id,"CREATE",LocalDateTime.now());

		} else{
			throw new ResponseStatusException(
					HttpStatus.FORBIDDEN,
					"a avaliacao nn existe"
			);
		}

	}
}