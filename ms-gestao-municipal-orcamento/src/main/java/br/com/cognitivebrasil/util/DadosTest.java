package br.com.cognitivebrasil.util;

import org.springframework.stereotype.Service;
import com.google.gson.Gson;
import br.com.cognitivebrasil.enumerator.Origem;
import br.com.cognitivebrasil.enumerator.Pasta;
import br.com.cognitivebrasil.model.Orcamento;

@Service
public class DadosTest {
	
	public void criaOrcamentoTest() {
		
		Orcamento orcamento = new Orcamento();
		
		orcamento.setDestino(Pasta.SAUDE);
		orcamento.setOrigem(Origem.FEDERAL);
		orcamento.setTotalOrcamento(8000000.00);
		orcamento.setValorGasto(0.00);
		
		Gson gson = new Gson();
		
		System.out.println("ORÇAMENTO TESTE " + gson.toJson(orcamento));
		
		/*
		JwtAuthenticationDto userTeste = new JwtAuthenticationDto();
		
		userTeste.setEmail("ronaldo.fjv@gmail.com");
		userTeste.setSenha("$2a$12$Uwsf5E2H6PNhUC7woW7GIeVDPYhzbv2cAhXjEw7thrzMdmmhcTKRi");

		System.out.println(gson.toJson(userTeste));
		*/

	}
}
