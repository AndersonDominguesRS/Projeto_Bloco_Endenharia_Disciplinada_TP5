package org.example.sprintrestapi.controller;

import org.example.sprintrestapi.model.Produto;
import org.example.sprintrestapi.service.ProdutoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/produtos")
@CrossOrigin("*")
public class ProdutoController {

    private final ProdutoService produtoService;

    public ProdutoController(ProdutoService produtoService) {
        this.produtoService = produtoService;
    }

    @GetMapping
    public ResponseEntity<List<Produto>> consultarProdutos() {
        return ResponseEntity.ok(produtoService.findALL());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> consultarPorId(@PathVariable Integer id) {
        try {
            Produto produto = produtoService.findById(id);
            return ResponseEntity.ok(produto);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("erro", e.getMessage()));
        }
    }

    @PostMapping
    public ResponseEntity<?> criar(@RequestBody Produto produto) {
        try {
            Produto salvo = produtoService.save(produto);
            return ResponseEntity.ok(salvo);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("erro", e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(@PathVariable Integer id, @RequestBody Produto produto) {
        try {
            Produto existente = produtoService.findById(id);

            existente.setNome(produto.getNome());
            existente.setQuantidade(produto.getQuantidade());
            existente.setPreco(produto.getPreco());

            Produto atualizado = produtoService.save(existente);
            return ResponseEntity.ok(atualizado);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("erro", e.getMessage()));
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<?> excluir(@PathVariable Integer id) {
        try {
            produtoService.delete(id);
            return ResponseEntity.ok(Map.of("mensagem", "Produto removido com sucesso"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("erro", e.getMessage()));
        }
    }
}
