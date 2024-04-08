package com.devsuperior.dscatalog.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.devsuperior.dscatalog.entities.Product;
import java.util.Optional;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import tests.Factory;

@DataJpaTest
class ProductRepositoryTest {

  @SuppressWarnings("unused")
  @Autowired
  private ProductRepository repository;

  private long existingId;
  private long nonExistingId;
  private long countTotalProducts;
  private static Product product;

  @BeforeAll
  static void initAll() {
    product = Factory.createProduct();
  }

  @BeforeEach
  void setUp() {
    existingId = 1L;
    nonExistingId = 1000L;
    countTotalProducts = 25L;
  }

  @Test
  @DisplayName("Should return true when id exists")
  void shouldDeleteObjectWhenIdExists() {
    repository.deleteById(existingId);

    Optional<Product> result = repository.findById(existingId);

    assertFalse(result.isPresent());
  }

  @Test
  @DisplayName("Should save persist with autoincrement id is null")
  void shouldSavePersistWithAutoincrementWhenIdIsNull() {
    product.setId(null);

    product = repository.save(product);

    assertNotNull(product.getId());
    assertEquals(countTotalProducts + 1, product.getId());
  }

  @Test
  @DisplayName("Should findById return a non-empty optional when id exists")
  void shouldFindByIdReturnANonEmptyOptionalWhenIdExists() {
    Optional<Product> result = repository.findById(existingId);

    assertTrue(result.isPresent());
  }

  @Test
  @DisplayName("Should findById return a non-empty optional when Id non exists")
  void shouldFindByIdReturnANonEmptyOptionalWhenIdNonExists() {
    Optional<Product> result = repository.findById(nonExistingId);

    assertFalse(result.isPresent());
  }
}
