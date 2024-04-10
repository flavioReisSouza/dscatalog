package tests;

import com.devsuperior.dscatalog.dto.ProductDTO;
import com.devsuperior.dscatalog.entities.Category;
import com.devsuperior.dscatalog.entities.Product;
import java.time.Instant;

@SuppressWarnings("unused")
public class Factory {

  public static Product createProduct() {
    Product product = new Product();
    product.setId(1L);
    product.setName("Phone");
    product.setDescription("Good Phone");
    product.setPrice(800.0);
    product.setImgUrl("https://img.com/img.png");
    product.setDate(Instant.parse("2021-04-18T03:00:00Z"));
    product.getCategories().add(new Category(2L, "Electronics", Instant.now(), Instant.now()));
    return product;
  }

  public static ProductDTO createProductDTO() {
    Product product = createProduct();
    return new ProductDTO(product, product.getCategories());
  }

  public static Category createCategory() {
    return new Category(2L, "Electronics", Instant.now(), Instant.now());
  }
}
