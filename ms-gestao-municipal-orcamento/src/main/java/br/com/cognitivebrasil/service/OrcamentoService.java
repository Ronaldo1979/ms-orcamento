package br.com.cognitivebrasil.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import br.com.cognitivebrasil.enumerator.Pasta;
import br.com.cognitivebrasil.model.Orcamento;
import br.com.cognitivebrasil.model.Usuario;
import br.com.cognitivebrasil.repositories.OrcamentoRepository;
import br.com.cognitivebrasil.repositories.UsuarioRepository;

@Service
public class OrcamentoService {

	@Autowired
	private OrcamentoRepository repository;
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	public Orcamento cadastraOrcamento(Orcamento obj) {
		Orcamento orcamento = null;
		try {
			orcamento = repository.save(obj);
		}catch (Exception e) {
			e.printStackTrace();
		}finally {
			repository.flush();
		}
		return orcamento;
	}
	
	public Optional<Orcamento> buscaOrcamento(Long objId) {
		Optional<Orcamento> orcamento = null;
		try {
			orcamento = repository.findById(objId);
		}catch (NoSuchElementException e) {
			return null;
		}catch (Exception e) {
			e.printStackTrace();
			return null;
		}finally {
			repository.flush();
		}
		return orcamento;
	}
	
	public Optional<Orcamento> buscaOrcamentoPorDestino(Pasta destino) {
		Optional<Orcamento> orcamento = null;
		try {
			orcamento = Optional.ofNullable(repository.findByDestino(destino));
		}catch (NoSuchElementException e) {
			return null;
		}catch (Exception e) {
			e.printStackTrace();
			return null;
		}finally {
			repository.flush();
		}
		return orcamento;
	}
	
	public List<Orcamento> listaOrcamento() {
		List<Orcamento> listaOrcamento = null;
		try {
			listaOrcamento = repository.findAll();
		}catch (Exception e) {
			e.printStackTrace();
		}finally {
			repository.flush();
		}
		return listaOrcamento;
	}
	
	public Orcamento atualizaOrcamento(Orcamento obj) {
		Orcamento orcamento = null;
		try {
			orcamento = repository.saveAndFlush(obj);
		}catch (Exception e) {
			e.printStackTrace();
		}finally {
			repository.flush();
		}
		return orcamento;
	}
	
	//USER==================================================================//
	
	public Usuario cadastraUsuario(Usuario obj) {
		Usuario usuario = null;
		try {
			usuario = usuarioRepository.save(obj);
		}catch (Exception e) {
			e.printStackTrace();
		}finally {
			usuarioRepository.flush();
		}
		return usuario;
	}
	
	public Usuario atualizaUsuario(Usuario obj) {
		Usuario usuario = null;
		try {
			usuario = usuarioRepository.saveAndFlush(obj);
		}catch (Exception e) {
			e.printStackTrace();
		}finally {
			usuarioRepository.flush();
		}
		return usuario;
	}
	
	public Optional<Usuario> buscaUsuario(String email) {
		
		Optional<Usuario> usuario = null;
		
		try {
			usuario = Optional.ofNullable(usuarioRepository.findByEmail(email));
		}catch (NoSuchElementException e) {
			return null;
		}catch (Exception e) {
			e.printStackTrace();
			return null;
		}finally {
			usuarioRepository.flush();
		}
		return usuario;
	}
	
	public List<Usuario> listaUsuario() {
		List<Usuario> listaUsuario = null;
		try {
			listaUsuario = usuarioRepository.findAll();
		}catch (Exception e) {
			e.printStackTrace();
		}finally {
			usuarioRepository.flush();
		}
		return listaUsuario;
	}
	
}
