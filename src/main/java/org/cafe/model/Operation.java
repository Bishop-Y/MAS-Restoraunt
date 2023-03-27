package org.cafe.model;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Operation implements Serializable {
    @SerializedName("oper_type")
    private int type;

    @SerializedName("equip_type")
    private int equipmentType;

    @SerializedName("oper_time")
    private double time;

    @SerializedName("oper_async_point")
    private int asyncPoint;

    @SerializedName("oper_products")
    private List<OperationProduct> productsList;
}
