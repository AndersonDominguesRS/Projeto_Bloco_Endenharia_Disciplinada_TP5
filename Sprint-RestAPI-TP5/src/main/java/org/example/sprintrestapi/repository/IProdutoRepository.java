package org.example.sprintrestapi.repository;

import org.example.sprintrestapi.model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IProdutoRepository extends JpaRepository<Produto, Integer> {


}