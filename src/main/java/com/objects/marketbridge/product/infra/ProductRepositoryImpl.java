package com.objects.marketbridge.product.infra;

import com.objects.marketbridge.common.domain.Product;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class ProductRepositoryImpl implements ProductRepository {

    private final ProductJpaRepository productJpaRepository;

    @Override
    public List<Product> findAll() {
        return productJpaRepository.findAll();
    }

    @Override
    public Optional<Product> findById(Long id) {
        return productJpaRepository.findById(id);
    }

    @Override
    public List<Product> findAllById(List<Long> ids) {
        return productJpaRepository.findAllById(ids);
    }

    @Override
    public List<Product> findByName(String name) {
        return productJpaRepository.findByName(name);
    }

    @Override
    public void deleteAllInBatch() {
        productJpaRepository.deleteAllInBatch();
    }

    @Override
    public Product save(Product product) {
        return productJpaRepository.save(product);
    }

    @Override
    public void saveAll(List<Product> products) {
        productJpaRepository.saveAll(products);
    }

    @Override
    public void delete(Product product) {
        productJpaRepository.delete(product);
    }
}
