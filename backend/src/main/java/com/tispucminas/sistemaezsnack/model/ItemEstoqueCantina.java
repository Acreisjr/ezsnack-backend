package com.tispucminas.sistemaezsnack.model;

import java.math.BigDecimal;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ItemEstoqueCantina {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private String tipo;
    private Boolean disponivel;
    private BigDecimal preco;

    @OneToMany(mappedBy = "itemEstoqueCantina")
    @JsonBackReference 
    private List<ItemPedido> itensPedidos;

    @ManyToOne
    @JsonBackReference 
    private Escola escola; // Associação com Escola

    public void addItemPedido(ItemPedido itemPedido) {
        itemPedido.setItemEstoqueCantina(this);
        this.itensPedidos.add(itemPedido);
    }
}
