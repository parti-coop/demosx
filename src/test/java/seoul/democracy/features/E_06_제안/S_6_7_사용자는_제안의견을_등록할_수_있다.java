package seoul.democracy.features.E_06_제안;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import seoul.democracy.common.exception.NotFoundException;
import seoul.democracy.opinion.domain.Opinion;
import seoul.democracy.opinion.dto.OpinionCreateDto;
import seoul.democracy.opinion.dto.OpinionDto;
import seoul.democracy.opinion.service.OpinionService;
import seoul.democracy.proposal.dto.ProposalDto;
import seoul.democracy.proposal.predicate.ProposalPredicate;
import seoul.democracy.proposal.service.ProposalService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;
import static seoul.democracy.opinion.dto.OpinionDto.projection;
import static seoul.democracy.opinion.predicate.OpinionPredicate.equalId;


/**
 * epic : 6. 제안
 * story : 6.7 사용자는 제안의견을 등록할 수 있다.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
    "file:src/test/resources/egovframework/spring-test/context-*.xml",
    "file:src/main/webapp/WEB-INF/config/egovframework/springmvc/egov-com-*.xml"
})
@Transactional
@Rollback
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class S_6_7_사용자는_제안의견을_등록할_수_있다 {

    private final static DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH-mm");
    private final static String ip = "127.0.0.2";
    private MockHttpServletRequest request;

    @Autowired
    private OpinionService opinionService;

    @Autowired
    private ProposalService proposalService;

    @Before
    public void setUp() throws Exception {
        request = new MockHttpServletRequest();
        request.setRemoteAddr(ip);
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
    }

    /**
     * 1. 사용자는 제안의견을 등록할 수 있다.
     */
    @Test
    @WithUserDetails("user2@googl.co.kr")
    public void T_1_사용자는_제안의견을_등록할_수_있다() {
        final String now = LocalDateTime.now().format(dateTimeFormatter);
        OpinionCreateDto createDto = OpinionCreateDto.of(1L, "새 제안의견 입니다.");
        Opinion opinion = opinionService.createOpinion(createDto);
        assertThat(opinion.getId(), is(notNullValue()));

        OpinionDto opinionDto = opinionService.getOpinion(equalId(opinion.getId()), projection);
        assertThat(opinionDto.getCreatedDate().format(dateTimeFormatter), is(now));
        assertThat(opinionDto.getModifiedDate().format(dateTimeFormatter), is(now));
        assertThat(opinionDto.getCreatedBy().getEmail(), is("user2@googl.co.kr"));
        assertThat(opinionDto.getModifiedBy().getEmail(), is("user2@googl.co.kr"));
        assertThat(opinionDto.getCreatedIp(), is(ip));
        assertThat(opinionDto.getModifiedIp(), is(ip));

        assertThat(opinionDto.getIssue().getId(), is(createDto.getIssueId()));

        assertThat(opinionDto.getLikeCount(), is(0L));
        assertThat(opinionDto.getContent(), is(createDto.getContent()));
        assertThat(opinionDto.getStatus(), is(Opinion.Status.OPEN));
        assertThat(opinionDto.getVote(), is(Opinion.Vote.ETC));

        ProposalDto proposalDto = proposalService.getProposal(ProposalPredicate.equalId(opinion.getIssue().getId()), ProposalDto.projection);
        assertThat(proposalDto.getStats().getOpinionCount(), is(2L));
        assertThat(proposalDto.getStats().getApplicantCount(), is(2L));
    }

    /**
     * 2. 사용자는 여러번 제안 의견을 등록할 수 있다.
     */
    @Test
    @WithUserDetails("user1@googl.co.kr")
    public void T_2_사용자는_여러번_제안_의견을_등록할_수_있다() {
        OpinionCreateDto createDto = OpinionCreateDto.of(1L, "다시 제안의견 입니다.");
        Opinion opinion = opinionService.createOpinion(createDto);
        assertThat(opinion.getId(), is(notNullValue()));

        OpinionDto opinionDto = opinionService.getOpinion(equalId(opinion.getId()), projection);
        assertThat(opinionDto.getIssue().getId(), is(createDto.getIssueId()));

        ProposalDto proposalDto = proposalService.getProposal(ProposalPredicate.equalId(opinion.getIssue().getId()), ProposalDto.projection);
        assertThat(proposalDto.getStats().getOpinionCount(), is(2L));
        assertThat(proposalDto.getStats().getApplicantCount(), is(1L));
    }

    /**
     * 3. 삭제된 제안에 의견을 등록할 수 없다.
     */
    @Test(expected = NotFoundException.class)
    @WithUserDetails("user1@googl.co.kr")
    public void T_3_삭제된_제안에_의견을_등록할_수_없다() {
        Long deletedProposalId = 2L;
        OpinionCreateDto createDto = OpinionCreateDto.of(deletedProposalId, "삭제된 제안의 제안의견 입니다.");
        opinionService.createOpinion(createDto);
    }

    /**
     * 4. 블럭된 제안에 의견을 등록할 수 없다.
     */
    @Test(expected = NotFoundException.class)
    @WithUserDetails("user1@googl.co.kr")
    public void T_3_블럭된_제안에_의견을_등록할_수_없다() {
        Long deletedProposalId = 3L;
        OpinionCreateDto createDto = OpinionCreateDto.of(deletedProposalId, "블럭된 제안의 제안의견 입니다.");
        opinionService.createOpinion(createDto);
    }
}
