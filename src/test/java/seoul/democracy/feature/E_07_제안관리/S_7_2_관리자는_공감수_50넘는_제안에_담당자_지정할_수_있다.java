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
import seoul.democracy.proposal.domain.Proposal;
import seoul.democracy.proposal.dto.ProposalDto;
import seoul.democracy.proposal.dto.ProposalManagerAssignDto;
import seoul.democracy.proposal.service.ProposalService;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static seoul.democracy.proposal.dto.ProposalDto.projection;
import static seoul.democracy.proposal.predicate.ProposalPredicate.equalId;


/**
 * epic : 7. 제안관리
 * story : 7.2 관리자는 공감수 50넘는 제안에 담당자 지정할 수 있다.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
    "file:src/test/resources/egovframework/spring-test/context-*.xml",
})
@Transactional
@Rollback
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class S_7_2_관리자는_공감수_50넘는_제안에_담당자_지정할_수_있다 {

    @Autowired
    private ProposalService proposalService;

    private final Long proposalIdUnder50Like = 1L;
    private final Long proposalIdOver50Like = 4L;

    private final Long adminId = 1L;
    private final Long managerId = 11L;
    private final Long userId = 21L;

    @Before
    public void setUp() throws Exception {

    }

    /**
     * 1. 관리자는 공감수 50넘는 제안에 매니저를 담당자로 지정할 수 있다.
     */
    @Test
    @WithUserDetails("admin1@googl.co.kr")
    public void T_1_관리자는_공감수_50넘는_제안에_매니저를_담당자로_지정할_수_있다() {
        ProposalManagerAssignDto assignDto = ProposalManagerAssignDto.of(proposalIdOver50Like, managerId);
        Proposal proposal = proposalService.assignManager(assignDto);

        ProposalDto proposalDto = proposalService.getProposal(equalId(proposal.getId()), projection);
        assertThat(proposalDto.getManager().getId(), is(assignDto.getManagerId()));
    }

    /**
     * 2. 공감수 50미만의 제안에 대해 담당자를 지정할 수 없다.
     */
    @Test(expected = BadRequestException.class)
    @WithUserDetails("admin2@googl.co.kr")
    public void T_2_공감수_50미만의_제안에_대해_담당자를_지정할_수_없다() {
        ProposalManagerAssignDto assignDto = ProposalManagerAssignDto.of(proposalIdUnder50Like, managerId);
        proposalService.assignManager(assignDto);
    }

    /**
     * 3. 관리자를 담당자로 지정할 수 없다.
     */
    @Test(expected = BadRequestException.class)
    @WithUserDetails("admin1@googl.co.kr")
    public void T_3_관리자를_담당자로_지정할_수_없다() {
        ProposalManagerAssignDto assignDto = ProposalManagerAssignDto.of(proposalIdOver50Like, adminId);
        proposalService.assignManager(assignDto);
    }

    /**
     * 4. 사용자를 담당자로 지정할 수 없다.
     */
    @Test(expected = BadRequestException.class)
    @WithUserDetails("admin1@googl.co.kr")
    public void T_4_사용자를_담당자로_지정할_수_없다() {
        ProposalManagerAssignDto assignDto = ProposalManagerAssignDto.of(proposalIdOver50Like, userId);
        proposalService.assignManager(assignDto);
    }

    /**
     * 5. 매니저는 담당자를 지정할 수 없다.
     */
    @Test(expected = AccessDeniedException.class)
    @WithUserDetails("manager1@googl.co.kr")
    public void T_5_매니저는_담당자를_지정할_수_없다() {
        ProposalManagerAssignDto assignDto = ProposalManagerAssignDto.of(proposalIdOver50Like, managerId);
        proposalService.assignManager(assignDto);
    }

    /**
     * 6. 사용자는 담당자를 지정할 수 없다.
     */
    @Test(expected = AccessDeniedException.class)
    @WithUserDetails("user1@googl.co.kr")
    public void T_6_사용자는_담당자를_지정할_수_없다() {
        ProposalManagerAssignDto assignDto = ProposalManagerAssignDto.of(proposalIdOver50Like, managerId);
        proposalService.assignManager(assignDto);
    }
}
