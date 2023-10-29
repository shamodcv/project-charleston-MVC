package lk.ijse.Charleston.dto.tm;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class FoodTM {
    private String mealType;
    private String mealID;
    private String mealName;
    private String mealDescription;
    private Double mealPrice;
}
