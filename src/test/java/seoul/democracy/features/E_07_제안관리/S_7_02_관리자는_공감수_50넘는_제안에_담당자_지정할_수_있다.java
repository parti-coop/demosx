package seoul.democracy.features.E_07_제안관리;

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
    "file:src/main/webapp/WEB-INF/config/egovframework/springmvc/egov-com-*.xml"
})
@Transactional
@Rollback
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class S_7_02_관리자는_공감수_50넘는_제안에_담당자_지정할_수_있다 {

    @Autowired
    private ProposalService proposalService;

    private final Long initProposalId = 1L;
    private final Long needAssignProposalId = 31L;
    private final Long assignedProposalId = 41L;
    private final Long completeProposalId = 51L;

    private final Long adminId = 1L;
    private final Long managerId = 11L;
    private final Long otherManagerId = 12L;
    private final Long userId = 21L;

    @Before
    public void setUp() throws Exception {

    }

    /**
     * 1. 관리자는 담당지정대기 상태에 매니저를 담당자로 지정할 수 있다.
     */
    @Test
    @WithUserDetails("admin1@googl.co.kr")
    public void T_1_관리자는_담당지정대기_상태에_매니저를_담당자로_지정할_수_있다() {
        ProposalManagerAssignDto assignDto = ProposalManagerAssignDto.of(needAssignProposalId, managerId);
        Proposal proposal = proposalService.assignManager(assignDto);

        ProposalDto proposalDto = proposalService.getProposal(equalId(proposal.getId()), projection);
        assertThat(proposalDto.getManager().getId(), is(assignDto.getManagerId()));
        assertThat(proposalDto.getProcess(), is(Proposal.Process.ASSIGNED));
    }

    /**
     * 2. 일반상태 제안에 대해 담당자를 지정할 수 없다.
     */
    @Test(expected = BadRequestException.class)
    @WithUserDetails("admin2@googl.co.kr")
    public void T_2_일반상태_제안에_대해_담당자를_지정할_수_없다() {
        ProposalManagerAssignDto assignDto = ProposalManagerAssignDto.of(initProposalId, managerId);
        proposalService.assignManager(assignDto);
    }

    /**
     * 3. 담당자가 지정된 제안의 담당자를 변경할 수 있다.
     */
    @Test
    @WithUserDetails("admin2@googl.co.kr")
    public void T_3_담당자가_지정된_제안의_담당자를_변경할_수_있다() {
        ProposalManagerAssignDto assignDto = ProposalManagerAssignDto.of(assignedProposalId, otherManagerId);
        Proposal proposal = proposalService.assignManager(assignDto);

        ProposalDto proposalDto = proposalService.getProposal(equalId(proposal.getId()), projection);
        assertThat(proposalDto.getProcess(), is(Proposal.Process.ASSIGNED));
        assertThat(proposalDto.getManager().getId(), is(assignDto.getManagerId()));
    }

    /**
     * 4. 답변이 완료된 제안에 담당자를 변경할 수 없다.
     */
    @Test(expected = BadRequestException.class)
    @WithUserDetails("admin2@googl.co.kr")
    public void T_4_답변이_완료된_제안에_담당자를_변경할_수_없다() {
        ProposalManagerAssignDto assignDto = ProposalManagerAssignDto.of(completeProposalId, managerId);
        proposalService.assignManager(assignDto);
    }

    /**
     * 5. 관리자를 담당자로 지정할 수 없다.
     */
    @Test(expected = BadRequestException.class)
    @WithUserDetails("admin1@googl.co.kr")
    public void T_5_관리자를_담당자로_지정할_수_없다() {
        ProposalManagerAssignDto assignDto = ProposalManagerAssignDto.of(needAssignProposalId, adminId);
        proposalService.assignManager(assignDto);
    }

    /**
     * 6. 사용자를 담당자로 지정할 수 없다.
     */
    @Test(expected = BadRequestException.class)
    @WithUserDetails("admin1@googl.co.kr")
    public void T_6_사용자를_담당자로_지정할_수_없다() {
        ProposalManagerAssignDto assignDto = ProposalManagerAssignDto.of(needAssignProposalId, userId);
        proposalService.assignManager(assignDto);
    }

    /**
     * 7. 매니저는 담당자를 지정할 수 없다.
     */
    @Test(expected = AccessDeniedException.class)
    @WithUserDetails("manager1@googl.co.kr")
    public void T_7_매니저는_담당자를_지정할_수_없다() {
        ProposalManagerAssignDto assignDto = ProposalManagerAssignDto.of(needAssignProposalId, managerId);
        proposalService.assignManager(assignDto);
    }

    /**
     * 8. 사용자는 담당자를 지정할 수 없다.
     */
    @Test(expected = AccessDeniedException.class)
    @WithUserDetails("user1@googl.co.kr")
    public void T_8_사용자는_담당자를_지정할_수_없다() {
        ProposalManagerAssignDto assignDto = ProposalManagerAssignDto.of(needAssignProposalId, managerId);
        proposalService.assignManager(assignDto);
    }
}

