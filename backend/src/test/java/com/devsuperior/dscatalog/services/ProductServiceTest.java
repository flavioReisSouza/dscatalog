package com.devsuperior.dscatalog.services;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.devsuperior.dscatalog.dto.ProductDTO;
import com.devsuperior.dscatalog.entities.Category;
import com.devsuperior.dscatalog.entities.Product;
import com.devsuperior.dscatalog.repositories.CategoryRepository;
import com.devsuperior.dscatalog.repositories.ProductRepository;
import com.devsuperior.dscatalog.services.exceptions.DatabaseException;
import com.devsuperior.dscatalog.services.exceptions.ResourceNotFoundException;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import tests.Factory;

@ExtendWith(SpringExtension.class)
class ProductServiceTest {

  @SuppressWarnings("unused")
  @InjectMocks
  private ProductService service;

  @Mock private ProductRepository repository;

  @SuppressWarnings("unused")
  @Mock
  private CategoryRepository categoryRepository;

  private long existingId;
  private long nonExistingId;
  private long dependentId;

  @BeforeEach
  void setUp() {
    existingId = 1L;
    nonExistingId = 2L;
    dependentId = 3L;

    Product product = Factory.createProduct();
    Category category = Factory.createCategory();
    PageImpl<Product> page = new PageImpl<>(List.of(product));

    doThrow(DatabaseException.class).when(repository).deleteById(dependentId);

    when(repository.existsById(existingId)).thenReturn(true);
    when(repository.existsById(nonExistingId)).thenReturn(false);
    when(repository.existsById(dependentId)).thenReturn(true);

    when(repository.findAll(any(Pageable.class))).thenReturn(page);

    when(repository.save(any())).thenReturn(product);

    when(repository.findById(existingId)).thenReturn(Optional.of(product));
    when(repository.findById(nonExistingId)).thenReturn(Optional.empty());

    when(repository.getReferenceById(existingId)).thenReturn(product);
    when(categoryRepository.getReferenceById(existingId)).thenReturn(category);
    when(repository.getReferenceById(nonExistingId)).thenThrow(ResourceNotFoundException.class);
  }

  @Test
  @DisplayName("Delete should do nothing when id exists")
  void deleteShouldDoNothingWhenIdExists() {

    assertDoesNotThrow(() -> service.delete(existingId));

    verify(repository, times(1)).deleteById(existingId);
  }

  @Test
  @DisplayName("Delete should throw ResourceNotFoundException when id does not exist")
  void deleteShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
    assertThrows(ResourceNotFoundException.class, () -> service.delete(nonExistingId));
  }

  @Test
  @DisplayName("Delete should throw DatabaseException when id is dependent")
  void deleteShouldThrowDatabaseExceptionWhenIdIsDependent() {
    assertThrows(DatabaseException.class, () -> service.delete(dependentId));
  }

  @Test
  @DisplayName("FindAllPaged should return page")
  void findAllPagedShouldReturnPage() {
    Pageable pageable = PageRequest.of(0, 10);

    Page<ProductDTO> result = service.findAllPaged(pageable);

    assertNotNull(result);
    verify(repository, times(1)).findAll(pageable);
  }

  @Test
  @DisplayName("FindById should return ProductDTO when id exists")
  void findByIdShouldReturnProductDTOWhenIdExists() {
    ProductDTO result = service.findById(existingId);

    assertNotNull(result);
    verify(repository, times(1)).findById(existingId);
  }

  @Test
  @DisplayName("FindById should throw ResourceNotFoundException when id does not exist")
  void findByIdShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
    assertThrows(ResourceNotFoundException.class, () -> service.findById(nonExistingId));
  }

  @Test
  @DisplayName("Update should return ProductDTO when id exists")
  void updateShouldReturnProductDTOWhenIdExists() {
    ProductDTO dto = Factory.createProductDTO();
    ProductDTO result = service.update(existingId, dto);

    assertNotNull(result);
    verify(repository, times(1)).getReferenceById(existingId);
    verify(repository, times(1)).save(any(Product.class));
  }

  @Test
  @DisplayName("Create should call save method of ProductRepository when ProductDTO is valid")
  void createShouldCallSaveMethodOfProductRepositoryWhenProductDTOIsValid() {
    ProductDTO dto = Factory.createProductDTO();
    ProductDTO result = service.create(dto);

    verify(repository, times(1)).save(any(Product.class));

    assertEquals(dto.getName(), result.getName());
    assertEquals(dto.getDescription(), result.getDescription());
    assertEquals(dto.getPrice(), result.getPrice());
  }
}
