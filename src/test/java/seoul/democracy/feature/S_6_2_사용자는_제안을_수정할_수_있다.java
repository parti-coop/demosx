package seoul.democracy.feature;

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
import seoul.democracy.proposal.dto.ProposalDto;
import seoul.democracy.proposal.dto.ProposalUpdateDto;
import seoul.democracy.proposal.service.ProposalService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static seoul.democracy.proposal.dto.ProposalDto.projection;
import static seoul.democracy.proposal.predicate.ProposalPredicate.equalId;


/**
 * epic : 6. 제안
 * story : 6.2 사용자는 제안을 수정할 수 있다.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
    "file:src/test/resources/egovframework/spring-test/context-*.xml",
})
@Transactional
@Rollback
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class S_6_2_사용자는_제안을_수정할_수_있다 {

    private final static DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH-mm");
    private final static String ip = "127.0.0.2";

    @Autowired
    private ProposalService proposalService;


    @Before
    public void setUp() throws Exception {
    }

    /**
     * 1. 사용자는 제목, 내용을 수정할 수 있다.
     */
    @Test
    @WithUserDetails("user1@googl.co.kr")
    public void T_1_사용자는_제목_내용을_수정할_수_있다() {
        final String now = LocalDateTime.now().format(dateTimeFormatter);
        ProposalUpdateDto updateDto = ProposalUpdateDto.of(1L, "제안 수정합니다.", "제안 수정내용입니다.");
        Proposal proposal = proposalService.update(updateDto, ip);

        ProposalDto proposalDto = proposalService.getProposal(equalId(proposal.getId()), projection);
        assertThat(proposalDto.getModifiedDate().format(dateTimeFormatter), is(now));
        assertThat(proposalDto.getModifiedBy().getEmail(), is("user1@googl.co.kr"));
        assertThat(proposalDto.getModifiedIp(), is(ip));

        assertThat(proposalDto.getTitle(), is(updateDto.getTitle()));
        assertThat(proposalDto.getContent(), is(updateDto.getContent()));
    }

    /**
     * 2. 다른 사람의 제안을 수정할 수 없다.
     */
    @Test(expected = AccessDeniedException.class)
    @WithUserDetails("user2@googl.co.kr")
    public void T_2_다른_사람의_제안을_수정할_수_없다() {
        ProposalUpdateDto updateDto = ProposalUpdateDto.of(1L, "다른 사람이 제안을 수정합니다.", "제안 수정내용입니다.");
        proposalService.update(updateDto, ip);
    }

    /**
     * 3. 존재하지 않는 제안을 수정할 수 없다.
     */
    @Test(expected = NotFoundException.class)
    @WithUserDetails("user1@googl.co.kr")
    public void T_3_존재하지_않는_제안을_수정할_수_없다() {
        ProposalUpdateDto updateDto = ProposalUpdateDto.of(999L, "없는 제안을 수정합니다.", "제안 수정내용입니다.");
        proposalService.update(updateDto, ip);
    }

    /**
     * 4. 삭제된 제안을 수정할 수 없다.
     */
    @Test(expected = NotFoundException.class)
    @WithUserDetails("user1@googl.co.kr")
    public void T_4_삭제된_제안을_수정할_수_없다() {
        ProposalUpdateDto updateDto = ProposalUpdateDto.of(2L, "삭제된 제안을 수정합니다.", "제안 수정내용입니다.");
        proposalService.update(updateDto, ip);
    }

    /**
     * 5. 블럭된 제안을 수정할 수 없다.
     */
    @Test(expected = NotFoundException.class)
    @WithUserDetails("user1@googl.co.kr")
    public void T_5_블럭된_제안을_수정할_수_없다() {
        ProposalUpdateDto updateDto = ProposalUpdateDto.of(3L, "블럭된 제안을 수정합니다.", "제안 수정내용입니다.");
        proposalService.update(updateDto, ip);
    }
}
