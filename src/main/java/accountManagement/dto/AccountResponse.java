package accountManagement.dto;

import lombok.*;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccountResponse {
    private Long id;
    private String name;
    private String phoneNr;
    private LocalDateTime createdDtime;
    private LocalDateTime modifiedDtime;
}
