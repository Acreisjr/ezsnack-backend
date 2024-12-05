package com.tispucminas.sistemaezsnack.model;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Conta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private BigDecimal saldo;

    private BigDecimal limiteDiario;

    private BigDecimal limiteDiarioPadrao;

    private LocalDate dataUltimoPedido;

    private boolean temLimiteDiario; 

    @ManyToOne
    @JoinColumn(name = "responsavel_id", referencedColumnName = "id")
    private Responsavel responsavel;

    @OneToOne
    @JoinColumn(name = "aluno_id", referencedColumnName = "id", nullable = false)
    @JsonManagedReference
    private Aluno aluno;

    public void aumentarSaldo(BigDecimal saldo) {
        this.saldo = this.saldo.add(saldo);
    }

    public void diminuirSaldo(BigDecimal saldo) {
        this.saldo = this.saldo.subtract(saldo);
    }

    public void resetLimiteDiarioCasoNovoDia() {
        if (!temLimiteDiario) {
            return;
        }

        LocalDate hoje = LocalDate.now();
        if (dataUltimoPedido == null || !dataUltimoPedido.equals(hoje)) {
            this.limiteDiario = this.limiteDiarioPadrao;
            this.dataUltimoPedido = hoje;
        }
    }
}
