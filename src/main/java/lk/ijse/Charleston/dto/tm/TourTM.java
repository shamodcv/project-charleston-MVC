package lk.ijse.Charleston.dto.tm;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class TourTM {
    private String tourId;
    private String tourName;
    private String tourDescription;
    private Double tourPrice;
}
