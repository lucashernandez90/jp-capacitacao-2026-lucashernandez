package com.br.com.indra.jp_capacitacao_2026.service;

import com.br.com.indra.jp_capacitacao_2026.model.*;
import com.br.com.indra.jp_capacitacao_2026.model.enums.StatusCarrinho;
import com.br.com.indra.jp_capacitacao_2026.model.enums.StatusPedido;
import com.br.com.indra.jp_capacitacao_2026.repository.CarrinhoRepository;
import com.br.com.indra.jp_capacitacao_2026.repository.PedidoRepository;
import com.br.com.indra.jp_capacitacao_2026.repository.ProdutosRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final ProdutosRepository produtosRepository;
    private final CarrinhoRepository carrinhoRepository;

    public Pedido criarPedido(List<Long> produtosIds) {

        if (produtosIds == null || produtosIds.isEmpty()) {
            throw new RuntimeException("Lista de produtos vazia");
        }

        Pedido pedido = new Pedido();
        pedido.setStatusPedido(StatusPedido.CREATED);
        pedido.setCriadoEm(LocalDateTime.now());

        List<PedidoItem> itens = new ArrayList<>();
        BigDecimal total = BigDecimal.ZERO;

        for (Long id : produtosIds) {

            Produtos produto = produtosRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Produto não encontrado: " + id));

            PedidoItem item = new PedidoItem();
            item.setPedido(pedido); 
            item.setProduto(produto);
            item.setQuantidade(1);
            item.setPrecoSnapshot(produto.getPreco());

            total = total.add(produto.getPreco());

            itens.add(item);
        }

        pedido.setItens(itens);
        pedido.setTotal(total);

        return pedidoRepository.save(pedido);
    }

    public Pedido buscarPorId(Long id) {
        return pedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado"));
    }

    public Pedido pagarPedido(Long id) {
        Pedido pedido = buscarPorId(id);

        if (pedido.getStatusPedido() != StatusPedido.CREATED) {
            throw new RuntimeException("Pedido só pode ser pago se estiver CREATED");
        }

        pedido.setStatusPedido(StatusPedido.PAID);
        return pedidoRepository.save(pedido);
    }

    public Pedido enviarPedido(Long id) {
        Pedido pedido = buscarPorId(id);

        if (pedido.getStatusPedido() != StatusPedido.PAID) {
            throw new RuntimeException("Pedido só pode ser enviado se estiver PAID");
        }

        pedido.setStatusPedido(StatusPedido.SHIPPED);
        return pedidoRepository.save(pedido);
    }

    public Pedido entregarPedido(Long id) {
        Pedido pedido = buscarPorId(id);

        if (pedido.getStatusPedido() != StatusPedido.SHIPPED) {
            throw new RuntimeException("Pedido só pode ser entregue se estiver SHIPPED");
        }

        pedido.setStatusPedido(StatusPedido.DELIVERED);
        return pedidoRepository.save(pedido);
    }

    public Pedido cancelarPedido(Long id) {
        Pedido pedido = buscarPorId(id);

        if (pedido.getStatusPedido() == StatusPedido.SHIPPED ||
                pedido.getStatusPedido() == StatusPedido.DELIVERED) {
            throw new RuntimeException("Pedido não pode ser cancelado");
        }

        pedido.setStatusPedido(StatusPedido.CANCELLED);
        return pedidoRepository.save(pedido);
    }

    @Transactional
    public Pedido criarPedidoCarrinhoAtivo(Long userId) {

        Carrinho carrinho = carrinhoRepository.findByUserIdAndStatus(userId, StatusCarrinho.ATIVO)
                .orElseThrow(() -> new RuntimeException("Nenhum carrinho ativo encontrado para o usuário: " + userId));

        if (carrinho.getItens().isEmpty()) {
            throw new RuntimeException("Carrinho vazio não pode ser convertido em pedido");
        }

        if (pedidoRepository.existsByCarrinhoId(carrinho.getId())) {
            throw new RuntimeException("Este carrinho já foi convertido em pedido");
        }

        Pedido pedido = new Pedido();
        pedido.setCarrinhoId(carrinho.getId());
        pedido.setUserId(userId);
        pedido.setStatusPedido(StatusPedido.CREATED);
        pedido.setCriadoEm(LocalDateTime.now());

        List<PedidoItem> itens = new ArrayList<>();
        BigDecimal total = BigDecimal.ZERO;

        for (CarrinhoItem ci : carrinho.getItens()) {
            Produtos produto = produtosRepository.findById(ci.getProduto().getId())
                    .orElseThrow(() -> new RuntimeException("Produto não encontrado: " + ci.getProduto().getId()));

            PedidoItem item = new PedidoItem();
            item.setPedido(pedido);
            item.setProduto(produto);
            item.setQuantidade(ci.getQuantidade());
            item.setPrecoSnapshot(produto.getPreco());

            BigDecimal subtotal = produto.getPreco().multiply(BigDecimal.valueOf(ci.getQuantidade()));
            total = total.add(subtotal);

            itens.add(item);
        }

        pedido.setItens(itens);
        pedido.setTotal(total);

        carrinho.setStatus(StatusCarrinho.FINALIZADO);
        carrinhoRepository.save(carrinho);

        return pedidoRepository.save(pedido);
    }
}