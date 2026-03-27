package com.br.com.indra.jp_capacitacao_2026.repository;

import com.br.com.indra.jp_capacitacao_2026.model.PedidoItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PedidoItemRepository extends JpaRepository<PedidoItem, Long> {
}
