package org.cafe.model.lists;
import org.cafe.model.Process;
import java.util.List;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProcessList {
    @SerializedName("process_log")
    private List<Process> processes;
}
