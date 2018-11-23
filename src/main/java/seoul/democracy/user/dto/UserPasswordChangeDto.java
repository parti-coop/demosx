package seoul.democracy.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
public class UserPasswordChangeDto {

    @NotBlank
    private String currentPassword;

    @NotBlank
    @Size(max = 20)
    private String changePassword;
}
