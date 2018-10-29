package seoul.democracy.user.dto;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

@Data
public class UserLoginDto {

    @NotBlank
    private String id;

    @NotBlank
    private String pw;

}
