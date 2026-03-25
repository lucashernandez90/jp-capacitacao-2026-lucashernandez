package com.br.com.indra.jp_capacitacao_2026.service.dto;

import com.br.com.indra.jp_capacitacao_2026.model.enums.StatusCarrinho;

import java.math.BigDecimal;
import java.util.List;

public record CarrinhoDTO(
        Long carrinhoId,
        Long userId,
        StatusCarrinho status,
        List<CarrinhoItemDTO> itens,
        BigDecimal total
) {}
