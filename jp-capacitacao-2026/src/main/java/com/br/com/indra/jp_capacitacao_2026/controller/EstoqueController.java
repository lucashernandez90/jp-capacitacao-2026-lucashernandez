package com.br.com.indra.jp_capacitacao_2026.controller;

import com.br.com.indra.jp_capacitacao_2026.model.enums.TipoTransacao;
import com.br.com.indra.jp_capacitacao_2026.service.EstoqueService;
import com.br.com.indra.jp_capacitacao_2026.service.dto.EstoqueDTO;
import com.br.com.indra.jp_capacitacao_2026.service.dto.TransacaoDTO;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Tag(name = "Estoque", description = "Endpoints para controle de estoque")
@RequestMapping("/inventory")

public class EstoqueController {

    private final EstoqueService estoqueService;

    @PostMapping("/{productId}/add")
    public ResponseEntity<EstoqueDTO> adicionar(@PathVariable Long productId,
                                                @RequestBody TransacaoDTO dto) {
        return ResponseEntity.ok(estoqueService.adicionar(productId, dto, TipoTransacao.ENTRADA));
    }

    @PostMapping("/{productId}/remove")
    public ResponseEntity<EstoqueDTO> remover(@PathVariable Long productId,
                                              @RequestBody TransacaoDTO dto) {
        return ResponseEntity.ok(estoqueService.remover(productId, dto));
    }

    @GetMapping("/{productId}")
    public ResponseEntity<EstoqueDTO> consultar(@PathVariable Long productId) {
        return ResponseEntity.ok(estoqueService.consultar(productId));
    }
}