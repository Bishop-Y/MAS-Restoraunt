package org.cafe.model;

import com.google.gson.annotations.SerializedName;
import jade.util.leap.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Process implements Serializable {
    @SerializedName("proc_id")
    private int id;
    @SerializedName("ord_dish")
    private int orderedDish;
    @SerializedName("proc_started")
    private LocalDateTime startedAt;
    @SerializedName("proc_ended")
    private LocalDateTime endedAt;
    @SerializedName("proc_active")
    private boolean active;
    @SerializedName("proc_operations")
    private List<ProcessOperation> operations;
}

