package org.cafe.model.lists;
import org.cafe.model.Operation;
import java.util.List;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OperationList {
    @SerializedName("operations")
    private List<Operation> operations;
}
