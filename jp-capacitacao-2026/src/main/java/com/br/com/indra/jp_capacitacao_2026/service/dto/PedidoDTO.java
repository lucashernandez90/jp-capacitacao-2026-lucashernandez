package com.br.com.indra.jp_capacitacao_2026.service.dto;

import com.br.com.indra.jp_capacitacao_2026.model.enums.StatusPedido;

import java.util.List;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PedidoDTO(
        Long pedidoId,
        Long userId,
        StatusPedido status,
        List<PedidoItemDTO> itens,
        BigDecimal total,
        LocalDateTime criadoEm
) {}
