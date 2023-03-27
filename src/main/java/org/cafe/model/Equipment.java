package org.cafe.model;

import com.google.gson.annotations.SerializedName;
import jade.util.leap.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Equipment implements Serializable {
    @SerializedName("equip_id")
    private int id;
    @SerializedName("equip_type")
    private int typeId;

    @SerializedName("equip_name")
    private String name;

    @SerializedName("equip_active")
    private boolean active;
}
