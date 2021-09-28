package br.com.cognitivebrasil.validate;

import java.util.ArrayList;
import java.util.List;
import br.com.cognitivebrasil.model.Orcamento;

public class ValidateOrcamento {

	private List<String> errors;

	public List<String> getErrors() {
		return errors;
	}

	public void setErrors(List<String> errors) {
		this.errors = errors;
	}
	
	public List<String> valida(Orcamento obj) {
		
		errors = new ArrayList<String>();
		
		try {
			
			if(obj == null) {
				errors.add("Os dados do orçamento não podem ser nulos.");
				return this.errors;
			}else {
				if(obj.getDestino() == null)
					errors.add("Campo destino inválido.");
				
				if(obj.getOrigem() == null)
					errors.add("Campo origem inválido.");
					
				if(obj.getTotalOrcamento() == null)
					errors.add("Campo total orçamento inválido.");
				
				if(obj.getValorGasto() == null)
					errors.add("Campo valor gasto inválido.");
			}
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		return this.errors;
	}
}
