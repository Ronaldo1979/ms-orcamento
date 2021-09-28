package br.com.cognitivebrasil.feignclients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import br.com.cognitivebrasil.model.Projeto;
import br.com.cognitivebrasil.util.Response;

@Component
@FeignClient(name = "ms-projeto", url="localhost:8087", path = "/resource")
public interface ProjetoFeignClient {

	@PatchMapping(value = "project/{projetoId}/{despesa}")
	public ResponseEntity<Response<Projeto>> atualizaProjeto(@PathVariable("projetoId") Long projetoId, @PathVariable("despesa") Double despesa);
}
