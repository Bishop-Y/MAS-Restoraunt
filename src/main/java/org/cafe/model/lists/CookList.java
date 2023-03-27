package org.cafe.model.lists;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.cafe.model.Cook;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CookList {
    @SerializedName("cookers")
    private List<Cook> cookers;
}
