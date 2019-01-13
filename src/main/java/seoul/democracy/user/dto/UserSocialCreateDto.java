package seoul.democracy.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
public class UserSocialCreateDto {

    private String email;

    private String provider;

    private String name;

    private String photo;
}
