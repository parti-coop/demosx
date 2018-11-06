package seoul.democracy.feature.E_08_토론;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import seoul.democracy.common.exception.BadRequestException;
import seoul.democracy.common.exception.NotFoundException;
import seoul.democracy.debate.dto.DebateDto;
import seoul.democracy.debate.predicate.DebatePredicate;
import seoul.democracy.debate.service.DebateService;
import seoul.democracy.opinion.domain.Opinion;
import seoul.democracy.opinion.dto.OpinionDto;
import seoul.democracy.opinion.dto.OpinionUpdateDto;
import seoul.democracy.opinion.service.OpinionService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static seoul.democracy.opinion.dto.OpinionDto.projection;
import static seoul.democracy.opinion.predicate.OpinionPredicate.equalId;


/**
 * epic : 8. 토론
 * story : 8.4 사용자는 토론의견을 수정할 수 있다.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
    "file:src/test/resources/egovframework/spring-test/context-*.xml",
})
@Transactional
@Rollback
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class S_8_4_사용자는_토론의견을_수정할_수_있다 {

    private final static DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH-mm");
    private final static String ip = "127.0.0.2";

    @Autowired
    private OpinionService opinionService;

    @Autowired
    private DebateService debateService;

    private final Long opinionIdInProgress = 111L;
    private final Long deletedOpinionId = 112L;
    private final Long blockedOpinionId = 113L;
    private final Long opinionIdInComplete = 151L;
    private final Long notExistsOpinionId = 999L;


    @Before
    public void setUp() throws Exception {
    }

    /**
     * 1. 사용자는 본인의견을 수정할 수 있다.
     */
    @Test
    @WithUserDetails("user1@googl.co.kr")
    public void T_1_사용자는_본인의견을_수정할_수_있다() {
        final String now = LocalDateTime.now().format(dateTimeFormatter);
        OpinionUpdateDto updateDto = OpinionUpdateDto.of(opinionIdInProgress, "토론 제안의견 수정합니다.");
        Opinion opinion = opinionService.updateOpinion(updateDto, ip);

        OpinionDto opinionDto = opinionService.getOpinion(equalId(opinion.getId()), projection);
        assertThat(opinionDto.getModifiedDate().format(dateTimeFormatter), is(now));
        assertThat(opinionDto.getModifiedBy().getEmail(), is("user1@googl.co.kr"));
        assertThat(opinionDto.getModifiedIp(), is(ip));

        assertThat(opinionDto.getContent(), is(updateDto.getContent()));

        DebateDto debateDto = debateService.getDebate(DebatePredicate.equalId(opinion.getIssue().getId()), DebateDto.projection, false, false);
        assertThat(debateDto.getStats().getOpinionCount(), is(1L));
        assertThat(debateDto.getStats().getApplicantCount(), is(1L));
    }

    /**
     * 2. 진행 중이 아닌 토론은 의견을 수정할 수 없다.
     */
    @Test(expected = BadRequestException.class)
    @WithUserDetails("user1@googl.co.kr")
    public void T_2_진행_중이_아닌_토론은_의견을_수정할_수_없다() {
        OpinionUpdateDto updateDto = OpinionUpdateDto.of(opinionIdInComplete, "토론 제안의견 수정합니다.");
        opinionService.updateOpinion(updateDto, ip);
    }

    /**
     * 3. 다른 사용자의 의견을 수정할 수 없다.
     */
    @Test(expected = AccessDeniedException.class)
    @WithUserDetails("user2@googl.co.kr")
    public void T_3_다른_사용자의_의견을_수정할_수_없다() {
        OpinionUpdateDto updateDto = OpinionUpdateDto.of(opinionIdInProgress, "토론 제안의견 수정합니다.");
        opinionService.updateOpinion(updateDto, ip);
    }

    /**
     * 4. 없는 의견을 수정할 수 없다.
     */
    @Test(expected = NotFoundException.class)
    @WithUserDetails("user1@googl.co.kr")
    public void T_4_없는_의견을_수정할_수_없다() {
        OpinionUpdateDto updateDto = OpinionUpdateDto.of(notExistsOpinionId, "없는 제안입니다.");
        opinionService.updateOpinion(updateDto, ip);
    }

    /**
     * 5. 삭제된 의견을 수정할 수 없다.
     */
    @Test(expected = NotFoundException.class)
    @WithUserDetails("user1@googl.co.kr")
    public void T_5_삭제된_의견을_수정할_수_없다() {
        OpinionUpdateDto updateDto = OpinionUpdateDto.of(deletedOpinionId, "삭제된 제안입니다.");
        opinionService.updateOpinion(updateDto, ip);
    }

    /**
     * 6. 블럭된 의견을 수정할 수 없다.
     */
    @Test(expected = NotFoundException.class)
    @WithUserDetails("user1@googl.co.kr")
    public void T_6_블럭된_의견을_수정할_수_없다() {
        OpinionUpdateDto updateDto = OpinionUpdateDto.of(blockedOpinionId, "블럭된 제안입니다.");
        opinionService.updateOpinion(updateDto, ip);
    }

}
