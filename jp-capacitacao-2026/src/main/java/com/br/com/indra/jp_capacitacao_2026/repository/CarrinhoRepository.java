package com.br.com.indra.jp_capacitacao_2026.repository;


import com.br.com.indra.jp_capacitacao_2026.model.Carrinho;
import com.br.com.indra.jp_capacitacao_2026.model.enums.StatusCarrinho;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CarrinhoRepository extends JpaRepository<Carrinho,Long> {
    Optional<Carrinho> findByUserIdAndStatus(Long userId, StatusCarrinho status);
}
