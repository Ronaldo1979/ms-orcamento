package br.com.cognitivebrasil.repositories;


import org.springframework.data.jpa.repository.JpaRepository;

import br.com.cognitivebrasil.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long>{

	Usuario findByEmail(String email);
}
