package seoul.democracy.feature.E_07_제안관리;

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
import seoul.democracy.opinion.domain.Opinion;
import seoul.democracy.opinion.dto.OpinionDto;
import seoul.democracy.opinion.service.OpinionService;
import seoul.democracy.proposal.dto.ProposalDto;
import seoul.democracy.proposal.predicate.ProposalPredicate;
import seoul.democracy.proposal.service.ProposalService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static seoul.democracy.opinion.dto.OpinionDto.projection;
import static seoul.democracy.opinion.predicate.OpinionPredicate.equalId;


/**
 * epic : 7. 제안관리
 * story : 7.8 관리자는 비공개 제안의견을 공개할 수 있다.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
    "file:src/test/resources/egovframework/spring-test/context-*.xml",
})
@Transactional
@Rollback
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class S_7_8_관리자는_비공개_제안의견을_공개할_수_있다 {

    private final static DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH-mm");
    private final static String ip = "127.0.0.2";

    @Autowired
    private OpinionService opinionService;

    @Autowired
    private ProposalService proposalService;

    private final Long blockedOpinionId = 41L;
    private final Long openedOpinionId = 1L;
    private final Long deletedOpinionId = 2L;
    private final Long blockedMultiOpinionId = 3L;

    @Before
    public void setUp() throws Exception {

    }

    /**
     * 1. 관리자는 비공개 제안의견을 공개할 수 있다.
     */
    @Test
    @WithUserDetails("admin1@googl.co.kr")
    public void T_1_관리자는_비공개_제안의견을_공개할_수_있다() {
        final String now = LocalDateTime.now().format(dateTimeFormatter);
        Opinion opinion = opinionService.openOpinion(blockedOpinionId, ip);

        OpinionDto opinionDto = opinionService.getOpinion(equalId(opinion.getId()), projection);
        assertThat(opinionDto.getModifiedDate().format(dateTimeFormatter), is(now));

        assertThat(opinionDto.getModifiedBy().getEmail(), is("admin1@googl.co.kr"));
        assertThat(opinionDto.getModifiedIp(), is(ip));

        assertThat(opinionDto.getStatus(), is(Opinion.Status.OPEN));

        ProposalDto proposalDto = proposalService.getProposal(ProposalPredicate.equalId(opinionDto.getIssue().getId()), ProposalDto.projection);
        assertThat(proposalDto.getStats().getOpinionCount(), is(1L));
        assertThat(proposalDto.getStats().getApplicantCount(), is(1L));
    }

    /**
     * 2. 관리자는 공개된 제안의견을 공개할 수 없다.
     */
    @Test(expected = BadRequestException.class)
    @WithUserDetails("admin1@googl.co.kr")
    public void T_2_관리자는_공개된_제안의견을_공개할_수_없다() {
        opinionService.openOpinion(openedOpinionId, ip);
    }

    /**
     * 3. 매니저는 비공개 제안의견을 공개할 수 없다.
     */
    @Test(expected = AccessDeniedException.class)
    @WithUserDetails("manager1@googl.co.kr")
    public void T_3_매니저는_비공개_제안의견을_공개할_수_없다() {
        opinionService.openOpinion(blockedOpinionId, ip);
    }

    /**
     * 4. 사용자는 비공개 제안의견을 공개할 수 없다.
     */
    @Test(expected = AccessDeniedException.class)
    @WithUserDetails("user1@googl.co.kr")
    public void T_4_사용자는_비공개_제안의견을_공개할_수_없다() {
        opinionService.openOpinion(blockedOpinionId, ip);
    }

    /**
     * 5. 관리자는 삭제된 제안을 공개할 수 없다.
     */
    @Test(expected = NotFoundException.class)
    @WithUserDetails("admin1@googl.co.kr")
    public void T_5_관리자는_삭제된_제안을_공개할_수_없다() {
        opinionService.openOpinion(deletedOpinionId, ip);
    }

    /**
     * 6. 관리자는 여러 제안의견 중 비공개 의견을 공개할 수 있다.
     */
    @Test
    @WithUserDetails("admin1@googl.co.kr")
    public void T_6_관리자는_여러_제안의견_중_비공개_의견을_공개할_수_있다() {
        Opinion opinion = opinionService.openOpinion(blockedMultiOpinionId, ip);

        OpinionDto opinionDto = opinionService.getOpinion(equalId(opinion.getId()), projection);
        assertThat(opinionDto.getStatus(), is(Opinion.Status.OPEN));

        ProposalDto proposalDto = proposalService.getProposal(ProposalPredicate.equalId(opinionDto.getIssue().getId()), ProposalDto.projection);
        assertThat(proposalDto.getStats().getOpinionCount(), is(2L));
        assertThat(proposalDto.getStats().getApplicantCount(), is(1L));
    }
}
