package lk.ijse.Charleston.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Room {
    private String roomId;
    private String roomType;
    private String roomDetails;
    private String numberofBeds;
    private Double price;
}
