package org.cafe.model;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DishCard implements Serializable {
    @SerializedName("card_id")
    private int id;

    @SerializedName("dish_name")
    private String name;

    @SerializedName("card_descr")
    private String description;

    @SerializedName("card_time")
    private double time;

    @SerializedName("operations")
    private List<Operation> operationsList;

}
