package com.br.com.indra.jp_capacitacao_2026.repository;

import com.br.com.indra.jp_capacitacao_2026.model.CarrinhoItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CarrinhoItemRepository extends JpaRepository<CarrinhoItem, Long> {
}
