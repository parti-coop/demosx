package seoul.democracy.issue.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import seoul.democracy.opinion.domain.Opinion;
import seoul.democracy.opinion.dto.OpinionCreateDto;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Getter
@NoArgsConstructor
@Entity
@DiscriminatorValue("A")
public class Action extends Issue {

    @Override
    public Opinion createOpinion(OpinionCreateDto createDto, String ip) {
        throw new UnsupportedOperationException("실행은 의견을 지원하지 않습니다.");
    }

    @Override
    public boolean isUpdatableOpinion() {
        return false;
    }
}
