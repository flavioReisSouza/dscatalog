package com.devsuperior.dscatalog.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.devsuperior.dscatalog.dto.ProductDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;
import tests.Factory;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class ProductControllerIT {

  @Autowired private MockMvc mockMvc;
  @Autowired private ObjectMapper objectMapper;

  private Long existingId;
  private Long nonExistingId;
  private Long countTotalProducts;
  private ProductDTO productDTO;

  @BeforeEach
  void setUp() throws Exception {

    existingId = 1L;
    nonExistingId = 1000L;
    countTotalProducts = 25L;

    productDTO = Factory.createProductDTO();
    productDTO.setName("PS3 Tuned");
    productDTO.setDescription("PS3 with retro games");
    productDTO.setPrice(100.0);
  }

  @Test
  @DisplayName("Should return sorted page when sort by name")
  void findAllShouldReturnSortedPageWhenSortByName() throws Exception {

    ResultActions result =
        mockMvc.perform(
            get("/products?page=0&size=10&sort=name,asc").accept(MediaType.APPLICATION_JSON));

    result.andExpect(status().isOk());
    result.andExpect(jsonPath("$.totalElements").value(countTotalProducts));
    result.andExpect(jsonPath("$.content").exists());
    result.andExpect(jsonPath("$.content[0].name").value("Macbook Pro"));
    result.andExpect(jsonPath("$.content[1].name").value("PC Gamer"));
    result.andExpect(jsonPath("$.content[2].name").value("PC Gamer Alfa"));
  }

  @Test
  @DisplayName("Should return page when findById")
  void findByIdShouldReturnPageWhenFindById() throws Exception {

    ResultActions result =
        mockMvc.perform(get("/products/{id}", existingId).accept(MediaType.APPLICATION_JSON));

    result.andExpect(status().isOk());
    result.andExpect(jsonPath("$.id").value(existingId));
  }

  @Test
  @DisplayName("Should throw NotFoundException when id does not exist")
  void findByIdShouldThrowNotFoundExceptionWhenIdDoesNotExist() throws Exception {

    ResultActions result =
        mockMvc.perform(get("/products/{id}", nonExistingId).accept(MediaType.APPLICATION_JSON));

    result.andExpect(status().isNotFound());
  }

  @Test
  @DisplayName("Should create product when valid data")
  void createShouldReturnProductDTOWhenValidData() throws Exception {
    String jsonBody = objectMapper.writeValueAsString(productDTO);

    ResultActions result =
        mockMvc.perform(
            post("/products")
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

    result.andExpect(status().isCreated());
    result.andExpect(jsonPath("$.id").exists());
    result.andExpect(jsonPath("$.name").value(productDTO.getName()));
    result.andExpect(jsonPath("$.description").value(productDTO.getDescription()));
  }

  @Test
  @DisplayName("Should return productDTO when id exists")
  void updateShouldReturnProductDTOWhenIdExists() throws Exception {
    String jsonBody = objectMapper.writeValueAsString(productDTO);

    String expectedName = productDTO.getName();
    String expectedDescription = productDTO.getDescription();

    ResultActions result =
        mockMvc.perform(
            put("/products/{id}", existingId)
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

    result.andExpect(status().isOk());
    result.andExpect(jsonPath("$.id").value(existingId));
    result.andExpect(jsonPath("$.name").value(expectedName));
    result.andExpect(jsonPath("$.description").value(expectedDescription));
  }

  @Test
  @DisplayName("Update should throw NotFound when id does not exist")
  void updateShouldThrowNotFoundWhenIdDoesNotExist() throws Exception {
    String jsonBody = objectMapper.writeValueAsString(productDTO);

    ResultActions result =
        mockMvc.perform(
            put("/products/{id}", nonExistingId)
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

    result.andExpect(status().isNotFound());
  }
}
