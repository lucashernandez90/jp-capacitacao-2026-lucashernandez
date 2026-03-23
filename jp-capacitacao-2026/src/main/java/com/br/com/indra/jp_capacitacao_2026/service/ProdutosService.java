package com.br.com.indra.jp_capacitacao_2026.service;

import com.br.com.indra.jp_capacitacao_2026.model.HistoricoPreco;
import com.br.com.indra.jp_capacitacao_2026.model.Produtos;
import com.br.com.indra.jp_capacitacao_2026.repository.HistoricoPrecoRepository;
import com.br.com.indra.jp_capacitacao_2026.repository.ProdutosRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProdutosService {

    private final ProdutosRepository produtosRepository;
    private final HistoricoPrecoRepository historicoPrecoRepository;

    public List<Produtos> getAll() {
        return produtosRepository.findAll();
    }

    public Produtos createdProduto(Produtos produto) {
        return produtosRepository.save(produto);
    }

    public Produtos atualiza(Produtos produto) {
        return produtosRepository.save(produto);
    }

    public void deletarProduto(Long id) {
        produtosRepository.deleteById(id);
    }

    public Produtos getById(Long id) {
        return produtosRepository.findById(id).get();
    }

    public Produtos atualizaPreco(Long id, BigDecimal preco) {
        final var produto = produtosRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));

        final var historico = new HistoricoPreco();
        historico.setPrecoAntigo(produto.getPreco());
        historico.setPrecoNovo(preco);
        historico.setProdutos(produto);
        historicoPrecoRepository.save(historico);

        produto.setPreco(preco);
        return produtosRepository.saveAndFlush(produto);
    }
}