package lk.ijse.Charleston.dto.tm;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class ReservationTM {
    String resId;
    String gusId;
    String roomId;
    Double roomPrice;
    String checkInDate;
    String checkOutDate;
}