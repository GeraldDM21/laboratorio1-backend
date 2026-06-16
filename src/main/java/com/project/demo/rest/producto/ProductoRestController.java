package com.project.demo.rest.producto;

import com.project.demo.logic.entity.categoria.Categoria;
import com.project.demo.logic.entity.categoria.CategoriaRepository;
import com.project.demo.logic.entity.http.GlobalResponseHandler;
import com.project.demo.logic.entity.http.Meta;
import com.project.demo.logic.entity.producto.Producto;
import com.project.demo.logic.entity.producto.ProductoRepository;
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
@RequestMapping("/productos")
public class ProductoRestController {

    private final ProductoRepository productoRepository;
    private final CategoriaRepository categoriaRepository;
    private final GlobalResponseHandler globalResponseHandler;

    public ProductoRestController(ProductoRepository productoRepository,
                                  CategoriaRepository categoriaRepository,
                                  GlobalResponseHandler globalResponseHandler) {
        this.productoRepository = productoRepository;
        this.categoriaRepository = categoriaRepository;
        this.globalResponseHandler = globalResponseHandler;
    }

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getAll(@RequestParam(defaultValue = "1") int page,
                                    @RequestParam(defaultValue = "10") int size,
                                    HttpServletRequest request) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<Producto> productoPage = productoRepository.findAll(pageable);

        Meta meta = new Meta(request.getMethod(), request.getRequestURL(). toString());
        meta.setTotalPages(productoPage.getTotalPages());
        meta.setTotalElements(productoPage.getTotalElements());
        meta.setPageNumber(productoPage.getNumber() + 1);
        meta.setPageSize(productoPage.getSize());

        return globalResponseHandler.handleResponse(
                "Productos retrieved successfully",
                productoPage.getContent(),
                HttpStatus.OK,
                meta
        );
    }

    @PostMapping
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<?> save(@RequestBody Producto producto,
                                  HttpServletRequest request) {
        if (producto.getCategoria() != null && producto.getCategoria().getId() != null) {
            Optional<Categoria> optionalCategoria = categoriaRepository.findById(producto.getCategoria().getId());
            if (optionalCategoria.isEmpty()) {
                return globalResponseHandler.handleResponse(
                        "Categoria id " + producto.getCategoria().getId() + " not found",
                        HttpStatus.NOT_FOUND,
                        request
                );
            }
            producto.setCategoria(optionalCategoria.get());
        }
        return globalResponseHandler.handleResponse(
                "Producto created successfully",
                productoRepository.save(producto),
                HttpStatus.CREATED,
                request
        );
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<?> update(@PathVariable Long id,
                                    @RequestBody Producto producto,
                                    HttpServletRequest request) {
        Optional<Producto> optional = productoRepository.findById(id);
        if (optional.isEmpty()) {
            return globalResponseHandler.handleResponse(
                    "Producto id " + id + " not found",
                    HttpStatus.NOT_FOUND,
                    request
            );
        }
        Producto existing = optional.get();
        existing.setNombre(producto.getNombre());
        existing.setDescripcion(producto.getDescripcion());
        existing.setPrecio(producto.getPrecio());
        existing.setCantidadStock(producto.getCantidadStock());

        if (producto.getCategoria() != null && producto.getCategoria().getId() != null) {
            Optional<Categoria> optionalCategoria = categoriaRepository.findById(producto.getCategoria().getId());
            if (optionalCategoria.isEmpty()) {
                return globalResponseHandler.handleResponse(
                        "Categoria id " + producto.getCategoria().getId() + " not found",
                        HttpStatus.NOT_FOUND,
                        request
                );
            }
            existing.setCategoria(optionalCategoria.get());
        }
        return globalResponseHandler.handleResponse(
                "Producto updated successfully",
                productoRepository.save(existing),
                HttpStatus.OK,
                request
        );
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<?> delete(@PathVariable Long id,
                                    HttpServletRequest request) {
        Optional<Producto> optional = productoRepository.findById(id);
        if (optional.isPresent()) {
            productoRepository.deleteById(id);
            return globalResponseHandler.handleResponse(
                    "Producto deleted successfully",
                    HttpStatus.OK,
                    request
            );
        }
        return globalResponseHandler.handleResponse(
                "Producto id " + id + " not found",
                HttpStatus.NOT_FOUND,
                request
        );
    }

}