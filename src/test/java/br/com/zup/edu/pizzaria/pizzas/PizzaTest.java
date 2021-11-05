package br.com.zup.edu.pizzaria.pizzas;

import br.com.zup.edu.pizzaria.ingredientes.*;
import org.junit.jupiter.api.*;

import java.math.*;
import java.util.*;

class PizzaTest {

    @Test
    void deveRetornarPrecoCorreto() {
        List<Ingrediente> ingredientes = List.of(new Ingrediente("Tomate", 5, new BigDecimal("2.50")),
                                                 new Ingrediente("Queijo muçarela", 2, new BigDecimal("1.25")));
        BigDecimal precoTotalIngredientes = ingredientes.stream()
                .map(Ingrediente::getPreco)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Pizza pizza = new Pizza("Queijo", ingredientes);
        Assertions.assertEquals(pizza.getCustoFixo().add(precoTotalIngredientes), pizza.getPreco());
    }
}