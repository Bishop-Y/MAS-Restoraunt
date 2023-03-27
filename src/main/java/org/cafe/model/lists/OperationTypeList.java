package org.cafe.model.lists;
import org.cafe.model.OperationType;
import java.util.List;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OperationTypeList {
    @SerializedName("operation_types")
    private List<OperationType> operationTypes;
}
