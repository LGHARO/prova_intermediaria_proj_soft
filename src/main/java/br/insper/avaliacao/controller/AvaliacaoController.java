package br.insper.avaliacao.controller;

import br.insper.avaliacao.dto.AvaliacaoDTO;
import br.insper.avaliacao.entity.Avaliacao;
import br.insper.avaliacao.service.AvaliacaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/avaliacao")
public class AvaliacaoController {

	@Autowired
	private AvaliacaoService avaliacaoService;

	@GetMapping
	public List<Avaliacao> listar() {
		return avaliacaoService.listar();
	}

	@GetMapping("/{id}")
	public Avaliacao obter(@PathVariable Long id) {
		return avaliacaoService.get(id);
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Avaliacao criar(@RequestBody AvaliacaoDTO dto) {
		return avaliacaoService.criar(dto);
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deletar(@PathVariable Long id) {
		avaliacaoService.deletar(id);
	}
}