package org.cafe.model;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class OperationProduct implements Serializable {
    @SerializedName("prod_type")
    private int type;

    @SerializedName("prod_quantity")
    private double quantity;
}
