package br.com.zup.edu.pizzaria.pizzas;

import br.com.zup.edu.pizzaria.ingredientes.*;
import br.com.zup.edu.pizzaria.pizzas.cadastropizza.*;
import com.fasterxml.jackson.databind.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.autoconfigure.web.servlet.*;
import org.springframework.boot.test.context.*;
import org.springframework.http.*;
import org.springframework.test.web.servlet.*;
import org.springframework.test.web.servlet.request.*;

import javax.transaction.*;
import java.math.*;
import java.util.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
	@NullAndEmptySource
	void naoDeveCadastrarPizzaSemIngredientes(List<Long> ingredientes) throws Exception {
		NovaPizzaRequest body = new NovaPizzaRequest("Calabresa", ingredientes);
		
		MockHttpServletRequestBuilder request = post(URI).contentType(MediaType.APPLICATION_JSON)
				.content(new ObjectMapper().writeValueAsString(body));
		
		mvc.perform(request).andExpect(status().isBadRequest());
	}

	@Test
	void naoDeveCadastrarPizzaComIngredientesInvalidos() throws Exception {
		NovaPizzaRequest body = new NovaPizzaRequest("Calabresa", List.of(100L, 500L));
		MockHttpServletRequestBuilder request = post(URI).contentType(MediaType.APPLICATION_JSON)
				.content(new ObjectMapper().writeValueAsString(body));
		mvc.perform(request).andExpect(status().isUnprocessableEntity());
	}

}