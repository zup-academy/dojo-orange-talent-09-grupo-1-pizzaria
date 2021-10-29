package br.com.zup.edu.pizzaria.pizzas;

import br.com.zup.edu.pizzaria.ingredientes.Ingrediente;
import br.com.zup.edu.pizzaria.ingredientes.IngredienteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class NovaPizzaControllerTest {

    @Autowired
    private IngredienteRepository ingredienteRepository;

    @Autowired
    private MockMvc mvc;

    private List<Long> ingredientes;

    private final String URI = "/api/pizzas";

    @BeforeEach
    void setUp() {
        Ingrediente queijoMucarela = new Ingrediente("Queijo muçarela", 1, new BigDecimal("2.0"));
        Ingrediente tomate = new Ingrediente("Tomate", 1, new BigDecimal("2.0"));
        ingredienteRepository.saveAll(List.of(queijoMucarela, tomate));
    }

    @Test
    void deveCadastrarNovaPizza() throws Exception {
    }

}