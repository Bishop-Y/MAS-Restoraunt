package org.cafe.model.lists;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.cafe.model.DishCard;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DishCardList {
    @SerializedName("dish_cards")
    private List<DishCard> dishCards;
}
