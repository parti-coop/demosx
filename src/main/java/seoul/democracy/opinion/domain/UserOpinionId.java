package seoul.democracy.opinion.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Getter
@Embeddable
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
public class UserOpinionId implements Serializable {

    private Long userId;

    private Long opinionId;

}
