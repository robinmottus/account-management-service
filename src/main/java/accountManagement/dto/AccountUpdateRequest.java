package accountManagement.dto;

import accountManagement.validation.ValidPhoneNumber;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountUpdateRequest {

    private Long id;

    @NotBlank(message = "Name is required!")
    private String name;

    @ValidPhoneNumber
    private String phoneNr;
}
