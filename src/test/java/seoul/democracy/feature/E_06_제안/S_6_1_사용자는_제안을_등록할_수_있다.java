package seoul.democracy.feature.E_06_제안;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import seoul.democracy.common.exception.BadRequestException;
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
    }

    /**
     * 1. 사용자는 제목,내용, 카테고리, 첨부파일로 제안을 등록할 수 있다.
     */
    @Test
    @WithUserDetails("user1@googl.co.kr")
    public void T_1_사용자는_제목_내용_카테고리_첨부파일로_제안을_등록할_수_있다() {
        final String now = LocalDateTime.now().format(dateTimeFormatter);
        ProposalCreateDto createDto = ProposalCreateDto.of("제안합니다.", "제안내용입니다.", "복지");
        Proposal proposal = proposalService.create(createDto, ip);
        assertThat(proposal.getId(), is(notNullValue()));

        ProposalDto proposalDto = proposalService.getProposal(equalId(proposal.getId()), projection);
        assertThat(proposalDto.getCreatedDate().format(dateTimeFormatter), is(now));
        assertThat(proposalDto.getModifiedDate().format(dateTimeFormatter), is(now));
        assertThat(proposalDto.getCreatedBy().getEmail(), is("user1@googl.co.kr"));
        assertThat(proposalDto.getModifiedBy().getEmail(), is("user1@googl.co.kr"));
        assertThat(proposalDto.getCreatedIp(), is(ip));
        assertThat(proposalDto.getModifiedIp(), is(ip));

        assertThat(proposalDto.getOpinionType(), is(OpinionType.PROPOSAL));
        assertThat(proposalDto.getCategory().getName(), is(createDto.getCategory()));

        assertThat(proposalDto.getStats().getViewCount(), is(0L));
        assertThat(proposalDto.getStats().getLikeCount(), is(0L));
        assertThat(proposalDto.getStats().getOpinionCount(), is(0L));
        assertThat(proposalDto.getStats().getYesCount(), is(0L));
        assertThat(proposalDto.getStats().getNoCount(), is(0L));
        assertThat(proposalDto.getStats().getEtcCount(), is(0L));

        assertThat(proposalDto.getStatus(), is(Proposal.Status.OPEN));

        assertThat(proposalDto.getTitle(), is(createDto.getTitle()));
        assertThat(proposalDto.getContent(), is(createDto.getContent()));
    }

    /**
     * 2. 비공개 카테고리에 제안을 등록할 수 없다.
     */
    @Test(expected = BadRequestException.class)
    @WithUserDetails("user1@googl.co.kr")
    public void T_2_비공개_카테고리에_제안을_등록할_수_없다() {
        ProposalCreateDto createDto = ProposalCreateDto.of("제안합니다.", "제안내용입니다.", "비공개");
        proposalService.create(createDto, ip);
    }

    /**
     * 3. 존재하지 않는 카테고리로 제안을 등록할 수없다.
     */
    @Test(expected = BadRequestException.class)
    @WithUserDetails("user1@googl.co.kr")
    public void T_3_존재하지_않는_카테고리로_제안을_등록할_수_없다() {
        ProposalCreateDto createDto = ProposalCreateDto.of("제안합니다.", "제안내용입니다.", "notExists");
        proposalService.create(createDto, ip);
    }
}
