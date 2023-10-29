package lk.ijse.Charleston.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Food {
    private String mealType;
    private String mealID;
    private String mealName;
    private String mealDescription;
    private Double mealPrice;

}
