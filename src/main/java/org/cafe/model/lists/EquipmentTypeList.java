package org.cafe.model.lists;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.cafe.model.EquipmentType;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EquipmentTypeList {
    @SerializedName("equipment_type")
    private List<EquipmentType> equipmentTypes;
}
