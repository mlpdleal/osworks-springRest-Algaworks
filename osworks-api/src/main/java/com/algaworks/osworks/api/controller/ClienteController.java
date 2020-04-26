package com.algaworks.osworks.api.controller;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.algaworks.osworks.domain.model.Cliente;
import com.algaworks.osworks.domain.repository.ClienteRepository;
import com.algaworks.osworks.domain.service.CadastroClienteService;

/*
 * Essa anotação indica que esse controler e rest
 * Como o spring esta utilizando o servidor de aplicação tomcat
 * que por padrão esta na porta 8080
 * Para testar essa api basta informar no postman localhost:8080/clientes
 */

@RestController
@RequestMapping("/clientes") // Essa anotation atribui o prefixo da uri
public class ClienteController {
	
	// Indica que esse atributo utiliza os recursos de injeção de dependência
	@Autowired
	private ClienteRepository clienteRepository;
	
	@Autowired
	private CadastroClienteService cadastroCliente;
	
	// esse metodo retorna uma collection resource
	// Essa anotation atribui a esse método a uri
	@GetMapping
	public List<Cliente> listar() {
		return clienteRepository.findAll();
	}
	
	//esse método retorna uma singleton resource (um recurso)
	// Essa anotation tem um valor váriavel na sua composição
	
	@GetMapping("/{clienteId}")
	public ResponseEntity<Cliente> buscar(@PathVariable Long clienteId) {
		
		// Utilizamos uma instância do tipo optional uma vez que o valor retornado pode ser um resource ou um valor nulo
		Optional<Cliente> cliente = clienteRepository.findById(clienteId);
		
		/*
		 * O ResponseEntity e um objeto que contem a resposta da requisição http
		 * Caso a resposta seja ok (codigo 200), é retornada essa resposta + o valor do objeto cliente
		 */
		
		if (cliente.isPresent()) {
			return ResponseEntity.ok(cliente.get());
		}
		
		// Caso a busca não retorne um cliente de acordo com o id informado
		// O responseEntity retorna not found (Codigo 404)
		
		return ResponseEntity.notFound().build();
		
	}
	
	// Indica que essa requisição é post
	// a uri dessa requisição é /clientes, como esse prefixo já foi declarado no RequestMapping da classe, não há necessidade de declara-lo aqui
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)// indica que o sucesso dessa requisição retorna um http status code 201
	public Cliente adicionar(@Valid @RequestBody Cliente cliente) { // transforma o objeto enviado no post em um objeto cliente java
		
		return cadastroCliente.salvar(cliente); // o método save esta disponível através da interface JpaRepository que clienteRepository herda
		
	}
	
	/**
	 * 
	 * @param clienteId - id do registro que deseja alterar
	 * @param cliente - corpo da requisição - Ex: corpo do json
	 * @return - http code status
	 * @Valid - indica que que deve usar os recursos do bean validation definido na classe Cliente
	 */
	@PutMapping("/{clienteId}")
	public ResponseEntity<Cliente> atualizar(@PathVariable Long clienteId, 
			@Valid @RequestBody Cliente cliente){ 
		
		if(!clienteRepository.existsById(clienteId)) { // verifica se existe registro com o id informado
			return ResponseEntity.notFound().build(); // caso não exista retorna o http code status 404 - not found
		}
		
		// caso exista registro com id, atribui esse id ao objeto cliente para que não seja criado um novo registro no banco
		cliente.setId(clienteId);
		cliente = cadastroCliente.salvar(cliente); // grava no banco esse objeto alterado
		
		// retorna o http status code 200 - sucesso
		return ResponseEntity.ok(cliente);
	}
	
	
	// indica que a requisição é um delete
	@DeleteMapping("/{clienteId}")
	public ResponseEntity<Void> remover(@PathVariable Long clienteId){
		
		if(!clienteRepository.existsById(clienteId)) {
			return ResponseEntity.notFound().build();
		}
		
		// deleta o cliente de acordo com o id informado
		cadastroCliente.excluir(clienteId);
		
		// como não a response não tem corpo, por padrão o http status é 204 - no content
		return ResponseEntity.noContent().build();
	}
	
	

}
