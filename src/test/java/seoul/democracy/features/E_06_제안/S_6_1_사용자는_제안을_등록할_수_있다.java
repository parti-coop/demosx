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
import seoul.democracy.issue.domain.Issue;
import seoul.democracy.opinion.domain.OpinionType;
import seoul.democracy.proposal.domain.Proposal;
import seoul.democracy.proposal.dto.ProposalCreateDto;
import seoul.democracy.proposal.dto.ProposalDto;
import seoul.democracy.proposal.service.ProposalService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;
import static seoul.democracy.proposal.dto.ProposalDto.projection;
import static seoul.democracy.proposal.predicate.ProposalPredicate.equalId;


/**
 * epic : 6. 제안
 * story : 6.1 사용자는 제안을 등록할 수 있다.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
    "file:src/test/resources/egovframework/spring-test/context-*.xml",
    "file:src/main/webapp/WEB-INF/config/egovframework/springmvc/egov-com-*.xml"
})
@Transactional
@Rollback
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class S_6_1_사용자는_제안을_등록할_수_있다 {

    private final static DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH-mm");
    private final static String ip = "127.0.0.1";

    @Autowired
    private ProposalService proposalService;


    @Before
    public void setUp() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRemoteAddr(ip);
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
    }

    /**
     * 1. 사용자는 제목,내용으로 제안을 등록할 수 있다.
     */
    @Test
    @WithUserDetails("user1@googl.co.kr")
    public void T_1_사용자는_제목_내용으로_제안을_등록할_수_있다() {
        final String now = LocalDateTime.now().format(dateTimeFormatter);
        ProposalCreateDto createDto = ProposalCreateDto.of("제안합니다.", "제안내용입니다.");
        Proposal proposal = proposalService.create(createDto);
        assertThat(proposal.getId(), is(notNullValue()));

        ProposalDto proposalDto = proposalService.getProposal(equalId(proposal.getId()), projection);
        assertThat(proposalDto.getCreatedDate().format(dateTimeFormatter), is(now));
        assertThat(proposalDto.getModifiedDate().format(dateTimeFormatter), is(now));
        assertThat(proposalDto.getCreatedBy().getEmail(), is("user1@googl.co.kr"));
        assertThat(proposalDto.getModifiedBy().getEmail(), is("user1@googl.co.kr"));
        assertThat(proposalDto.getCreatedIp(), is(ip));
        assertThat(proposalDto.getModifiedIp(), is(ip));

        assertThat(proposalDto.getOpinionType(), is(OpinionType.PROPOSAL));

        assertThat(proposalDto.getStats().getViewCount(), is(0L));
        assertThat(proposalDto.getStats().getLikeCount(), is(0L));
        assertThat(proposalDto.getStats().getOpinionCount(), is(0L));
        assertThat(proposalDto.getStats().getYesCount(), is(0L));
        assertThat(proposalDto.getStats().getNoCount(), is(0L));
        assertThat(proposalDto.getStats().getEtcCount(), is(0L));

        assertThat(proposalDto.getStatus(), is(Issue.Status.OPEN));

        assertThat(proposalDto.getTitle(), is(createDto.getTitle()));
        assertThat(proposalDto.getContent(), is(createDto.getContent()));
    }

}
