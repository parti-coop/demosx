package seoul.democracy.feature.E_09_토론관리;

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
import seoul.democracy.common.exception.NotFoundException;
import seoul.democracy.debate.dto.DebateDto;
import seoul.democracy.debate.predicate.DebatePredicate;
import seoul.democracy.debate.service.DebateService;
import seoul.democracy.opinion.domain.Opinion;
import seoul.democracy.opinion.dto.OpinionDto;
import seoul.democracy.opinion.service.OpinionService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static seoul.democracy.opinion.dto.OpinionDto.projection;
import static seoul.democracy.opinion.predicate.OpinionPredicate.equalId;


/**
 * epic : 9. 토론관리
 * story : 9.8. 관리자는 의견을 블럭 처리할 수 있다.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
    "file:src/test/resources/egovframework/spring-test/context-*.xml",
})
@Transactional
@Rollback
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class S_9_8_관리자는_의견을_블럭_처리할_수_있다 {

    private final static DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH-mm");
    private final static String ip = "127.0.0.2";

    @Autowired
    private OpinionService opinionService;

    @Autowired
    private DebateService debateService;

    private final Long proposalOpinionId = 111L;
    private final Long deletedOpinionId = 112L;
    private final Long blockedOpinionId = 113L;

    private final Long multiOpinionId = 114L;

    private final Long yesOpinionId = 131L;
    private final Long noOpinionId = 132L;
    private final Long etcOpinionId = 133L;

    @Before
    public void setUp() throws Exception {

    }

    /**
     * 1. 관리자는 제안의견을 블럭할 수 있다.
     */
    @Test
    @WithUserDetails("admin1@googl.co.kr")
    public void T_1_관리자는_제안의견을_블럭할_수_있다() {
        final String now = LocalDateTime.now().format(dateTimeFormatter);
        Opinion opinion = opinionService.blockOpinion(proposalOpinionId, ip);

        OpinionDto opinionDto = opinionService.getOpinion(equalId(opinion.getId()), projection);
        assertThat(opinionDto.getModifiedDate().format(dateTimeFormatter), is(now));
        assertThat(opinionDto.getModifiedBy().getEmail(), is("admin1@googl.co.kr"));
        assertThat(opinionDto.getModifiedIp(), is(ip));

        assertThat(opinionDto.getStatus(), is(Opinion.Status.BLOCK));

        DebateDto debateDto = debateService.getDebate(DebatePredicate.equalId(opinion.getIssue().getId()), DebateDto.projection, false, false);
        assertThat(debateDto.getStats().getOpinionCount(), is(2L));
        assertThat(debateDto.getStats().getYesCount(), is(0L));
        assertThat(debateDto.getStats().getNoCount(), is(0L));
        assertThat(debateDto.getStats().getEtcCount(), is(2L));
        assertThat(debateDto.getStats().getApplicantCount(), is(1L));
    }

    /**
     * 2. 관리자는 토론 찬성의견을 블럭할 수 있다.
     */
    @Test
    @WithUserDetails("admin1@googl.co.kr")
    public void T_2_관리자는_토론_찬성의견을_블럭할_수_있다() {
        Opinion opinion = opinionService.blockOpinion(yesOpinionId, ip);

        OpinionDto opinionDto = opinionService.getOpinion(equalId(opinion.getId()), projection);
        assertThat(opinionDto.getStatus(), is(Opinion.Status.BLOCK));

        DebateDto debateDto = debateService.getDebate(DebatePredicate.equalId(opinion.getIssue().getId()), DebateDto.projection, false, false);
        assertThat(debateDto.getStats().getOpinionCount(), is(2L));
        assertThat(debateDto.getStats().getYesCount(), is(0L));
        assertThat(debateDto.getStats().getNoCount(), is(1L));
        assertThat(debateDto.getStats().getEtcCount(), is(1L));
        assertThat(debateDto.getStats().getApplicantCount(), is(2L));
    }

    /**
     * 3. 관리자는 토론 반대의견을 블럭할 수 있다.
     */
    @Test
    @WithUserDetails("admin1@googl.co.kr")
    public void T_3_관리자는_토론_반대의견을_블럭할_수_있다() {
        Opinion opinion = opinionService.blockOpinion(noOpinionId, ip);

        OpinionDto opinionDto = opinionService.getOpinion(equalId(opinion.getId()), projection);
        assertThat(opinionDto.getStatus(), is(Opinion.Status.BLOCK));

        DebateDto debateDto = debateService.getDebate(DebatePredicate.equalId(opinion.getIssue().getId()), DebateDto.projection, false, false);
        assertThat(debateDto.getStats().getOpinionCount(), is(2L));
        assertThat(debateDto.getStats().getYesCount(), is(1L));
        assertThat(debateDto.getStats().getNoCount(), is(0L));
        assertThat(debateDto.getStats().getEtcCount(), is(1L));
        assertThat(debateDto.getStats().getApplicantCount(), is(2L));
    }

    /**
     * 4. 관리자는 제안의견을 블럭할 수 있다.
     */
    @Test
    @WithUserDetails("admin1@googl.co.kr")
    public void T_4_관리자는_토론_기타의견을_블럭할_수_있다() {
        Opinion opinion = opinionService.blockOpinion(etcOpinionId, ip);

        OpinionDto opinionDto = opinionService.getOpinion(equalId(opinion.getId()), projection);
        assertThat(opinionDto.getStatus(), is(Opinion.Status.BLOCK));

        DebateDto debateDto = debateService.getDebate(DebatePredicate.equalId(opinion.getIssue().getId()), DebateDto.projection, false, false);
        assertThat(debateDto.getStats().getOpinionCount(), is(2L));
        assertThat(debateDto.getStats().getYesCount(), is(1L));
        assertThat(debateDto.getStats().getNoCount(), is(1L));
        assertThat(debateDto.getStats().getEtcCount(), is(0L));
        assertThat(debateDto.getStats().getApplicantCount(), is(2L));
    }

    /**
     * 5. 매니저는 제안의견을 블럭할 수 없다.
     */
    @Test(expected = AccessDeniedException.class)
    @WithUserDetails("manager1@googl.co.kr")
    public void T_5_매니저는_제안의견을_블럭할_수_없다() {
        opinionService.blockOpinion(proposalOpinionId, ip);
    }

    /**
     * 6. 사용자는 제안의견을 블럭할 수 없다.
     */
    @Test(expected = AccessDeniedException.class)
    @WithUserDetails("user1@googl.co.kr")
    public void T_6_사용자는_제안의견을_블럭할_수_없다() {
        opinionService.blockOpinion(proposalOpinionId, ip);
    }

    /**
     * 7. 관리자는 블럭된 제안의견을 블럭할 수 없다.
     */
    @Test(expected = NotFoundException.class)
    @WithUserDetails("admin1@googl.co.kr")
    public void T_7_관리자는_블럭된_제안의견을_블럭할_수_없다() {
        opinionService.blockOpinion(blockedOpinionId, ip);
    }

    /**
     * 8. 관리자는 삭제된 제안의견을 블럭할 수 없다.
     */
    @Test(expected = NotFoundException.class)
    @WithUserDetails("admin1@googl.co.kr")
    public void T_8_관리자는_삭제된_제안의견을_블럭할_수_없다() {
        opinionService.blockOpinion(deletedOpinionId, ip);
    }

    /**
     * 9. 관리자는 여러 제안의견 중 하나를 블럭할 수 있다.
     */
    @Test
    @WithUserDetails("admin2@googl.co.kr")
    public void T_9_관리자는_여러_제안의견_중_하나를_블럭할_수_있다() {
        final String now = LocalDateTime.now().format(dateTimeFormatter);
        Opinion opinion = opinionService.blockOpinion(multiOpinionId, ip);

        OpinionDto opinionDto = opinionService.getOpinion(equalId(opinion.getId()), projection);
        assertThat(opinionDto.getModifiedDate().format(dateTimeFormatter), is(now));
        assertThat(opinionDto.getModifiedBy().getEmail(), is("admin2@googl.co.kr"));
        assertThat(opinionDto.getModifiedIp(), is(ip));

        assertThat(opinionDto.getStatus(), is(Opinion.Status.BLOCK));

        DebateDto debateDto = debateService.getDebate(DebatePredicate.equalId(opinion.getIssue().getId()), DebateDto.projection, false, false);
        assertThat(debateDto.getStats().getOpinionCount(), is(2L));
        assertThat(debateDto.getStats().getYesCount(), is(0L));
        assertThat(debateDto.getStats().getNoCount(), is(0L));
        assertThat(debateDto.getStats().getEtcCount(), is(2L));
        assertThat(debateDto.getStats().getApplicantCount(), is(2L));
    }
}
