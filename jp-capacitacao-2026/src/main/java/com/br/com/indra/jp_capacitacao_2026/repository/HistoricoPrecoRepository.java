package com.br.com.indra.jp_capacitacao_2026.repository;

import com.br.com.indra.jp_capacitacao_2026.model.HistoricoPreco;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Repository
public interface HistoricoPrecoRepository extends JpaRepository<HistoricoPreco, String> {
    List<HistoricoPreco> findByProdutosId(Long produtosId);
}
