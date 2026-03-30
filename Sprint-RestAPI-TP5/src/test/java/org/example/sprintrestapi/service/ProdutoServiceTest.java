package org.example.sprintrestapi.service;

import org.example.sprintrestapi.model.Produto;
import org.example.sprintrestapi.repository.IProdutoRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

public class ProdutoServiceTest {

    private IProdutoRepository repository = Mockito.mock(IProdutoRepository.class);
    private ProdutoService produtoService = new ProdutoService(repository);

    @ParameterizedTest
    @CsvSource({
            "-10.0, 'valor preco nao pode ser negativo'",
            "10.0,  'Nome do produto é obrigatório'",
            "10.0,  'Quantidade deve ser ao menos 1'"
    })
    @DisplayName("Deve validar múltiplos cenários de entrada inválida")
    void validarEntradasInvalidas(double preco, String mensagemEsperada) {
        String nome = "Produto Valido";
        Integer quantidade = 10;

        if (mensagemEsperada.equals(ProdutoService.ERR_NOME)) {
            nome = "";
        } else if (mensagemEsperada.equals(ProdutoService.ERR_QTD)) {
            quantidade = 0;
        }

        Produto p = Produto.criarNovo(nome, quantidade, preco);

        Exception exception = assertThrows(IllegalArgumentException.class, ( ) -> {
            produtoService.save(p);
        });

        assertEquals(mensagemEsperada, exception.getMessage( ));
        verify(repository, times(0)).save(any());
    }

    @Test
    @DisplayName("Deve lançar exceção quando o preço for nulo")
    void validarPrecoNulo() {
        Produto p = Produto.criarNovo("Teste", 10, null);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            produtoService.save(p);
        });

        assertEquals(ProdutoService.ERR_PRECO, exception.getMessage( ));
    }

    @Test
    @DisplayName("Deve lançar exceção quando o produto for nulo")
    void validarProdutoNulo() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            produtoService.save(null);
        });
        assertEquals(ProdutoService.ERR_PRODUTO_NULL, exception.getMessage());
    }

    @Test
    @DisplayName("Deve salvar produto com sucesso quando dados são válidos")
    void deveSalvarComSucesso() {
        Produto p = Produto.criarNovo("Produto Ok", 5, 100.0);

        produtoService.save(p);

        verify(repository, times(1)).save(p);
    }
}