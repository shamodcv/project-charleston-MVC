package lk.ijse.Charleston.dto;

import lombok.*;
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class ReservationDetails {

        private String Res_ID;
        private String Room_ID;
        private String Room_price;
        private String Guest_ID;
        private String Guest_Name;
        private String Check_In_Date;
        private String Check_Out_Date;
}
