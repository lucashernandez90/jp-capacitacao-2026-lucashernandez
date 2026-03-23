package com.br.com.indra.jp_capacitacao_2026.controller;

import com.br.com.indra.jp_capacitacao_2026.model.Categoria;
import com.br.com.indra.jp_capacitacao_2026.service.CategoriaService;
import com.br.com.indra.jp_capacitacao_2026.service.dto.CategoriaDTO;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "Categorias", description = "Endpoints para gerenciamento de categorias")
@RequestMapping("/categorias")
public class CategoriaController {

    private final CategoriaService categoriaService;

    @GetMapping
    public ResponseEntity<List<Categoria>> getAll() {
        return ResponseEntity.ok(categoriaService.getAll());
    }

    @PostMapping
    public ResponseEntity<Categoria> criar(@RequestBody CategoriaDTO dto) {
        return ResponseEntity.ok(categoriaService.criar(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Categoria> atualizar(@PathVariable Long id,
                                               @RequestBody CategoriaDTO dto) {
        return ResponseEntity.ok(categoriaService.atualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        categoriaService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
