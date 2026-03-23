package com.br.com.indra.jp_capacitacao_2026.service;

import com.br.com.indra.jp_capacitacao_2026.model.HistoricoPreco;
import com.br.com.indra.jp_capacitacao_2026.repository.HistoricoPrecoRepository;
import com.br.com.indra.jp_capacitacao_2026.service.dto.HistoricoProdutoDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class HistoricoService {

    private final HistoricoPrecoRepository historicoPrecoRepository;

    public List<HistoricoProdutoDTO> getHistoricoByProdutoId(Long produtoId) {
        return historicoPrecoRepository.findByProdutosId(produtoId)
                .stream()
                .map(h -> new HistoricoProdutoDTO(
                        h.getId(),
                        h.getProdutos().getNome(),
                        h.getPrecoAntigo(),
                        h.getPrecoNovo(),
                        h.getDataAlteracao()
                ))
                .toList();
    }

}
