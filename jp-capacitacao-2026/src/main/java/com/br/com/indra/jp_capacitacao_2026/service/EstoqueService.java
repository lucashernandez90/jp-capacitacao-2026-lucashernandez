package com.br.com.indra.jp_capacitacao_2026.service;

import com.br.com.indra.jp_capacitacao_2026.model.Carrinho;
import com.br.com.indra.jp_capacitacao_2026.model.CarrinhoItem;
import com.br.com.indra.jp_capacitacao_2026.model.Estoque;
import com.br.com.indra.jp_capacitacao_2026.model.InventoryTransaction;
import com.br.com.indra.jp_capacitacao_2026.model.enums.TipoTransacao;
import com.br.com.indra.jp_capacitacao_2026.repository.EstoqueRepository;
import com.br.com.indra.jp_capacitacao_2026.repository.InventoryTransactionRepository;
import com.br.com.indra.jp_capacitacao_2026.repository.ProdutosRepository;
import com.br.com.indra.jp_capacitacao_2026.service.dto.EstoqueDTO;
import com.br.com.indra.jp_capacitacao_2026.service.dto.TransacaoDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor

public class EstoqueService {

    private final EstoqueRepository estoqueRepository;
    private final InventoryTransactionRepository inventoryTransactionRepository;
    private final ProdutosRepository produtosRepository;

    public EstoqueDTO adicionar(Long produtoId, TransacaoDTO dto, TipoTransacao tipo){
        try{
            final var estoque = buscarOuCriar(produtoId);

            estoque.setQuantidade(estoque.getQuantidade() + dto.quantidade());
            verificarEstoqueBaixo(estoque);
            estoqueRepository.save(estoque);

            registrarTransacao(estoque.getProduto().getId(), dto.quantidade(), tipo, dto.motivo());

            return toDTO(estoque);
        } catch (RuntimeException e){
            throw new RuntimeException("Erro ao adicionar no estoque: " + e.getMessage());
        }

    }

    public EstoqueDTO remover(Long produtoId, TransacaoDTO dto) {
        try {
            final var estoque = buscarOuCriar(produtoId);

            if (estoque.getQuantidade() < dto.quantidade()) {
                throw new RuntimeException("Estoque insuficiente. Disponível: " + estoque.getQuantidade());
            }

            estoque.setQuantidade(estoque.getQuantidade() - dto.quantidade());
            verificarEstoqueBaixo(estoque);
            estoqueRepository.save(estoque);
            registrarTransacao(produtoId, -dto.quantidade(), TipoTransacao.SAIDA, dto.motivo());

            return toDTO(estoque);

        } catch (RuntimeException e) {
            throw new RuntimeException("Erro ao remover estoque: " + e.getMessage());
        }
    }

    public EstoqueDTO consultar(Long produtoId){
        return toDTO(buscarOuCriar(produtoId));
    }

    private Estoque buscarOuCriar(Long produtoId) {
        return estoqueRepository.findByProdutoId(produtoId).orElseGet(() -> {
                    final var produto = produtosRepository.findById(produtoId)
                            .orElseThrow(() -> new RuntimeException("Produto não encontrado"));
                    final var novoEstoque = new Estoque();
                    novoEstoque.setProduto(produto);
                    novoEstoque.setQuantidade(0);
                    novoEstoque.setQuantidade_minima(5);
                    return estoqueRepository.save(novoEstoque);
        });
    }

    private void verificarEstoqueBaixo(Estoque estoque){
        estoque.setEstoque_baixo(estoque.getQuantidade() <= estoque.getQuantidade_minima());
    }
    private void registrarTransacao(Long produtoId, Integer delta, TipoTransacao tipo, String motivo) {

        try{
            final var produto = produtosRepository.findById(produtoId)
                    .orElseThrow(() -> new RuntimeException("Produto não encontrado com id: " + produtoId));
            final var transacao = new InventoryTransaction();
            transacao.setProduto(produto);
            transacao.setDelta(delta);
            transacao.setTipo(tipo);
            transacao.setMotivo(motivo);
            inventoryTransactionRepository.save(transacao);
        } catch (RuntimeException e){
            throw new RuntimeException("Erro ao registrar: " + e.getMessage());
        }
    }

    public EstoqueDTO ajustar(Long produtoId, TransacaoDTO dto){
        try{
            final var estoque = buscarOuCriar(produtoId);

            int novaQuantidade = estoque.getQuantidade() + dto.quantidade();

            if (novaQuantidade < 0){
                throw new RuntimeException("Ajuste invalido: estoque nao pode ficar negativo");
            }

            estoque.setQuantidade(novaQuantidade);
            verificarEstoqueBaixo(estoque);
            estoqueRepository.save(estoque);

            registrarTransacao(produtoId, dto.quantidade(), TipoTransacao.AJUSTE, dto.motivo());

            return toDTO(estoque);
        } catch (RuntimeException e){
            throw new RuntimeException("Erro ao ajustar estoque: " + e.getMessage());
        }
    }

    public EstoqueDTO devolver(Long produtoId, TransacaoDTO dto){
        try{
            final var estoque = buscarOuCriar(produtoId);

            estoque.setQuantidade(estoque.getQuantidade() + dto.quantidade());
            verificarEstoqueBaixo(estoque);
            estoqueRepository.save(estoque);

            registrarTransacao(produtoId, dto.quantidade(), TipoTransacao.DEVOLUCAO, dto.motivo());

            return toDTO(estoque);
        } catch (RuntimeException e){
            throw new RuntimeException("Erro ao registrar devolucao: " + e.getMessage());
        }
    }

    public void baixarEstoque(Long produtoId, Integer quantidade) {

        final var estoque = buscarOuCriar(produtoId);

        if (estoque.getQuantidade() < quantidade) {
            throw new RuntimeException("Estoque insuficiente para o produto: " + produtoId + "Disponivel: - " + estoque.getQuantidade() +
                    "Solicitado" + quantidade);
        }

        estoque.setQuantidade(estoque.getQuantidade() - quantidade);
        verificarEstoqueBaixo(estoque);
        estoqueRepository.save(estoque);

        registrarTransacao(
                produtoId,
                -quantidade,
                TipoTransacao.SAIDA,
                "Venda via carrinho"
        );
    }

    private EstoqueDTO toDTO(Estoque e) {
        return new EstoqueDTO(
                e.getProduto().getId(),
                e.getProduto().getNome(),
                e.getQuantidade(),
                e.getQuantidade_minima(),
                e.getEstoque_baixo()
        );
    }
}
