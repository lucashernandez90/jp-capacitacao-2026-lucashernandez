package com.br.com.indra.jp_capacitacao_2026.controller;

import com.br.com.indra.jp_capacitacao_2026.model.Pedido;
import com.br.com.indra.jp_capacitacao_2026.service.PedidoService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
@Tag(name = "Pedidos", description = "Endpoints para controle de pedidos")
public class PedidoController {

    private final PedidoService pedidoService;

    @PostMapping
    public ResponseEntity<Pedido> criarPedido(@RequestBody List<Long> produtosIds) {
        return ResponseEntity.ok(pedidoService.criarPedido(produtosIds));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pedido> getPedido(@PathVariable Long id) {
        return ResponseEntity.ok(pedidoService.buscarPorId(id));
    }

    @PostMapping("/{id}/pay")
    public ResponseEntity<Pedido> pagar(@PathVariable Long id) {
        return ResponseEntity.ok(pedidoService.pagarPedido(id));
    }

    @PostMapping("/{id}/ship")
    public ResponseEntity<Pedido> enviar(@PathVariable Long id) {
        return ResponseEntity.ok(pedidoService.enviarPedido(id));
    }

    @PostMapping("/{id}/deliver")
    public ResponseEntity<Pedido> entregar(@PathVariable Long id) {
        return ResponseEntity.ok(pedidoService.entregarPedido(id));
    }

    @PostMapping("/{id}/cancel")
    public ResponseEntity<Pedido> cancelar(@PathVariable Long id) {
        return ResponseEntity.ok(pedidoService.cancelarPedido(id));
    }

    @PostMapping("/from-active-cart")
    public ResponseEntity<Pedido> criarPedidoDoCarrinhoAtivo(@RequestParam Long userId) {
        return ResponseEntity.ok(pedidoService.criarPedidoCarrinhoAtivo(userId));
    }

    @PostMapping("/from-cart/{carrinhoId}")
    public ResponseEntity<Pedido> criarPedidoDoCarrinho(@PathVariable Long carrinhoId) {
        return ResponseEntity.ok(pedidoService.criarPedidoCarrinhoAtivo(carrinhoId));
    }
}