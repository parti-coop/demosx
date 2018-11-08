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
import seoul.democracy.common.exception.NotFoundException;
import seoul.democracy.issue.domain.Issue;
import seoul.democracy.proposal.domain.Proposal;
import seoul.democracy.proposal.dto.ProposalDto;
import seoul.democracy.proposal.service.ProposalService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static seoul.democracy.proposal.dto.ProposalDto.projection;
import static seoul.democracy.proposal.predicate.ProposalPredicate.equalId;


/**
 * epic : 7. 제안관리
 * story : 7.5 관리자는 제안을 블럭 처리할 수 있다.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
    "file:src/test/resources/egovframework/spring-test/context-*.xml",
})
@Transactional
@Rollback
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class S_7_5_관리자는_제안을_블럭_처리할_수_있다 {

    private final static DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH-mm");
    private final static String ip = "127.0.0.2";

    @Autowired
    private ProposalService proposalService;

    private final Long proposalId = 1L;
    private final Long deletedProposalId = 2L;
    private final Long blockedProposalId = 3L;

    @Before
    public void setUp() throws Exception {

    }

    /**
     * 1. 관리자는 제안을 블럭할 수 있다.
     */
    @Test
    @WithUserDetails("admin1@googl.co.kr")
    public void T_1_관리자는_제안을_블럭할_수_있다() {
        final String now = LocalDateTime.now().format(dateTimeFormatter);
        Proposal proposal = proposalService.closed(proposalId, ip);
        ProposalDto proposalDto = proposalService.getProposal(equalId(proposal.getId()), projection);
        assertThat(proposalDto.getModifiedDate().format(dateTimeFormatter), is(now));
        assertThat(proposalDto.getModifiedBy().getEmail(), is("admin1@googl.co.kr"));
        assertThat(proposalDto.getModifiedIp(), is(ip));

        assertThat(proposalDto.getStatus(), is(Issue.Status.CLOSED));
    }

    /**
     * 2. 매니저는 제안을 블럭할 수 없다.
     */
    @Test(expected = AccessDeniedException.class)
    @WithUserDetails("manager1@googl.co.kr")
    public void T_2_매니저는_제안을_블럭할_수_없다() {
        proposalService.closed(proposalId, ip);
    }

    /**
     * 3. 사용자는 제안을 블럭할 수 없다.
     */
    @Test(expected = AccessDeniedException.class)
    @WithUserDetails("user1@googl.co.kr")
    public void T_3_사용자는_제안을_블럭할_수_없다() {
        proposalService.closed(proposalId, ip);
    }

    /**
     * 4. 관리자는 블럭된 제안을 블럭할 수 없다.
     */
    @Test(expected = NotFoundException.class)
    @WithUserDetails("admin1@googl.co.kr")
    public void T_4_관리자는_블럭된_제안을_블럭할_수_없다() {
        proposalService.closed(blockedProposalId, ip);
    }

    /**
     * 5. 관리자는 삭제된 제안을 블럭할 수 없다.
     */
    @Test(expected = NotFoundException.class)
    @WithUserDetails("admin1@googl.co.kr")
    public void T_5_관리자는_삭제된_제안을_블럭할_수_없다() {
        proposalService.closed(deletedProposalId, ip);
    }
}
