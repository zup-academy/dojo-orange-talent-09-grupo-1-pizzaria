package br.com.zup.edu.pizzaria.pizzas;

import br.com.zup.edu.pizzaria.ingredientes.Ingrediente;
import br.com.zup.edu.pizzaria.ingredientes.IngredienteRepository;
import br.com.zup.edu.pizzaria.pizzas.cadastropizza.NovaPizzaRequest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.transaction.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class NovaPizzaControllerTest {

	@Autowired
	private IngredienteRepository ingredienteRepository;
	
	@Autowired
	private PizzaRepository pizzaRepository;

	@Autowired
	private MockMvc mvc;

	private List<Long> ingredientes;

	private final String URI = "/api/pizzas";
	
	static List<Long> retornaListaVazia() {
		return List.of();
	}

	@BeforeEach
	void setUp() {
		Ingrediente queijoMucarela = new Ingrediente("Queijo muçarela", 1, new BigDecimal("2.0"));
		Ingrediente tomate = new Ingrediente("Tomate", 1, new BigDecimal("2.0"));
		ingredienteRepository.saveAll(List.of(queijoMucarela, tomate));
		ingredientes = List.of(queijoMucarela.getId(), tomate.getId());
	}

	@Test
	void deveCadastrarNovaPizza() throws Exception {
		NovaPizzaRequest body = new NovaPizzaRequest("Muçarela", ingredientes);
		
		MockHttpServletRequestBuilder request = post(URI).contentType(MediaType.APPLICATION_JSON)
				.content(new ObjectMapper().writeValueAsString(body));
		mvc.perform(request).andExpect(status().isCreated()).andExpect(header().exists("Location"))
				.andExpect(redirectedUrlPattern("/api/pizzas/{spring:[0-9]+}"));

	}
	
	@Test
	void naoDeveCadastrarPizzaComSaborDuplicado() throws Exception {
		NovaPizzaRequest body = new NovaPizzaRequest("Calabresa", ingredientes);
		pizzaRepository.save(body.paraPizza(ingredienteRepository));
		
		MockHttpServletRequestBuilder request = post(URI).contentType(MediaType.APPLICATION_JSON)
				.content(new ObjectMapper().writeValueAsString(body));
		
		mvc.perform(request).andExpect(status().isBadRequest());
		
	}
	
	@ParameterizedTest
	@NullSource
	@MethodSource("retornaListaVazia")
	void naoDeveCadastrarPizzaSemIngredientes(List<Long> ingredientes) throws Exception {
		NovaPizzaRequest body = new NovaPizzaRequest("Calabresa", ingredientes);
		
		MockHttpServletRequestBuilder request = post(URI).contentType(MediaType.APPLICATION_JSON)
				.content(new ObjectMapper().writeValueAsString(body));
		
		mvc.perform(request).andExpect(status().isBadRequest());
	}

}