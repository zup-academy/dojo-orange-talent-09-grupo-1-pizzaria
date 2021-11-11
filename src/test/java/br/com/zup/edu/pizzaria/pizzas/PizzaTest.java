package br.com.zup.edu.pizzaria.pizzas;

import br.com.zup.edu.pizzaria.ingredientes.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.springframework.util.Assert;

import java.math.*;
import java.util.*;

class PizzaTest {

    @Test
    void deveCriarUmaPizza() {
        List<Ingrediente> ingredientes = List.of(new Ingrediente("Tomate", 5, new BigDecimal("2.50")));

        Pizza pizza = new Pizza("Queijo", ingredientes);
        Assertions.assertNotNull(pizza);
    }

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

    @ParameterizedTest
    @NullAndEmptySource
    void naoDeveCriarPizzaSemIngredientes(List<Ingrediente> ingredientes) {
       Assertions.assertThrows(IllegalArgumentException.class, () -> {
           new Pizza("Calabresa", ingredientes);
       });
    }
}