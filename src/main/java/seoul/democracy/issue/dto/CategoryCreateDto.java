package seoul.democracy.issue.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
public class CategoryCreateDto {

    @NotBlank
    @Size(max = 30)
    private String name;

    @NotNull
    private Boolean enabled;

    @NotNull
    @Min(0)
    @Max(1000)
    private Integer sequence;
}
