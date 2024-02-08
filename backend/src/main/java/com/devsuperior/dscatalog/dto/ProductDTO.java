package com.devsuperior.dscatalog.dto;

import com.devsuperior.dscatalog.entities.Category;
import com.devsuperior.dscatalog.entities.Product;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {

  private Long id;
  private String name;
  private String description;
  private Double price;
  private String imgUrl;
  private Instant date;
  private List<CategoryDTO> categories = new ArrayList<>();

  public ProductDTO(Product entity) {
    id = entity.getId();
    name = entity.getName();
    description = entity.getDescription();
    price = entity.getPrice();
    imgUrl = entity.getImgUrl();
    date = entity.getDate();
  }

  public ProductDTO(Product entity, Set<Category> categories) {
    this(entity);
    categories.forEach(cat -> this.categories.add(new CategoryDTO(cat)));
  }
}
