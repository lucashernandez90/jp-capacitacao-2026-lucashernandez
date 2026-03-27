package com.br.com.indra.jp_capacitacao_2026.controller;


import com.br.com.indra.jp_capacitacao_2026.model.Pedido;
import com.br.com.indra.jp_capacitacao_2026.service.CarrinhoService;
import com.br.com.indra.jp_capacitacao_2026.service.PedidoService;
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
    private final PedidoService pedidoService;

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

    @DeleteMapping("/{carrinhoId}")
    public ResponseEntity<Void> deletarCarrinho(@PathVariable Long carrinhoId) {
        carrinhoService.deletarCarrinho(carrinhoId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/finalizar")
    public ResponseEntity<CarrinhoDTO> finalizarCarrinho(@RequestParam Long userId) {
        return ResponseEntity.ok(carrinhoService.finalizarCarrinho(userId));
    }

    @PostMapping("/cancelar")
    public ResponseEntity<CarrinhoDTO> cancelarCarrinho(@RequestParam Long userId) {
        return ResponseEntity.ok(carrinhoService.cancelarCarrinho(userId));
    }

    @PostMapping("/{carrinhoId}/checkout")
    public ResponseEntity<Pedido> checkout(@PathVariable Long carrinhoId) {

        Pedido pedido = pedidoService.criarPedidoCarrinhoAtivo(carrinhoId);
        return ResponseEntity.ok(pedido);
    }

}
