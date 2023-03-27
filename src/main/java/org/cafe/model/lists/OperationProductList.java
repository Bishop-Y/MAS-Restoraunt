package org.cafe.model.lists;
import org.cafe.model.OperationProduct;
import java.util.List;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OperationProductList {
    @SerializedName("oper_products")
    private List<OperationProduct> operationProducts;
}
