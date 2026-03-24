package com.br.com.indra.jp_capacitacao_2026.service;

import com.br.com.indra.jp_capacitacao_2026.model.Categoria;
import com.br.com.indra.jp_capacitacao_2026.repository.CategoriaRepository;
import com.br.com.indra.jp_capacitacao_2026.service.dto.CategoriaDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;

    public List<Categoria> getAll() { return categoriaRepository.findAll(); }

    public Categoria criar(CategoriaDTO dto) {
        try{
            if (categoriaRepository.existsByNomeAndCategoriaPaiId(dto.nome(), dto.categoriaPaiId())) {
                throw new RuntimeException("Ja existe uma categoria com esse nome neste nivel");
            }

            final var categoria = new Categoria();
            categoria.setNome(dto.nome());

            if (dto.categoriaPaiId() != null) {
                final var pai = categoriaRepository.findById(dto.categoriaPaiId())
                        .orElseThrow(() -> new RuntimeException("Categoria pai nao encontrada"));
                categoria.setCategoriaPai(pai);
        }
            return categoriaRepository.save(categoria);
        } catch (RuntimeException e) {
            throw new RuntimeException("Erro ao criar categoria: " + e.getMessage());
        }
    }

    public Categoria atualizar(Long id, CategoriaDTO dto) {
        final var categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoria nao encontrada"));
        categoria.setNome(dto.nome());
        return categoriaRepository.save(categoria);
    }

    public void deletar(Long id) {
        categoriaRepository.deleteById(id);
    }
}