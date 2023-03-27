package org.cafe.model;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MenuDish implements Serializable {
    @SerializedName("menu_dish_id")
    private int id;

    @SerializedName("menu_dish_card")
    private int card;

    @SerializedName("menu_dish_price")
    private double price;

    @SerializedName("menu_dish_active")
    private boolean active;
}

