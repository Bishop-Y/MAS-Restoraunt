package org.cafe.model.lists;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.cafe.model.ProductType;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductTypeList {
    @SerializedName("product_types")
    private List<ProductType> productTypes;
}
