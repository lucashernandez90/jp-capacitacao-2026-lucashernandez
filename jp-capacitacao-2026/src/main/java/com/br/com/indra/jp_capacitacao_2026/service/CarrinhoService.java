package com.br.com.indra.jp_capacitacao_2026.service;

import com.br.com.indra.jp_capacitacao_2026.model.Carrinho;
import com.br.com.indra.jp_capacitacao_2026.model.CarrinhoItem;
import com.br.com.indra.jp_capacitacao_2026.model.enums.StatusCarrinho;
import com.br.com.indra.jp_capacitacao_2026.repository.CarrinhoItemRepository;
import com.br.com.indra.jp_capacitacao_2026.repository.CarrinhoRepository;
import com.br.com.indra.jp_capacitacao_2026.repository.ProdutosRepository;
import com.br.com.indra.jp_capacitacao_2026.service.dto.CarrinhoDTO;
import com.br.com.indra.jp_capacitacao_2026.service.dto.CarrinhoItemDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CarrinhoService {

    private final CarrinhoRepository carrinhoRepository;
    private final CarrinhoItemRepository carrinhoItemRepository;
    private final ProdutosRepository produtosRepository;

    public CarrinhoDTO getCarrinho(Long userId) {
        try {
            final var carrinho = buscarOuCriarCarrinho(userId);
            return toDTO(carrinho);
        } catch (RuntimeException e) {
            throw new RuntimeException("Erro ao buscar carrinho: " + e.getMessage());
        }
    }

    public CarrinhoDTO adicionarItem(Long userId, Long produtoId, Integer quantidade) {
        try {
            final var carrinho = buscarOuCriarCarrinho(userId);
            final var produto = produtosRepository.findById(produtoId)
                    .orElseThrow(() -> new RuntimeException("Produto não encontrado"));
            final var item = new CarrinhoItem();
            item.setCarrinho(carrinho);
            item.setProduto(produto);
            item.setQuantidade(quantidade);
            item.setPrecoSnapshot(produto.getPreco());
            carrinhoItemRepository.save(item);
            return toDTO(buscarOuCriarCarrinho(userId));
        } catch (RuntimeException e) {
            throw new RuntimeException("Erro ao adicionar item: " + e.getMessage());
        }
    }

    public CarrinhoDTO atualizarItem(Long itemId, Integer quantidade) {
        try {
            final var item = carrinhoItemRepository.findById(itemId)
                    .orElseThrow(() -> new RuntimeException("Item não encontrado"));
            item.setQuantidade(quantidade);
            carrinhoItemRepository.save(item);
            return toDTO(item.getCarrinho());
        } catch (RuntimeException e) {
            throw new RuntimeException("Erro ao atualizar item: " + e.getMessage());
        }
    }

    public CarrinhoDTO removerItem(Long itemId) {
        try {
            final var item = carrinhoItemRepository.findById(itemId)
                    .orElseThrow(() -> new RuntimeException("Item não encontrado"));
            final var carrinho = item.getCarrinho();
            carrinhoItemRepository.delete(item);
            return toDTO(carrinho);
        } catch (RuntimeException e) {
            throw new RuntimeException("Erro ao remover item: " + e.getMessage());
        }
    }

    private Carrinho buscarOuCriarCarrinho(Long userId) {
        return carrinhoRepository.findByUserIdAndStatus(userId, StatusCarrinho.ATIVO)
                .orElseGet(() -> {
                    final var novoCarrinho = new Carrinho();
                    novoCarrinho.setUserId(userId);
                    novoCarrinho.setStatus(StatusCarrinho.ATIVO);
                    return carrinhoRepository.save(novoCarrinho);
                });
    }

    private CarrinhoDTO toDTO(Carrinho carrinho) {
        final List<CarrinhoItemDTO> itens = carrinho.getItens().stream()
                .map(i -> new CarrinhoItemDTO(
                        i.getId(),
                        i.getProduto().getId(),
                        i.getProduto().getNome(),
                        i.getQuantidade(),
                        i.getPrecoSnapshot(),
                        i.getPrecoSnapshot().multiply(BigDecimal.valueOf(i.getQuantidade()))
                ))
                .toList();

        final var total = itens.stream()
                .map(CarrinhoItemDTO::subtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new CarrinhoDTO(
                carrinho.getId(),
                carrinho.getUserId(),
                carrinho.getStatus(),
                itens,
                total
        );
    }
}