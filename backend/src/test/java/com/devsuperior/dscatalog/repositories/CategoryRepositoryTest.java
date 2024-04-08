package com.devsuperior.dscatalog.repositories;

import static org.junit.jupiter.api.Assertions.assertFalse;

import com.devsuperior.dscatalog.entities.Category;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class CategoryRepositoryTest {

  @SuppressWarnings("unused")
  @Autowired
  private CategoryRepository repository;

  private long existingId;

  @BeforeEach
  void setUp() {
    existingId = 1L;
  }

  @Test
  @DisplayName("Should return true when id exists")
  void shouldDeleteObjectWhenIdExists() {
    repository.deleteById(existingId);

    Optional<Category> result = repository.findById(existingId);

    assertFalse(result.isPresent());
  }
}
