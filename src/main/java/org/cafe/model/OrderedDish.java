package org.cafe.model;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderedDish implements Serializable {
    @SerializedName("ord_dish_id")
    private int id;
    @SerializedName("menu_dish")
    private int dish;
}
