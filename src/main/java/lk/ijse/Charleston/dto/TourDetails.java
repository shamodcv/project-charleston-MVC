package lk.ijse.Charleston.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class TourDetails {
  private String bookID;
  private String guestID;
  private String guestName;
  private String tourID;
  private String tourName;
  private Double price;
}
