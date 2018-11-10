package seoul.democracy.feature.E_08_토론;

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
import seoul.democracy.common.exception.AlreadyExistsException;
import seoul.democracy.common.exception.BadRequestException;
import seoul.democracy.common.exception.NotFoundException;
import seoul.democracy.debate.dto.DebateDto;
import seoul.democracy.debate.predicate.DebatePredicate;
import seoul.democracy.debate.service.DebateService;
import seoul.democracy.opinion.domain.Opinion;
import seoul.democracy.opinion.dto.OpinionCreateDto;
import seoul.democracy.opinion.dto.OpinionDto;
import seoul.democracy.opinion.service.OpinionService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;
import static seoul.democracy.opinion.dto.OpinionDto.projection;
import static seoul.democracy.opinion.predicate.OpinionPredicate.equalId;


/**
 * epic : 8. 토론
 * story : 8.3 사용자는 토론의견을 등록할 수 있다.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
    "file:src/test/resources/egovframework/spring-test/context-*.xml",
})
@Transactional
@Rollback
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class S_8_3_사용자는_토론의견을_등록할_수_있다 {

    private final static DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH-mm");
    private final static String ip = "127.0.0.2";
    private MockHttpServletRequest request;

    @Autowired
    private OpinionService opinionService;

    @Autowired
    private DebateService debateService;

    private final Long debateIdInOpenWithProposal = 101L;
    private final Long debateIdInProgressWithProposal = 111L;

    private final Long debateIdInOpenWithDebate = 121L;
    private final Long debateIdInProgressWithDebate = 131L;

    private final Long closedDebateId = 141L;


    @Before
    public void setUp() throws Exception {
        request = new MockHttpServletRequest();
        request.setRemoteAddr(ip);
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
    }

    /**
     * 1. 사용자는 진행 중인 토론에 제안의견을 등록할 수 있다.
     */
    @Test
    @WithUserDetails("user3@googl.co.kr")
    public void T_1_1_사용자는_진행_중인_토론에_제안의견을_등록할_수_있다() {
        final String now = LocalDateTime.now().format(dateTimeFormatter);
        OpinionCreateDto createDto = OpinionCreateDto.of(debateIdInProgressWithProposal, "토론의 제안의견 입니다.");
        Opinion opinion = opinionService.createOpinion(createDto);
        assertThat(opinion.getId(), is(notNullValue()));

        OpinionDto opinionDto = opinionService.getOpinion(equalId(opinion.getId()), projection);
        assertThat(opinionDto.getCreatedDate().format(dateTimeFormatter), is(now));
        assertThat(opinionDto.getModifiedDate().format(dateTimeFormatter), is(now));
        assertThat(opinionDto.getCreatedBy().getEmail(), is("user3@googl.co.kr"));
        assertThat(opinionDto.getModifiedBy().getEmail(), is("user3@googl.co.kr"));
        assertThat(opinionDto.getCreatedIp(), is(ip));
        assertThat(opinionDto.getModifiedIp(), is(ip));

        assertThat(opinionDto.getIssue().getId(), is(createDto.getIssueId()));

        assertThat(opinionDto.getLikeCount(), is(0L));
        assertThat(opinionDto.getContent(), is(createDto.getContent()));
        assertThat(opinionDto.getStatus(), is(Opinion.Status.OPEN));
        assertThat(opinionDto.getVote(), is(Opinion.Vote.ETC));

        DebateDto debateDto = debateService.getDebate(DebatePredicate.equalId(opinion.getIssue().getId()), DebateDto.projection, false, false);
        assertThat(debateDto.getStats().getOpinionCount(), is(4L));
        assertThat(debateDto.getStats().getApplicantCount(), is(3L));
    }

    /**
     * 2. 사용자는 진행 중이 아닌 토론에 제안의견을 등록할 수 없다.
     */
    @Test(expected = BadRequestException.class)
    @WithUserDetails("user1@googl.co.kr")
    public void T_2_사용자는_진행_중이_아닌_토론에_제안의견을_등록할_수_없다() {
        OpinionCreateDto createDto = OpinionCreateDto.of(debateIdInOpenWithProposal, "토론의 제안의견 입니다.");
        opinionService.createOpinion(createDto);
    }

    /**
     * 3. 사용자는 여러번 제안의견을 등록할 수 있다.
     */
    @Test
    @WithUserDetails("user1@googl.co.kr")
    public void T_3_사용자는_여러번_제안의견을_등록할_수_있다() {
        OpinionCreateDto createDto = OpinionCreateDto.of(debateIdInProgressWithProposal, "토론의 제안의견을 다시 등록합니다.");
        Opinion opinion = opinionService.createOpinion(createDto);
        assertThat(opinion.getId(), is(notNullValue()));

        OpinionDto opinionDto = opinionService.getOpinion(equalId(opinion.getId()), projection);

        DebateDto debateDto = debateService.getDebate(DebatePredicate.equalId(opinionDto.getIssue().getId()), DebateDto.projection, false, false);
        assertThat(debateDto.getStats().getOpinionCount(), is(4L));
        assertThat(debateDto.getStats().getApplicantCount(), is(2L));
    }

    /**
     * 4. 사용자는 진행 중 토론 찬성의견을 등록할 수 있다.
     */
    @Test
    @WithUserDetails("user1@googl.co.kr")
    public void T_4_사용자는_진행_중_토론_찬성의견을_등록할_수_있다() {
        OpinionCreateDto createDto = OpinionCreateDto.of(debateIdInProgressWithDebate, Opinion.Vote.YES, "토론의 제안의견 입니다.");
        Opinion opinion = opinionService.createOpinion(createDto);

        OpinionDto opinionDto = opinionService.getOpinion(equalId(opinion.getId()), projection);
        assertThat(opinionDto.getLikeCount(), is(0L));
        assertThat(opinionDto.getContent(), is(createDto.getContent()));
        assertThat(opinionDto.getStatus(), is(Opinion.Status.OPEN));
        assertThat(opinionDto.getVote(), is(createDto.getVote()));

        DebateDto debateDto = debateService.getDebate(DebatePredicate.equalId(opinion.getIssue().getId()), DebateDto.projection, false, false);
        assertThat(debateDto.getStats().getOpinionCount(), is(4L));
        assertThat(debateDto.getStats().getYesCount(), is(2L));
        assertThat(debateDto.getStats().getNoCount(), is(1L));
        assertThat(debateDto.getStats().getEtcCount(), is(1L));
        assertThat(debateDto.getStats().getApplicantCount(), is(4L));
    }

    /**
     * 5. 사용자는 진행 중 토론 반대의견을 등록할 수 있다.
     */
    @Test
    @WithUserDetails("user1@googl.co.kr")
    public void T_5_사용자는_진행_중_토론_반대의견을_등록할_수_있다() {
        OpinionCreateDto createDto = OpinionCreateDto.of(debateIdInProgressWithDebate, Opinion.Vote.NO, "토론의 제안의견 입니다.");
        Opinion opinion = opinionService.createOpinion(createDto);

        OpinionDto opinionDto = opinionService.getOpinion(equalId(opinion.getId()), projection);
        assertThat(opinionDto.getVote(), is(createDto.getVote()));

        DebateDto debateDto = debateService.getDebate(DebatePredicate.equalId(opinion.getIssue().getId()), DebateDto.projection, false, false);
        assertThat(debateDto.getStats().getOpinionCount(), is(4L));
        assertThat(debateDto.getStats().getYesCount(), is(1L));
        assertThat(debateDto.getStats().getNoCount(), is(2L));
        assertThat(debateDto.getStats().getEtcCount(), is(1L));
        assertThat(debateDto.getStats().getApplicantCount(), is(4L));
    }

    /**
     * 6. 사용자는 진행 중 토론 기타의견을 등록할 수 있다.
     */
    @Test
    @WithUserDetails("user1@googl.co.kr")
    public void T_6_사용자는_진행_중_토론_기타의견을_등록할_수_있다() {
        OpinionCreateDto createDto = OpinionCreateDto.of(debateIdInProgressWithDebate, Opinion.Vote.ETC, "토론의 제안의견 입니다.");
        Opinion opinion = opinionService.createOpinion(createDto);

        OpinionDto opinionDto = opinionService.getOpinion(equalId(opinion.getId()), projection);
        assertThat(opinionDto.getVote(), is(createDto.getVote()));

        DebateDto debateDto = debateService.getDebate(DebatePredicate.equalId(opinion.getIssue().getId()), DebateDto.projection, false, false);
        assertThat(debateDto.getStats().getOpinionCount(), is(4L));
        assertThat(debateDto.getStats().getYesCount(), is(1L));
        assertThat(debateDto.getStats().getNoCount(), is(1L));
        assertThat(debateDto.getStats().getEtcCount(), is(2L));
        assertThat(debateDto.getStats().getApplicantCount(), is(4L));
    }

    /**
     * 7. 사용자는 진행 중이 아닐때 토론의견을 등록할 수 없다.
     */
    @Test(expected = BadRequestException.class)
    @WithUserDetails("user1@googl.co.kr")
    public void T_7_사용자는_진행_중이_아닐때_토론의견을_등록할_수_없다() {
        OpinionCreateDto createDto = OpinionCreateDto.of(debateIdInOpenWithDebate, Opinion.Vote.YES, "토론의 제안의견 입니다.");
        opinionService.createOpinion(createDto);
    }

    /**
     * 8. 사용자는 토론의견은 한번만 등록 가능하다.
     */
    @Test(expected = AlreadyExistsException.class)
    @WithUserDetails("user2@googl.co.kr")
    public void T_8_사용자는_토론의견은_한번만_등록_가능하다() {
        OpinionCreateDto createDto = OpinionCreateDto.of(debateIdInProgressWithDebate, Opinion.Vote.YES, "토론의 제안의견 입니다.");
        opinionService.createOpinion(createDto);
    }

    /**
     * 9. 비공개 토론에 의견을 등록할 수 없다.
     */
    @Test(expected = NotFoundException.class)
    @WithUserDetails("user1@googl.co.kr")
    public void T_9_비공개_토론에_의견을_등록할_수_없다() {
        OpinionCreateDto createDto = OpinionCreateDto.of(closedDebateId, Opinion.Vote.YES, "토론의 제안의견 입니다.");
        opinionService.createOpinion(createDto);
    }

}
