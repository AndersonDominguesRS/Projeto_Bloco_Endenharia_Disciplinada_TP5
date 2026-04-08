package org.example.sprintrestapi.service;

import org.example.sprintrestapi.model.*;
import org.example.sprintrestapi.repository.IProdutoRepository;
import org.example.sprintrestapi.service.ProdutoService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            produtoService.save(p);
        });

        assertEquals(mensagemEsperada, exception.getMessage());
        verify(repository, times(0)).save(any());
    }

    @Test
    @DisplayName("Deve lançar exceção quando o preço for nulo")
    void validarPrecoNulo() {
        Produto p = Produto.criarNovo("Teste", 10, null);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            produtoService.save(p);
        });

        assertEquals(ProdutoService.ERR_PRECO, exception.getMessage());
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

    @Test
    @DisplayName("Deve retornar lista de produtos")
    void deveListarProdutos() {
        when(repository.findAll()).thenReturn(Arrays.asList(
                Produto.criarNovo("A", 1, 10.0),
                Produto.criarNovo("B", 2, 20.0)
        ));

        List<Produto> lista = produtoService.findALL();

        assertEquals(2, lista.size());
    }

    @Test
    @DisplayName("Deve atualizar produto existente")
    void deveAtualizarProduto() throws Exception {
        Produto existente = Produto.criarNovo("Antigo", 1, 10.0);

        Field idField = Produto.class.getDeclaredField("id");
        idField.setAccessible(true);
        idField.set(existente, 1);

        when(repository.findById(1)).thenReturn(Optional.of(existente));
        when(repository.save(any())).thenReturn(existente);

        Produto atualizado = Produto.criarNovo("Novo", 5, 50.0);
        idField.set(atualizado, 1);

        Produto resultado = produtoService.save(atualizado);

        assertEquals("Novo", resultado.getNome());
        assertEquals(5, resultado.getQuantidade());
        assertEquals(50.0, resultado.getPreco());
    }




    @Test
    @DisplayName("Deve excluir produto pelo ID")
    void deveExcluirProduto() {
        produtoService.delete(1);
        verify(repository, times(1)).deleteById(1);
    }

    @Test
    @DisplayName("ProdutoPromocional deve aplicar desconto corretamente")
    void precoPromocional() {
        ProdutoPromocional p = new ProdutoPromocional("Promo", 1, 100.0);
        assertEquals(90.0, p.getPrecoVenda());
    }

    @Test
    @DisplayName("ProdutoPadrao deve retornar preço sem desconto")
    void precoPadrao() {
        ProdutoPadrao p = new ProdutoPadrao("Normal", 1, 100.0);
        assertEquals(100.0, p.getPrecoVenda());
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "   ", "\n", "\t", "<script>alert(1)</script>", " "})
    @DisplayName("Deve rejeitar nomes inválidos (fuzz testing)")
    void deveRejeitarNomesInvalidos(String nomeInvalido) {
        Produto p = Produto.criarNovo(nomeInvalido, 1, 10.0);

        Exception ex = assertThrows(IllegalArgumentException.class, () -> produtoService.save(p));

        assertEquals(ProdutoService.ERR_NOME, ex.getMessage());
    }

    @Test
    @DisplayName("Deve falhar quando o repositório estiver indisponível")
    void falhaNoRepositorio() {
        when(repository.save(any())).thenThrow(new RuntimeException("Falha no banco"));

        Produto p = Produto.criarNovo("Teste", 1, 10.0);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> produtoService.save(p));

        assertEquals("Falha no banco", ex.getMessage());
    }
}
