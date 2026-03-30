package org.example.sprintrestapi.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@DiscriminatorValue("PROMOCIONAL")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProdutoPromocional extends Produto {

    public ProdutoPromocional(String nome, Integer quantidade, Double preco) {
        super(nome, quantidade, preco);
    }

    @Override
    public Double getPrecoVenda( ) {
        return this.getPreco() != null ? this.getPreco() * 0.90 : 0.0;

    }
}