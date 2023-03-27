package org.cafe.model;

import com.google.gson.annotations.SerializedName;
import jade.util.leap.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cook implements Serializable {
    @SerializedName("cook_id")
    private int id;

    @SerializedName("cook_name")
    private String name;

    @SerializedName("cook_active")
    private boolean active;
}
