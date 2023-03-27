package org.cafe.model.lists;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.cafe.model.Product;

import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductList {
    @JsonProperty("products")
    private List<Product> products;
}
