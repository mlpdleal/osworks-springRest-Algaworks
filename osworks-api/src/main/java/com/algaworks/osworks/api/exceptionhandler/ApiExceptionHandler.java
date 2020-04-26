package com.algaworks.osworks.api.exceptionhandler;

import java.time.OffsetDateTime;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.algaworks.osworks.domain.exception.EntidadeNaoEncontradaException;
import com.algaworks.osworks.domain.exception.NegocioException;

/*
 * Classe que trata as possíveis exceções da api
 * Extende a classe ResponseEntityExceptionHandler que já contem o tratamento de exceçoes comuns para serviços rest
 */

@ControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler{
	
	// Nesse momento esse atributo possui a injeção de dependência para pegar o conteúdo do arquivo messages.properties
	@Autowired
	private MessageSource messageSource;
	
	@ExceptionHandler(EntidadeNaoEncontradaException.class)
	public ResponseEntity<Object> handleEntidadeNaoEncontrada(NegocioException ex, WebRequest request){
		
		var status = HttpStatus.NOT_FOUND;
		
		var problema = new Problema();
		problema.setStatus(status.value());
		problema.setTitulo(ex.getMessage());
		problema.setDataHora(OffsetDateTime.now());
		
		
		return handleExceptionInternal(ex, problema, new HttpHeaders(), status, request);
	}
	
	@ExceptionHandler(NegocioException.class)
	public ResponseEntity<Object> handleNegocio(NegocioException ex, WebRequest request){
		
		var status = HttpStatus.BAD_REQUEST;
		
		var problema = new Problema();
		problema.setStatus(status.value());
		problema.setTitulo(ex.getMessage());
		problema.setDataHora(OffsetDateTime.now());
		
		
		return handleExceptionInternal(ex, problema, new HttpHeaders(), status, request);
	}
	
	// Sobreescrita do método handleMethodArgumentNotValid para que a response seja definida da forma que desejamos
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		
		// Instancia uma variavel arraylist que carrega objetos do tipo Problema.Campo (inner class)
		var campos = new ArrayList<Problema.Campo>();
		
		// percorrer todos os possiveis erros que o objeto ex tem armazenado e adiciona na propriedade campos
		for (ObjectError error : ex.getBindingResult().getAllErrors()) {
			String nome = ((FieldError) error).getField();
			String mensagem = messageSource.getMessage(error, LocaleContextHolder.getLocale());
			
			campos.add(new Problema.Campo(nome, mensagem));
		}
		
		// Instancia um objeto do tipo problema, e faz o sets no objeto
		var problema = new Problema();
		problema.setStatus(status.value());
		problema.setTitulo("Um ou mais campos estão inválidos. Faça o preenchimento correto e tente novamente");
		problema.setDataHora(OffsetDateTime.now());
		problema.setCampos(campos);
		
		// retorno padrão do handleExceptionInternal, observe que o objeto problema e passado como parâmetro para a composição da reponse
		return super.handleExceptionInternal(ex, problema, headers, status, request);
	}

}
