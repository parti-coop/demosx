package seoul.democracy.post.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;
import seoul.democracy.post.domain.Post;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
public class PostCreateDto {

    @NotNull
    private Post.Status status;

    @NotBlank
    @Size(max = 100)
    private String title;

    private String content;

}
