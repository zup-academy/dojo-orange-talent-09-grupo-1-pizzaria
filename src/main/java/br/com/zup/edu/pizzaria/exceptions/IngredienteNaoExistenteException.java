package br.com.zup.edu.pizzaria.exceptions;

public class IngredienteNaoExistenteException extends BussinessException {

    public IngredienteNaoExistenteException(String ingredienteId) {
        super("O ingrediente do id informado não existe: " + ingredienteId);
    }

}
