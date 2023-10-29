package lk.ijse.Charleston.dto.tm;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class OrderDetailsTM {
    private String orderID;
    private String mealID;
    private Integer qty;
    private Double total;
}
