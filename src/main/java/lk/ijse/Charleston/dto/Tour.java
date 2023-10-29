package lk.ijse.Charleston.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Tour {
    private String tourId;
    private String tourName;
    private String tourDescription;
    private Double tourPrice;
}
