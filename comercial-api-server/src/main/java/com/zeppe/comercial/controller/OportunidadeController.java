package com.zeppe.comercial.controller;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.zeppe.comercial.model.Oportunidade;
import com.zeppe.comercial.repository.OportunidadeRepository;

@RestController
@RequestMapping("/oportunidades")
public class OportunidadeController {
	
	@Autowired
	private OportunidadeRepository oportunidadeRepository;

	@GetMapping
	public List<Oportunidade> listar(){
		return oportunidadeRepository.findAll();
	}
	
	/**
	 * 
	 * @param id
	 * @return
	 * 
	 * nota: o PathVariable é para indicar que esse parametro é o que vai ser usado no url como id
	 */
	@GetMapping("/{id}")
	public ResponseEntity<Oportunidade> buscar(@PathVariable Long id) {
		
		Optional<Oportunidade> oportunidade = oportunidadeRepository.findById(id);
		
		if(oportunidade.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		
		return ResponseEntity.ok(oportunidade.get());
	}
	
	/**
	 * 
	 * @param oportunidade
	 * @return
	 * 
	 * nota: Requestbody é o que transforma o json em um objeto java
	 * nota2: ResponseStatus serve para mudar o status de resposta do server
	 * nesse caso ele retornaria 200 (requisição bem sucedida) e o ideal seria
	 * 201 que é de create
	 */
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Oportunidade adicionar(@Valid @RequestBody Oportunidade oportunidade) {
		
		Optional<Oportunidade> oportunidadeExistente = oportunidadeRepository.findByDescricaoAndNomeProspecto(
				oportunidade.getDescricao(), oportunidade.getNomeProspecto());
		
		if(oportunidadeExistente.isPresent()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					"Já existe uma oportunidade cadastrada com esse prospecto e descrição");
		}
		
		return oportunidadeRepository.save(oportunidade);
	}
	
	
}
