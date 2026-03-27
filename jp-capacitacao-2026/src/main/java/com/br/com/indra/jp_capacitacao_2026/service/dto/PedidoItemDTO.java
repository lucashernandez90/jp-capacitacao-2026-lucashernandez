package com.br.com.indra.jp_capacitacao_2026.service.dto;

import java.math.BigDecimal;

public record PedidoItemDTO(
        Long itemId,
        String nomeProduto,
        Integer quantidade,
        BigDecimal precoSnapshot,
        BigDecimal subtotal
) {}
