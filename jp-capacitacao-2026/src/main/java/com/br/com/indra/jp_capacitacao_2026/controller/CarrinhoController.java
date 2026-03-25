package com.br.com.indra.jp_capacitacao_2026.controller;


import com.br.com.indra.jp_capacitacao_2026.service.CarrinhoService;
import com.br.com.indra.jp_capacitacao_2026.service.dto.CarrinhoDTO;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Tag(name = "Carrinho", description = "Endpoints para gerenciamento do carrinho")
@RequestMapping("/cart")
public class CarrinhoController {

    private final CarrinhoService carrinhoService;

    @GetMapping
    public ResponseEntity<CarrinhoDTO> getCarrinho(@RequestParam Long userId) {
        return ResponseEntity.ok(carrinhoService.getCarrinho(userId));
    }

    @PostMapping("/items")
    public ResponseEntity<CarrinhoDTO> adicionarItem(@RequestParam Long userId,
                                                     @RequestParam Long produtoId,
                                                     @RequestParam Integer quantidade) {
        return ResponseEntity.ok(carrinhoService.adicionarItem(userId, produtoId, quantidade));
    }

    @PutMapping("/items/{itemId}")
    public ResponseEntity<CarrinhoDTO> atualizarItem(@PathVariable Long itemId,
                                                     @RequestParam Integer quantidade) {
        return ResponseEntity.ok(carrinhoService.atualizarItem(itemId, quantidade));
    }

    @DeleteMapping("/items/{itemId}")
    public ResponseEntity<CarrinhoDTO> removerItem(@PathVariable Long itemId) {
        return ResponseEntity.ok(carrinhoService.removerItem(itemId));
    }
}
