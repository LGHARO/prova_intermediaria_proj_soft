package br.insper.pagamento.controller;

import br.insper.pagamento.dto.CursoDTO;
import br.insper.pagamento.entity.Curso;
import br.insper.pagamento.service.CursoService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cursos")
public class CursoController {

	private final CursoService cursoService;

	public CursoController(CursoService cursoService) {
		this.cursoService = cursoService;
	}

	@GetMapping
	public List<Curso> listar(@RequestParam(required = false) String nome) {
		return cursoService.listar(nome);
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Curso criar(@RequestBody CursoDTO dto) {
		return cursoService.criar(dto);
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deletar(@PathVariable Long id) {
		cursoService.deletar(id);
	}
}