package org.cafe.model.lists;
import org.cafe.model.OrderedDish;
import java.util.List;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderedDishList {
    @SerializedName("vis_ord_dishes")
    private List<OrderedDish> orderedDishes;
}
