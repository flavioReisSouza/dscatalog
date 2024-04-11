package com.devsuperior.dscatalog.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.devsuperior.dscatalog.dto.ProductDTO;
import com.devsuperior.dscatalog.services.ProductService;
import com.devsuperior.dscatalog.services.exceptions.ResourceNotFoundException;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import tests.Factory;

@WebMvcTest(ProductController.class)
class ProductControllerTest {

  @SuppressWarnings("unused")
  @Autowired
  private MockMvc mockMvc;

  @SuppressWarnings("unused")
  @MockBean
  private ProductService service;

  private long existingId;
  private long nonExistingId;

  @BeforeEach
  void setUp() {
    existingId = 1L;
    nonExistingId = 2L;

    ProductDTO productDTO = Factory.createProductDTO();

    PageImpl<ProductDTO> page = new PageImpl<>(List.of(productDTO));

    when(service.findAllPaged(any())).thenReturn(page);
    when(service.findById(existingId)).thenReturn(productDTO);
    when(service.findById(nonExistingId)).thenThrow(ResourceNotFoundException.class);
  }

  @Test
  @DisplayName("Should return page")
  void findAllShouldReturnPage() throws Exception {
    ResultActions result =
            mockMvc.perform(get("/products").accept(MediaType.APPLICATION_JSON));

    result.andExpect(status().isOk());
  }

  @Test
  @DisplayName("Should return product when id exists")
  void findByIdShouldReturnProductWhenIdExists() throws Exception {
    ResultActions result =
        mockMvc.perform(get("/products/{id}", existingId).accept(MediaType.APPLICATION_JSON));

    result.andExpect(status().isOk());
    result.andExpect(jsonPath("$.id").exists());
    result.andExpect(jsonPath("$.name").exists());
    result.andExpect(jsonPath("$.description").exists());
  }

  @Test
  @DisplayName("Should throw NotFoundException when id does not exist")
  void findByIdShouldThrowNotFoundExceptionWhenIdDoesNotExist() throws Exception {
    ResultActions result =
        mockMvc.perform(get("/products/{id}", nonExistingId).accept(MediaType.APPLICATION_JSON));

    result.andExpect(status().isNotFound());
  }
}
