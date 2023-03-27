package org.cafe.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VisitorOrder implements Serializable {
    @SerializedName("vis_name")
    private String visitorName;

    @SerializedName("vis_ord_started")
    private LocalDateTime orderStarted;

    @SerializedName("vis_ord_ended")
    private LocalDateTime orderEnded;

    @SerializedName("vis_ord_total")
    private int orderTotal;

    @SerializedName("vis_ord_dishes")
    private List<OrderedDish> orderedDishes;

}

