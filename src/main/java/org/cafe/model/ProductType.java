package org.cafe.model;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductType implements Serializable {
    @SerializedName("prod_type")
    private int id;
    @SerializedName("prod_type_name")
    private String name;
    @SerializedName("prod_is_food")
    private boolean isFood;
}

