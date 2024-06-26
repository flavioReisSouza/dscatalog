package com.devsuperior.dscatalog.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.devsuperior.dscatalog.dto.ProductDTO;
import com.devsuperior.dscatalog.entities.Product;
import com.devsuperior.dscatalog.repositories.ProductRepository;
import com.devsuperior.dscatalog.services.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class ProductServiceIT {

  @Autowired private ProductService service;
  @Autowired private ProductRepository repository;

  private Long existingId;
  private Long nonExistingId;
  private Long countTotalProducts;
  private ProductDTO productDTO;

  @BeforeEach
  void setUp() throws Exception {

    existingId = 1L;
    nonExistingId = 1000L;
    countTotalProducts = 25L;

    productDTO = new ProductDTO();
    productDTO.setName("PS3 Tuned");
    productDTO.setDescription("PS3 with retro games");
    productDTO.setPrice(1000.0);
  }

  @Test
  @DisplayName("Should delete resource when id exists")
  void deleteShouldDeleteResourceWhenIdExists() {

    service.delete(existingId);

    assertEquals(countTotalProducts - 1, repository.count());
  }

  @Test
  @DisplayName("Should throw ResourceNotFoundException when id does not exist")
  void deleteShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
    assertThrows(ResourceNotFoundException.class, () -> service.delete(nonExistingId));
  }

  @Test
  @DisplayName("Should return page when page 0 size 10")
  void findAllPagedShouldReturnPageWhenPage0Size10() {

    PageRequest pageRequest = PageRequest.of(0, 10);

    Page<ProductDTO> result = service.findAllPaged(pageRequest);

    assertFalse(result.isEmpty());
    assertEquals(0, result.getNumber());
    assertEquals(10, result.getSize());
    assertEquals(countTotalProducts, result.getTotalElements());
  }

  @Test
  @DisplayName("Should return empty when page does not exist")
  void findAllPagedShouldReturnEmptyWhenPageDoesNotExist() {

    PageRequest pageRequest = PageRequest.of(50, 10);

    Page<ProductDTO> result = service.findAllPaged(pageRequest);

    assertTrue(result.isEmpty());
  }

  @Test
  @DisplayName("Should return sorted page when sort by name")
  void findAllPagedShouldReturnSortedPageWhenSortByName() {

    PageRequest pageRequest = PageRequest.of(0, 10, Sort.by("name"));

    Page<ProductDTO> result = service.findAllPaged(pageRequest);

    assertFalse(result.isEmpty());
    assertEquals("Macbook Pro", result.getContent().get(0).getName());
    assertEquals("PC Gamer", result.getContent().get(1).getName());
    assertEquals("PC Gamer Alfa", result.getContent().get(2).getName());
  }

  @Test
  void createShouldPersistDataWhenValidData() {
    ProductDTO result = service.create(productDTO);

    assertNotNull(result.getId());
    assertEquals(productDTO.getName(), result.getName());
    assertEquals(productDTO.getDescription(), result.getDescription());
    assertEquals(productDTO.getPrice(), result.getPrice());

    Product persistedProduct = repository.findById(result.getId()).orElse(null);
    assertNotNull(persistedProduct);
  }
}
