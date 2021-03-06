package com.dallab.cattoy.controller;

import com.dallab.cattoy.application.ProductService;
import com.dallab.cattoy.authentication.AuthorizeRequired;
import com.dallab.cattoy.domain.Product;
import com.dallab.cattoy.dto.ProductDto;
import com.github.dozermapper.core.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin
@RequestMapping("/products")
@RestController
public class ProductController {

    @Autowired
    Mapper mapper;

    @Autowired
    private ProductService productService;

    @GetMapping
    public List<ProductDto> list() {
        List<Product> products = productService.getProducts();

        return products.stream()
                .map(product -> mapper.map(product, ProductDto.class))
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ProductDto detail(
            @PathVariable("id") Long id
    ) {
        Product product = productService.getProduct(id);

        return mapper.map(product, ProductDto.class);
    }

    // 인증된 사용자 & 관리자만 접근 가능
    @AuthorizeRequired("ADMIN")
    @PostMapping
    public ResponseEntity<?> create(
            @Valid @RequestBody ProductDto productDto
    ) throws URISyntaxException {
        Product product = productService.addProduct(
                mapper.map(productDto, Product.class));

        URI location = new URI("/products/" + product.getId());
        return ResponseEntity.created(location).build();
    }

    @PatchMapping("/{id}")
    public void update(
            @PathVariable("id") Long id,
            @Valid @RequestBody ProductDto productDto
    ) {
        productService.updateProduct(id, productDto);
    }

    @DeleteMapping("/{id}")
    public void destroy(
            @PathVariable("id") Long id
    ) {
        productService.removeProduct(id);
    }

}
