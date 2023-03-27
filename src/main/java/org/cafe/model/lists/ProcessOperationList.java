package org.cafe.model.lists;
import org.cafe.model.ProcessOperation;
import java.util.List;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProcessOperationList {
    @SerializedName("proc_operations")
    private List<ProcessOperation> processes;
}
