package com.project.demo.rest.categoria;

import com.project.demo.logic.entity.categoria.Categoria;
import com.project.demo.logic.entity.categoria.CategoriaRepository;
import com.project.demo.logic.entity.http.GlobalResponseHandler;
import com.project.demo.logic.entity.http.Meta;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/categorias")

public class CategoriaRestController {

    private final CategoriaRepository categoriaRepository;
    private final GlobalResponseHandler globalResponseHandler;

    public CategoriaRestController(CategoriaRepository categoriaRepository,
                                   GlobalResponseHandler globalResponseHandler) {
        this.categoriaRepository = categoriaRepository;
        this.globalResponseHandler = globalResponseHandler;
    }
    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getAll(@RequestParam(defaultValue = "1") int page,
                                    @RequestParam(defaultValue = "10") int size,
                                    HttpServletRequest request) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<Categoria> categoriaPage = categoriaRepository.findAll(pageable);

        Meta meta = new Meta(request.getMethod(), request.getRequestURL().toString());
        meta.setTotalPages(categoriaPage.getTotalPages());
        meta.setTotalElements(categoriaPage.getTotalElements());
        meta.setPageNumber(categoriaPage.getNumber() + 1);
        meta.setPageSize(categoriaPage.getSize());

        return globalResponseHandler.handleResponse(
                "Categorias retrieved successfully",
                categoriaPage.getContent(),
                HttpStatus.OK,
                meta
        );
    }

    @PostMapping
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<?> create(@RequestBody Categoria categoria,
                                    HttpServletRequest request) {
        Categoria saved = categoriaRepository.save(categoria);
        return globalResponseHandler.handleResponse(
                "Categoria created successfully",
                saved,
                HttpStatus.CREATED,
                request
        );
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<?> update(@PathVariable Long id,
                                    @RequestBody Categoria categoria,
                                    HttpServletRequest request) {
        Optional<Categoria> optional = categoriaRepository.findById(id);
        if (optional.isPresent()) {
            Categoria existing = optional.get();
            existing.setNombre(categoria.getNombre());
            existing.setDescripcion(categoria.getDescripcion());
            return globalResponseHandler.handleResponse(
                    "Categoria updated successfully",
                    categoriaRepository.save(existing),
                    HttpStatus.OK,
                    request
            );
        }
        return globalResponseHandler.handleResponse(
                "Categoria id " + id + " not found",
                HttpStatus.NOT_FOUND,
                request
        );
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<?> delete(@PathVariable Long id,
                                    HttpServletRequest request) {
        Optional<Categoria> optional = categoriaRepository.findById(id);
        if (optional.isPresent()) {
            categoriaRepository.deleteById(id);
            return globalResponseHandler.handleResponse(
                    "Categoria deleted successfully",
                    HttpStatus.OK,
                    request
            );
        }
        return globalResponseHandler.handleResponse(
                "Categoria id " + id + " not found",
                HttpStatus.NOT_FOUND,
                request
        );
    }
}
