package lk.ijse.Charleston.dto;


import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Guest {
    private String idType;
    private String id;
    private String name;
    private String contactNo;
    private String country;
    private String email;
}
