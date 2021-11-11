package br.com.zup.edu.pizzaria.pizzas;

import br.com.zup.edu.pizzaria.ingredientes.Ingrediente;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Pizza {

    private final static BigDecimal PRECO_MASSA = new BigDecimal("15.0");
    private final static BigDecimal PRECO_MAO_DE_OBRA = new BigDecimal("5.0");

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String sabor;

    private BigDecimal preco = BigDecimal.ZERO;

    @ManyToMany
    private List<Ingrediente> ingredientes = new ArrayList<>();

    public Pizza(String sabor, List<Ingrediente> ingredientes) {
        if(ingredientes == null || ingredientes.isEmpty()) {
            throw new IllegalArgumentException("Lista de ingredientes é invalida!");
        }
        this.sabor = sabor;
        this.ingredientes = ingredientes;
        calcularPreco();
    }

    /**
     * @deprecated apenas para uso do hibernate
     */
    @Deprecated
    public Pizza() {
    }

    private void calcularPreco() {
        this.preco = this.preco.add(PRECO_MASSA);
        this.preco = this.preco.add(PRECO_MAO_DE_OBRA);
        this.preco = this.ingredientes.stream()
                .map(Ingrediente::getPreco)
                .reduce(this.preco, BigDecimal::add);
    }

    public Long getId() {
        return id;
    }

    public BigDecimal getPreco() {
        return preco;
    }

    public List<Ingrediente> getIngredientes() {
        return ingredientes;
    }

    public BigDecimal getCustoFixo() {
        return PRECO_MAO_DE_OBRA.add(PRECO_MASSA);
    }
}
