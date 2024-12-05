package com.tispucminas.sistemaezsnack.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;
import java.util.ArrayList;

@Entity
@Getter
@Setter
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private BigDecimal precoTotal;

    private LocalDateTime data;

    private String status;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<ItemPedido> itensPedidos;

    @ManyToOne
    @JoinColumn(name = "conta_id", referencedColumnName = "id", nullable = false)
    private Conta conta;

    public Pedido(){
        this.itensPedidos = new ArrayList<ItemPedido>();
    }

    public void addItemPedido(ItemPedido itemPedido) {
        itemPedido.setPedido(this);
        this.itensPedidos.add(itemPedido);
    }
}
