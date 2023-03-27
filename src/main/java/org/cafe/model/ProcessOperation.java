package org.cafe.model;

import com.google.gson.annotations.SerializedName;
import jade.util.leap.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProcessOperation implements Serializable {
    @SerializedName("proc_oper")
    private int id;
}
