package seoul.democracy.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
public class UserManagerCreateDto {

    @NotNull
    private Long userId;

    @NotBlank
    private String category;

    @NotBlank
    @Size(max = 30)
    private String department1;

    @NotBlank
    @Size(max = 30)
    private String department2;

    @NotBlank
    @Size(max = 30)
    private String department3;
}
