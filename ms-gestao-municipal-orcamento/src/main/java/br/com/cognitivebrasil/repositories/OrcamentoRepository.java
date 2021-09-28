package br.com.cognitivebrasil.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.cognitivebrasil.enumerator.Pasta;
import br.com.cognitivebrasil.model.Orcamento;

public interface OrcamentoRepository extends JpaRepository<Orcamento, Long>{

	Orcamento findByDestino(Pasta destino);
	
}
