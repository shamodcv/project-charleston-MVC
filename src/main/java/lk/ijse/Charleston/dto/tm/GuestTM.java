package lk.ijse.Charleston.dto.tm;


import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class GuestTM {
    private String idType;
    private String id;
    private String name;
    private String contactNo;
    private String country;
    private String email;
}
