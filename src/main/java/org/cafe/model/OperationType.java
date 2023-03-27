package org.cafe.model;

import com.google.gson.annotations.SerializedName;
import jade.util.leap.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OperationType implements Serializable {
    @SerializedName("oper_type_id")
    private int id;

    @SerializedName("oper_type_name")
    private String name;
}
