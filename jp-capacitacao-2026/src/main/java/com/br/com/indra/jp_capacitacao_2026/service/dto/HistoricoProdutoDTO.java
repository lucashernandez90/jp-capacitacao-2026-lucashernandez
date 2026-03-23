package com.br.com.indra.jp_capacitacao_2026.service.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record HistoricoProdutoDTO(
        UUID id,
        String produto,
        BigDecimal precoAntigo,
        BigDecimal precoNovo,
        LocalDateTime dataRegistro
) {}
