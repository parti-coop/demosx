package seoul.democracy.feature.E_07_제안관리;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
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
 * story : 7.7 관리자는 제안의견을 블럭 처리할 수 있다.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
    "file:src/test/resources/egovframework/spring-test/context-*.xml",
})
@Transactional
@Rollback
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class S_7_7_관리자는_제안의견을_블럭_처리할_수_있다 {

    private final static DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH-mm");
    private final static String ip = "127.0.0.2";
    private MockHttpServletRequest request;

    @Autowired
    private OpinionService opinionService;

    @Autowired
    private ProposalService proposalService;

    private final Long opinionId = 1L;
    private final Long deletedOpinionId = 2L;
    private final Long blockedOpinionId = 3L;
    private final Long multiOpinionId = 31L;

    @Before
    public void setUp() throws Exception {
        request = new MockHttpServletRequest();
        request.setRemoteAddr(ip);
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
    }

    /**
     * 1. 관리자는 제안의견을 블럭할 수 있다.
     */
    @Test
    @WithUserDetails("admin1@googl.co.kr")
    public void T_1_관리자는_제안의견을_블럭할_수_있다() {
        final String now = LocalDateTime.now().format(dateTimeFormatter);
        Opinion opinion = opinionService.blockOpinion(opinionId);

        OpinionDto opinionDto = opinionService.getOpinion(equalId(opinion.getId()), projection);
        assertThat(opinionDto.getModifiedDate().format(dateTimeFormatter), is(now));
        assertThat(opinionDto.getModifiedBy().getEmail(), is("admin1@googl.co.kr"));
        assertThat(opinionDto.getModifiedIp(), is(ip));

        assertThat(opinionDto.getStatus(), is(Opinion.Status.BLOCK));

        ProposalDto proposalDto = proposalService.getProposal(ProposalPredicate.equalId(opinion.getIssue().getId()), ProposalDto.projection);
        assertThat(proposalDto.getStats().getOpinionCount(), is(0L));
        assertThat(proposalDto.getStats().getApplicantCount(), is(0L));
    }

    /**
     * 2. 매니저는 제안의견을 블럭할 수 없다.
     */
    @Test(expected = AccessDeniedException.class)
    @WithUserDetails("manager1@googl.co.kr")
    public void T_2_매니저는_제안의견을_블럭할_수_없다() {
        opinionService.blockOpinion(opinionId);
    }

    /**
     * 3. 사용자는 제안의견을 블럭할 수 없다.
     */
    @Test(expected = AccessDeniedException.class)
    @WithUserDetails("user1@googl.co.kr")
    public void T_3_사용자는_제안의견을_블럭할_수_없다() {
        opinionService.blockOpinion(opinionId);
    }

    /**
     * 4. 관리자는 블럭된 제안의견을 블럭할 수 없다.
     */
    @Test(expected = NotFoundException.class)
    @WithUserDetails("admin1@googl.co.kr")
    public void T_4_관리자는_블럭된_제안의견을_블럭할_수_없다() {
        opinionService.blockOpinion(blockedOpinionId);
    }

    /**
     * 5. 관리자는 삭제된 제안의견을 블럭할 수 없다.
     */
    @Test(expected = NotFoundException.class)
    @WithUserDetails("admin1@googl.co.kr")
    public void T_5_관리자는_삭제된_제안의견을_블럭할_수_없다() {
        opinionService.blockOpinion(deletedOpinionId);
    }

    /**
     * 6. 관리자는 여러 제안의견 중 하나를 블럭할 수 있다.
     */
    @Test
    @WithUserDetails("admin1@googl.co.kr")
    public void T_6_관리자는_여러_제안의견_중_하나를_블럭할_수_있다() {
        final String now = LocalDateTime.now().format(dateTimeFormatter);
        Opinion opinion = opinionService.blockOpinion(multiOpinionId);

        OpinionDto opinionDto = opinionService.getOpinion(equalId(opinion.getId()), projection);
        assertThat(opinionDto.getModifiedDate().format(dateTimeFormatter), is(now));
        assertThat(opinionDto.getModifiedBy().getEmail(), is("admin1@googl.co.kr"));
        assertThat(opinionDto.getModifiedIp(), is(ip));

        assertThat(opinionDto.getStatus(), is(Opinion.Status.BLOCK));

        ProposalDto proposalDto = proposalService.getProposal(ProposalPredicate.equalId(opinion.getIssue().getId()), ProposalDto.projection);
        assertThat(proposalDto.getStats().getOpinionCount(), is(1L));
        assertThat(proposalDto.getStats().getApplicantCount(), is(1L));
    }
}
