package lk.ijse.Charleston.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Reservation {
    String resId;
    String gusId;
    String roomId;
    Double roomPrice;
    String checkInDate;
    String checkOutDate;

}
