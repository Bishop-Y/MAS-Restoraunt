package org.cafe.model.lists;
import org.cafe.model.MenuDish;

import java.io.Serializable;
import java.util.List;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MenuDishList implements Serializable {
    @SerializedName("menu_dishes")
    private List<MenuDish> menuDishes;
}
