package com.algaworks.osworks.domain.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.algaworks.osworks.domain.model.Cliente;

/**
 * 
 * @author manoel
 *
 * Interface responsável pelo repository de clientes
 * Extende a interface JpaRepository
 * Com isso, através de convensão ao utlizar essa interface, vc já tem acesso aos métodos básicos para incluir dados no banco
 * No operador diamante de JpaRepository, é passada o tipo da classe modelo + o tipo do id dessa classe
 * A anotação Repository pertence ao Spring, que indica que essa interface se comporta com um repositorio
 */

// Essa classe já está pronta para ser usada nas controllers, basta instanciar-la

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long>{
	
	// Os nomes dos métodos são criados de acordo com a convensão findBy<nomeAtributo>
	
	// Pesquisa pelo nome pela correspondência exata
	List<Cliente> findByNome(String nome);
	// Pesquisa pelo nome que contem a string informada
	List<Cliente> findByNomeContaining(String nome);
	//pesquisa por email informado
	Cliente findByEmail(String email);
	
}
