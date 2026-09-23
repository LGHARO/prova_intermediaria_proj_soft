package br.insper.curso.controller;

import br.insper.curso.dto.CursoDTO;
import br.insper.curso.entity.Curso;
import br.insper.curso.service.CursoService;
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