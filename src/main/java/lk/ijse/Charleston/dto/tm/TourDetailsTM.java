package lk.ijse.Charleston.dto.tm;

import lombok.*;
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class TourDetailsTM {
    private String bookID;
    private String guestID;
    private String guestName;
    private String tourID;
    private String tourName;
    private Double price;

}
