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
import seoul.democracy.common.exception.NotFoundException;
import seoul.democracy.proposal.domain.Proposal;
import seoul.democracy.proposal.dto.ProposalAdminCommentEditDto;
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
 * story : 7.1 관리자는 관리자의견을 등록할 수 있다.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
    "file:src/test/resources/egovframework/spring-test/context-*.xml",
    "file:src/main/webapp/WEB-INF/config/egovframework/springmvc/egov-com-*.xml"
})
@Transactional
@Rollback
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class S_7_1_관리자는_관리자의견을_등록할_수_있다 {

    private final static DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH-mm");

    @Autowired
    private ProposalService proposalService;

    private final Long proposalId = 1L;
    private final Long deletedProposalId = 2L;
    private final Long blockedProposalId = 3L;
    private final Long proposalIdWithAdminComment = 1L;

    @Before
    public void setUp() throws Exception {

    }

    /**
     * 1. 관리자는 관리자의견을 등록할 수 있다.
     */
    @Test
    @WithUserDetails("admin1@googl.co.kr")
    public void T_1_관리자는_관리자의견을_등록할_수_있다() {
        final String now = LocalDateTime.now().format(dateTimeFormatter);
        ProposalAdminCommentEditDto editDto = ProposalAdminCommentEditDto.of(proposalId, "관리자댓글입니다.");
        Proposal proposal = proposalService.editAdminComment(editDto);

        ProposalDto proposalDto = proposalService.getProposal(equalId(proposal.getId()), projection);
        assertThat(proposalDto.getAdminCommentDate().format(dateTimeFormatter), is(now));
        assertThat(proposalDto.getAdminComment(), is(editDto.getComment()));
    }

    /**
     * 2. 관리자는 관리자의견을 수정할 수 있다.
     */
    @Test
    @WithUserDetails("admin2@googl.co.kr")
    public void T_2_관리자는_관리자의견을_수정할_수_있다() {
        final String now = LocalDateTime.now().format(dateTimeFormatter);
        ProposalAdminCommentEditDto editDto = ProposalAdminCommentEditDto.of(proposalIdWithAdminComment, "관리자댓글 수정입니다.");
        Proposal proposal = proposalService.editAdminComment(editDto);

        ProposalDto proposalDto = proposalService.getProposal(equalId(proposal.getId()), projection);
        assertThat(proposalDto.getAdminCommentDate().format(dateTimeFormatter), is(now));
        assertThat(proposalDto.getAdminComment(), is(editDto.getComment()));
    }

    /**
     * 3. 매니저는 관리자 의견을 수정할 수 없다.
     */
    @Test(expected = AccessDeniedException.class)
    @WithUserDetails("manager1@googl.co.kr")
    public void T_3_매니저_관리자_의견을_수정할_수_없다() {
        ProposalAdminCommentEditDto editDto = ProposalAdminCommentEditDto.of(proposalIdWithAdminComment, "매니저는 관리자댓글를 수정할 수 없습니다.");
        proposalService.editAdminComment(editDto);
    }

    /**
     * 4. 사용자는 관리자 의견을 수정할 수 없다.
     */
    @Test(expected = AccessDeniedException.class)
    @WithUserDetails("user1@googl.co.kr")
    public void T_4_사용자는_관리자_의견을_수정할_수_없다() {
        ProposalAdminCommentEditDto editDto = ProposalAdminCommentEditDto.of(proposalIdWithAdminComment, "사용자는 관리자댓글을 수정할 수 없습니다.");
        proposalService.editAdminComment(editDto);
    }

    /**
     * 5. 삭제된 제안에 관리자의견을 편집할 수 없다.
     */
    @Test(expected = NotFoundException.class)
    @WithUserDetails("admin1@googl.co.kr")
    public void T_5_삭제된_제안에_관리자의견을_편집할_수_없다() {
        ProposalAdminCommentEditDto editDto = ProposalAdminCommentEditDto.of(deletedProposalId, "사용자는 관리자댓글을 수정할 수 없습니다.");
        proposalService.editAdminComment(editDto);
    }

    /**
     * 6. 블럭된 제안에 관리자의견을 편집할 수 없다.
     */
    @Test(expected = NotFoundException.class)
    @WithUserDetails("admin1@googl.co.kr")
    public void T_6_블럭된_제안에_관리자의견을_편집할_수_없다() {
        ProposalAdminCommentEditDto editDto = ProposalAdminCommentEditDto.of(blockedProposalId, "사용자는 관리자댓글을 수정할 수 없습니다.");
        proposalService.editAdminComment(editDto);
    }
}
