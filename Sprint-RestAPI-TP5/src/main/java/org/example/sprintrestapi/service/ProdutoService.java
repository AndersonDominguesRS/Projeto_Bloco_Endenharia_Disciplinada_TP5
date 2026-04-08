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

    public List<Produto> findALL() {
        return produtoRepository.findAll();
    }

    public Produto findById(Integer id) {
        return produtoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Produto não encontrado"));
    }

    public Produto save(Produto produto) {
        validarProduto(produto);

        if (produto.getId() != null) {
            Produto existente = findById(produto.getId());
            existente.setNome(produto.getNome());
            existente.setQuantidade(produto.getQuantidade());
            existente.setPreco(produto.getPreco());
            return produtoRepository.save(existente);
        }

        return produtoRepository.save(produto);
    }



    public void delete(Integer id) {
        produtoRepository.deleteById(id);
    }

    private void validarProduto(Produto produto) {

        if (produto == null) {
            throw new IllegalArgumentException(ERR_PRODUTO_NULL);
        }

        if (produto.getPreco() == null || produto.getPreco() < 0) {
            throw new IllegalArgumentException(ERR_PRECO);
        }

        if (produto.getNome() == null || produto.getNome().isBlank()) {
            throw new IllegalArgumentException(ERR_NOME);
        }

        if (!produto.getNome().matches("^[A-Za-zÀ-ÖØ-öø-ÿ0-9 ]+$")) {
            throw new IllegalArgumentException(ERR_NOME);
        }

        if (produto.getQuantidade() == null || produto.getQuantidade() < 1) {
            throw new IllegalArgumentException(ERR_QTD);
        }
    }

}
