package br.com.cognitivebrasil.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import br.com.cognitivebrasil.enumerator.Pasta;
import br.com.cognitivebrasil.model.Orcamento;
import br.com.cognitivebrasil.model.Usuario;
import br.com.cognitivebrasil.service.OrcamentoService;
import br.com.cognitivebrasil.util.Response;
import br.com.cognitivebrasil.validate.ValidateOrcamento;
import br.com.cognitivebrasil.validate.ValidateUsuario;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/resource")
@Api(value = "API Gestão Municipal de Orçamento")
@CrossOrigin(origins = "*")
public class OrcamentoAPI {

	@Autowired
	private OrcamentoService orcamentoService;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@PostMapping(value = "budgets")
	@ApiOperation(value = "Cadastra Orçamento",
		notes = "Cadastra um novo orçamento")
	public ResponseEntity<Response<Orcamento>> cadastraOrcamento(@Validated @RequestBody String obj){
		
		System.out.println("***********************************");
		System.out.println(">>> Cadastrando Orcamento...");
		System.out.println("***********************************");
		
		Orcamento orcamento = null;
		Response<Orcamento> response = new Response<Orcamento>();
		ValidateOrcamento validate = new ValidateOrcamento();
		List<String> listaErros = new ArrayList<String>();
		
		try {
			
			Gson gson = new Gson();
			JsonObject jsonObject = JsonParser.parseString(obj).getAsJsonObject();
			
			orcamento = gson.fromJson(jsonObject.toString(), Orcamento.class);
			
			if(orcamento != null) {
				
				List<String> erros = validate.valida(orcamento);
				
				if(erros == null || erros.isEmpty()) {
					
					Optional<Orcamento> orcamentoCadastrado = orcamentoService.buscaOrcamentoPorDestino(orcamento.getDestino());
					
					if(orcamentoCadastrado.isEmpty() || orcamentoCadastrado == null) {
						
						orcamento = orcamentoService.cadastraOrcamento(orcamento);
						response.setData(orcamento);
						
					}else {
						
						erros.add("Orçamento já cadastrado");
						response.setErrors(erros);
						
						orcamento.setId(orcamentoCadastrado.get().getId());
						response.setData(orcamento);
						
						return ResponseEntity.badRequest().body(response);
					}
					
				}else {
					
					response.setErrors(erros);
					response.setData(orcamento);
					
					return ResponseEntity.badRequest().body(response);
				}
			}
			
		}catch(Exception e) {
			e.printStackTrace();
			
			listaErros.add("Erro ao cadastrar orçamento");
			response.setErrors(listaErros);
			
			return ResponseEntity.badRequest().body(response);
		}
		
		return ResponseEntity.ok(response);
	}
	
	@GetMapping(value = "budgets")
	@ApiOperation(value = "Lista Orçamento",
		notes = "Lista todos os orçamentos")
	public  List<Orcamento> listaOrcamento() {
		
		List<Orcamento> orcamentos = null;
		
		try {
			orcamentos = orcamentoService.listaOrcamento();
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		return orcamentos;
	}
	
	@GetMapping(value = "budgets/{orcamentoId}")
	@ApiOperation(value = "Busca Orçamento",
		notes = "Busca orçamento especifico de acordo com ID")
	public ResponseEntity<Response<Orcamento>> buscaOrcamento(@PathVariable("orcamentoId") Long orcamentoId) {
		
		Response<Orcamento> response = new Response<Orcamento>();
		List<String> listaErros = new ArrayList<String>();
		Optional<Orcamento> orcamento = null;
		
		try {
		
			orcamento = orcamentoService.buscaOrcamento(orcamentoId);
			if(orcamento != null && !orcamento.isEmpty()) {
				response.setData(orcamento.get());
				return ResponseEntity.ok(response);
			
			}else {
				listaErros.add("Orçamento não enconatrado");
				response.setErrors(listaErros);
				return ResponseEntity.badRequest().body(response);
			}
			
		}catch (Exception e) {
			e.printStackTrace();
			
			listaErros.add("Erro ao buscar orçamento");
			response.setErrors(listaErros);
			return ResponseEntity.badRequest().body(response);
		}
	}
	
	@GetMapping(value = "budgets/despesa/{destino}")
	@ApiOperation(value = "Busca Orçamento Destino",
		notes = "Busca orçamento especifico de acordo com a pasta de destino")
	public ResponseEntity<Response<Orcamento>> buscaOrcamentoPorDestino(@PathVariable("destino") String destino) {
		
		Response<Orcamento> response = new Response<Orcamento>();
		List<String> listaErros = new ArrayList<String>();
		Optional<Orcamento> orcamento = null;
		
		try {
			
			Pasta pasta = null;
			if(destino.equals(Pasta.EDUCACAO.name()))
				pasta = Pasta.EDUCACAO;
			if(destino.equals(Pasta.SAUDE.name()))
				pasta = Pasta.SAUDE;
			if(destino.equals(Pasta.SPORT.name()))
				pasta = Pasta.SPORT;

			if(pasta != null) {
				
				orcamento = orcamentoService.buscaOrcamentoPorDestino(pasta);
				if(orcamento != null && !orcamento.isEmpty()) {
					response.setData(orcamento.get());
					return ResponseEntity.ok(response);
				
				}else {
					listaErros.add("Orçamento não enconatrado");
					response.setErrors(listaErros);
					return ResponseEntity.badRequest().body(response);
				}
				
			}else {
				
				listaErros.add("Pasta de destino não existe!");
				response.setErrors(listaErros);
				return ResponseEntity.badRequest().body(response);
			}
		
			
			
		}catch (Exception e) {
			e.printStackTrace();
			
			listaErros.add("Erro ao buscar orçamento");
			response.setErrors(listaErros);
			return ResponseEntity.badRequest().body(response);
		}
	}
	
	@PatchMapping(value = "budgets/{orcamentoId}/{despesa}")
	@ApiOperation(value = "Atualiza Orçamento",
		notes = "Atualiza valor do orçamento conforme a despesa informada")
	public ResponseEntity<Response<Orcamento>> atualizaOrcamento(@PathVariable("orcamentoId") Long orcamentoId, @PathVariable("despesa") Double despesa){
		
		System.out.println("****************************************");
		System.out.println(">>> Alterando orçamento...");
		System.out.println("****************************************");
		
		Orcamento orcamento = null;
		Response<Orcamento> response = new Response<Orcamento>();
		List<String> listaErros = new ArrayList<String>();
		
		try {
			
			orcamento = orcamentoService.buscaOrcamento(orcamentoId).orElse(null);
			if(orcamento != null) {
				
				Double saldoAtual = (orcamento.getTotalOrcamento() - orcamento.getValorGasto());
				
				if(despesa <= saldoAtual) {
					
					orcamento.setValorGasto(orcamento.getValorGasto() + despesa);
					orcamento = orcamentoService.atualizaOrcamento(orcamento);
					response.setData(orcamento);
				}else {
					
					listaErros.add("Saldo insuficiente!");
					response.setErrors(listaErros);
				}
			}
			
		}catch(Exception e) {
			e.printStackTrace();
			
			listaErros.add("Erro ao atualizar orçamento.");
			response.setErrors(listaErros);
			return ResponseEntity.badRequest().body(response);
		}
		return ResponseEntity.ok(response);
	}
	
	
	//USER==================================================================//
	
	@PostMapping(value = "user")
	@ApiOperation(value = "Cadastra Usuário",
		notes = "Cadastra um novo usuário")
	public ResponseEntity<Response<Usuario>> cadastraUsuario(@Validated @RequestBody String obj){
		
		System.out.println("***********************************");
		System.out.println(">>> Cadastrando Usuario...");
		System.out.println("***********************************");
		
		Usuario usuario = null;
		Response<Usuario> response = new Response<Usuario>();
		ValidateUsuario validate = new ValidateUsuario();
		List<String> listaErros = new ArrayList<String>();
		
		try {
			
			Gson gson = new Gson();
			JsonObject jsonObject = JsonParser.parseString(obj).getAsJsonObject();
			
			usuario = gson.fromJson(jsonObject.toString(), Usuario.class);
			
			if(usuario != null) {
				
				List<String> erros = validate.valida(usuario);
				
				if(erros == null || erros.isEmpty()) {
					
					Optional<Usuario> userCadastrado = orcamentoService.buscaUsuario(usuario.getEmail());
					
					if(userCadastrado.isEmpty() || userCadastrado == null) {
						usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
						usuario = orcamentoService.cadastraUsuario(usuario);
						response.setData(usuario);
					}else {
						
						erros.add("Usuário " + usuario.getEmail() + " já cadastrado!");
						response.setErrors(erros);
						response.setData(usuario);
						return ResponseEntity.badRequest().body(response);
					}
					
				}else {
					
					response.setErrors(erros);
					response.setData(usuario);
					
					return ResponseEntity.badRequest().body(response);
				}
			}
			
		}catch(Exception e) {
			e.printStackTrace();
			
			listaErros.add("Erro ao cadastrar usuario");
			response.setErrors(listaErros);
			
			return ResponseEntity.badRequest().body(response);
		}
		
		return ResponseEntity.ok(response);
	}
	
	@GetMapping(value = "user")
	@ApiOperation(value = "Lista Usuário",
		notes = "Lista todos os usuários")
	public  List<Usuario> listaUsuario() {
		
		List<Usuario> usuario = null;
		
		try {
			usuario = orcamentoService.listaUsuario();
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		return usuario;
	}
	
	@GetMapping(value = "user/{email}")
	@ApiOperation(value = "Busca Usuário",
		notes = "Busca usuário por email")
	public ResponseEntity<Response<Usuario>> buscaUsuario(@PathVariable("email") String email) {
		
		Response<Usuario> response = new Response<Usuario>();
		List<String> listaErros = new ArrayList<String>();
		Optional<Usuario> usuario = null;
		
		try {
		
			usuario = orcamentoService.buscaUsuario(email);
			if(usuario != null && !usuario.isEmpty()) {
				response.setData(usuario.get());
				return ResponseEntity.ok(response);
			
			}else {
				listaErros.add("Usuario não encontrado");
				response.setErrors(listaErros);
				return ResponseEntity.badRequest().body(response);
			}
			
		}catch (Exception e) {
			e.printStackTrace();
			
			listaErros.add("Erro ao buscar secretaria");
			response.setErrors(listaErros);
			return ResponseEntity.badRequest().body(response);
		}
	}
	
	@PutMapping(value = "user")
	@ApiOperation(value = "Atualiza Usuário",
		notes = "Atualiza senha de usuário")
	public ResponseEntity<Response<Usuario>> atualizaUsuario(@Validated @RequestBody String obj){
		
		System.out.println("***********************************");
		System.out.println(">>> Atualizando Usuario...");
		System.out.println("***********************************");
		
		Usuario usuario = null;
		Response<Usuario> response = new Response<Usuario>();
		ValidateUsuario validate = new ValidateUsuario();
		List<String> listaErros = new ArrayList<String>();
		
		try {
			
			Gson gson = new Gson();
			JsonObject jsonObject = JsonParser.parseString(obj).getAsJsonObject();
			
			usuario = gson.fromJson(jsonObject.toString(), Usuario.class);
			
			if(usuario != null) {
				
				List<String> erros = validate.valida(usuario);
				
				if(erros == null || erros.isEmpty()) {
					
					Optional<Usuario> userCadastrado = orcamentoService.buscaUsuario(usuario.getEmail());
					
					if( userCadastrado != null && !userCadastrado.isEmpty()) {
						if(usuario.getId() == null)
							usuario.setId(userCadastrado.get().getId());
						usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
						usuario = orcamentoService.atualizaUsuario(usuario);
						response.setData(usuario);
					}else {
						
						erros.add("Usuário " + usuario.getEmail() + " não existe!");
						response.setErrors(erros);
						response.setData(usuario);
						return ResponseEntity.badRequest().body(response);
					}
					
				}else {
					
					response.setErrors(erros);
					response.setData(usuario);
					
					return ResponseEntity.badRequest().body(response);
				}
			}
			
		}catch(Exception e) {
			e.printStackTrace();
			
			listaErros.add("Erro ao cadastrar usuario");
			response.setErrors(listaErros);
			
			return ResponseEntity.badRequest().body(response);
		}
		
		return ResponseEntity.ok(response);
	}
}
