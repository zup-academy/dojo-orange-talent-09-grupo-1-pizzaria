package br.com.zup.edu.pizzaria.ingredientes.cadastrodeingredientes;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;

import javax.transaction.Transactional;

import br.com.zup.edu.pizzaria.ingredientes.IngredienteRepository;
import com.fasterxml.jackson.core.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class NovoIngredienteControllerTest {

    @Autowired
    private IngredienteRepository ingredienteRepository;

    @Autowired
    private MockMvc mvc;

    private final String URI = "/api/ingredientes";

    @Test
    void deveCadastrarNovoIngrediente() throws Exception {

        NovoIngredienteRequest body = new NovoIngredienteRequest("Queijo muçarela", new BigDecimal("2.0"), 200);
        MockHttpServletRequestBuilder request = post(URI)
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content(new ObjectMapper().writeValueAsString(body));
        mvc.perform(request)
           .andExpect(status().isCreated())
           .andExpect(header().exists("Location"))
                .andExpect(redirectedUrlPattern("/api/ingredientes/{spring:[0-9]+}"));

    }

    @Test
    void naoDeveCadastrarIngredienteExistente() throws Exception {

        NovoIngredienteRequest body = new NovoIngredienteRequest("Queijo muçarela", new BigDecimal("2.0"), 200);

        ingredienteRepository.save(body.paraIngrediente());

        MockHttpServletRequestBuilder request = post(URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(body));
        mvc.perform(request)
                .andExpect(status().isBadRequest());
    }

    @ParameterizedTest
    @NullAndEmptySource
    void deveRetornarStatusCode400QuandoNomeInvalido(String input) throws Exception {
        NovoIngredienteRequest body = new NovoIngredienteRequest(input, new BigDecimal("2.0"), 200);
        MockHttpServletRequestBuilder request = post(URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(body));
        mvc.perform(request)
                .andExpect(status().isBadRequest());
    }

    @ParameterizedTest
    @ValueSource(strings = {"-1", "0"})
    void deveRetornartatusCode400QuandoPrecoNegativoOuZero(String preco) throws Exception {
        NovoIngredienteRequest body = new NovoIngredienteRequest("Queijo muçarela", new BigDecimal(preco), 200);
        MockHttpServletRequestBuilder request = post(URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(body));
        mvc.perform(request)
                .andExpect(status().isBadRequest());
    }

    @ParameterizedTest
    @NullSource
    void deveRetornarStatusCode400QuandoPrecoNulo(BigDecimal preco) throws Exception {
        NovoIngredienteRequest body = new NovoIngredienteRequest("Queijo muçarela", preco, 200);
        MockHttpServletRequestBuilder request = post(URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(body));
        mvc.perform(request)
                .andExpect(status().isBadRequest());
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, 0})
    void deveRetornartatusCode400QuandoQuantidadeNegativaOuZero(int quantidade) throws Exception {
        NovoIngredienteRequest body = new NovoIngredienteRequest("Queijo muçarela", new BigDecimal("2.0"), quantidade);
        MockHttpServletRequestBuilder request = post(URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(body));
        mvc.perform(request)
                .andExpect(status().isBadRequest());
    }
}