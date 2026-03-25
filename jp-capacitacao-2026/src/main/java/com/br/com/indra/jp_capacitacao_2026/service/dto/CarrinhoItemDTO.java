package com.br.com.indra.jp_capacitacao_2026.service.dto;

import java.math.BigDecimal;

public record CarrinhoItemDTO(
        Long itemId,
        Long produtoId,
        String nomeProduto,
        Integer quantidade,
        BigDecimal priceSnapshot,
        BigDecimal subtotal
) {}
