package lk.ijse.Charleston.dto;

import javafx.scene.control.Button;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Order {
    private String orderID;
    private String resID;
    private String roomID;
    private String guestID;
    private String guestName;
    private String orderDate;
    private String mealID;
    private String mealName;
    private Double mealPrice;
    private Integer qty;
    private Double total;
    private Button removeBtn;
}
