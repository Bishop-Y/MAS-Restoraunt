package org.cafe.model;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product implements Serializable {
    @SerializedName("prod_item_id")
    private int id;
    @SerializedName("prod_item_type")
    private int type;
    @SerializedName("prod_item_name")
    private String name;
    @SerializedName("prod_item_company")
    private String company;
    @SerializedName("prod_item_unit")
    private String unit;
    @SerializedName("prod_item_quantity")
    private double quantity;
    @SerializedName("prod_item_cost")
    private double cost;
    @SerializedName("prod_item_delivered")
    private LocalDateTime delivered;
    @SerializedName("prod_item_valid_until")
    private LocalDateTime validUntil;
}

