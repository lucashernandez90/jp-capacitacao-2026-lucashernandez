package com.br.com.indra.jp_capacitacao_2026.service.dto;

public record EstoqueDTO(
        Long produtoId,
        String nomeProduto,
        Integer quantidade,
        Integer quantidadeMinima,
        Boolean estoqueBaixo
) {}


