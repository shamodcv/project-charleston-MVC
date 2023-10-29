package lk.ijse.Charleston.dto.tm;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class RoomTM {
    private String roomId;
    private String roomType;
    private String roomDetails;
    private String numberofBeds;
    private Double price;
}
