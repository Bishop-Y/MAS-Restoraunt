package org.cafe.model.lists;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.cafe.model.Equipment;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EquipmentList {
    @SerializedName("equipment")
    private List<Equipment> equipment;
}
