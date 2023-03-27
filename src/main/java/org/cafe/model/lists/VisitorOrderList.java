package org.cafe.model.lists;

import com.google.gson.annotations.SerializedName;
import jade.util.leap.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.cafe.model.VisitorOrder;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VisitorOrderList{
    @SerializedName("visitors_orders")
    private List<VisitorOrder> visitorOrders;
}
