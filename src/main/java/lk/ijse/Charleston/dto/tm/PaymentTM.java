package lk.ijse.Charleston.dto.tm;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class PaymentTM {
    String paymentId;
    String guestId;
    String guestName;
    String resId;
    String roomId;
    String checkIn;
    String checkOut;
    Double orderAm;
    Double total;
}
