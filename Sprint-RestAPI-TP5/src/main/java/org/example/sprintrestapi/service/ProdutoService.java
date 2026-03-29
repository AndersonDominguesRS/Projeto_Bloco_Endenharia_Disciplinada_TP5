package org.example.sprintrestapi.service;

import org.example.sprintrestapi.model.Produto;
import org.example.sprintrestapi.repository.IProdutoRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ProdutoService {
    private final IProdutoRepository produtoRepository;

    public static final String ERR_PRECO = "valor preco nao pode ser negativo";
    public static final String ERR_NOME = "Nome do produto é obrigatório";
    public static final String ERR_QTD = "Quantidade deve ser ao menos 1";
    public static final String ERR_PRODUTO_NULL = "Produto nulo";

    public ProdutoService(IProdutoRepository produtoRepository) {
        this.produtoRepository = produtoRepository;
    }

    public List<Produto> findAll() {
        return produtoRepository.findAll();
    }

    /**
     * Salva um produto aplicando as Cláusulas de Guarda exigidas no TP5.
     * O método falha o mais rápido possível (fail-fast).
     */
    public Produto save(Produto produto) {
        validarProduto(produto);
        return produtoRepository.save(produto);
    }

    private void validarProduto(Produto produto) {
        // --- Cláusulas de Guarda (Substituem IFs aninhados) ---

        if (produto == null) {
            throw new IllegalArgumentException(ERR_PRODUTO_NULL);
        }

        if (produto.getPreco() == null || produto.getPreco() < 0) {
            throw new IllegalArgumentException(ERR_PRECO);
        }

        if (produto.getNome() == null || produto.getNome().isBlank()) {
            throw new IllegalArgumentException(ERR_NOME);
        }

        if (produto.getQuantidade() == null || produto.getQuantidade() < 1) {
            throw new IllegalArgumentException(ERR_QTD);
        }
    }
}