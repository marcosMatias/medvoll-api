package med.voll.api.controller;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import jakarta.validation.Valid;
import med.voll.api.domain.medico.DadosAtualizacaoMedico;
import med.voll.api.domain.medico.DadosCadastroMedico;
import med.voll.api.domain.medico.DadosDetalhamentoMedico;
import med.voll.api.domain.medico.DadosListagemMedico;
import med.voll.api.domain.medico.Medico;
import med.voll.api.domain.medico.MedicoRepository;

@RestController
@RequestMapping("medicos")
public class MedicoController {
	
	@Autowired
	private MedicoRepository medicoRepository;

	@PostMapping
	@Transactional
	public ResponseEntity cadastrar(@RequestBody @Valid DadosCadastroMedico dados ,UriComponentsBuilder uriBuilder) {

		var medico = new Medico(dados);
	medicoRepository.save(medico);		
	
	var uri = uriBuilder.path("/medicos/{id}").buildAndExpand(medico.getId()).toUri();
	
	return ResponseEntity.created(uri).body(new DadosDetalhamentoMedico(medico));
	
	}
	
	@GetMapping
	public ResponseEntity<Page<DadosListagemMedico>> listar(Pageable paginacao){
		//medicoRepository.findAll(paginacao).stream().map(DadosListagemMedico::new).toList();
		//return medicoRepository.findAll(paginacao).map(DadosListagemMedico::new);
		var page =  medicoRepository.findAllByAtivoTrue(paginacao).map(DadosListagemMedico::new);
		
		return ResponseEntity.ok(page);
	}
	
	
	@PutMapping
	@Transactional
	public ResponseEntity atualizar(@RequestBody @Valid DadosAtualizacaoMedico dados) {
	
		var medico = medicoRepository.getReferenceById(dados.id());
		medico.atualizarInformacoes(dados);
		
		// o jpa faz o update sozinho porque ocorreu uma mudança na entidade, devido a anotação transactional
		
		return ResponseEntity.ok(new DadosDetalhamentoMedico(medico));
	}
	
	@DeleteMapping("/{id}")
	@Transactional
	public ResponseEntity excluir(@PathVariable Long id) {
		
		//medicoRepository.deleteById(id);
		var medico = medicoRepository.getReferenceById(id);
		medico.excluir();
		 return ResponseEntity.noContent().build();
		
	}
	
	
	@GetMapping("/{id}")
	public ResponseEntity detalhar(@PathVariable Long id) {
		
		
		var medico = medicoRepository.getReferenceById(id);
		
		 return ResponseEntity.ok(new DadosDetalhamentoMedico(medico));
		
	}
	
	
	
}
