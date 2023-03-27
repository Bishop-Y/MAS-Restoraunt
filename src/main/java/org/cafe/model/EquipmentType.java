package org.cafe.model;

import com.google.gson.annotations.SerializedName;
import jade.util.leap.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EquipmentType implements Serializable {
    @SerializedName("equip_type_id")
    private int id;

    @SerializedName("equip_type_name")
    private String name;
}

