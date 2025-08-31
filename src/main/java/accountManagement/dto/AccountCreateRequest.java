package accountManagement.dto;

import accountManagement.validation.ValidPhoneNumber;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountCreateRequest {

    @NotBlank(message = "Name is required!")
    private String name;

    @ValidPhoneNumber
    private String phoneNr;
}
