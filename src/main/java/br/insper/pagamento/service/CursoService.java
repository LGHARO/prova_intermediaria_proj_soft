package br.insper.pagamento.service;

import br.insper.pagamento.dto.CursoDTO;
import br.insper.pagamento.entity.Curso;
import br.insper.pagamento.repository.CursoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class CursoService {

	private final CursoRepository cursoRepository;

	public CursoService(CursoRepository cursoRepository) {
		this.cursoRepository = cursoRepository;
	}

	public List<Curso> listar(String nome) {
		if (nome == null || nome.isBlank()) {
			return cursoRepository.findByDeletadoFalse();
		}

		return cursoRepository
				.findByDeletadoFalseAndNomeStartingWithIgnoreCase(nome);
	}

	public Curso criar(CursoDTO dto) {
		Curso curso = Curso.fromDTO(dto);
		return cursoRepository.save(curso);
	}

	public void deletar(Long id) {
		Curso curso = cursoRepository.findById(id)
				.filter(item -> !item.isDeletado())
				.orElseThrow(() -> new ResponseStatusException(
						HttpStatus.NOT_FOUND,
						"Curso não encontrado"
				));

		curso.setDeletado(true);
		cursoRepository.save(curso);
	}
}