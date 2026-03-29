package org.example.sprintrestapi.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@DiscriminatorValue("PADRAO")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProdutoPadrao extends Produto {

    public ProdutoPadrao(String nome, Integer quantidade, Double preco) {
        super(nome, quantidade, preco);
    }

    @Override
    public Double getPrecoVenda() {
        return this.getPreco();
    }
}