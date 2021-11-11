package br.com.zup.edu.pizzaria.handler;

import br.com.zup.edu.pizzaria.exceptions.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestControllerAdvice
public class ControllerHandler {

    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ExceptionHandler(IngredienteNaoExistenteException.class)
    public Map<String, String> handlePaisJaExistenteError(BussinessException e) {
        Map<String, String> response = new HashMap<>();
        response.put("message:", e.getMessage());
        return response;
    }

}

