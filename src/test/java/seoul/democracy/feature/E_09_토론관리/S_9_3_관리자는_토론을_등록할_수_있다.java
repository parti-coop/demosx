package seoul.democracy.feature.E_09_토론관리;

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
import seoul.democracy.debate.domain.Debate;
import seoul.democracy.debate.dto.DebateCreateDto;
import seoul.democracy.debate.dto.DebateDto;
import seoul.democracy.debate.service.DebateService;
import seoul.democracy.issue.domain.Issue;
import seoul.democracy.issue.dto.IssueFileDto;
import seoul.democracy.opinion.domain.OpinionType;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static seoul.democracy.debate.dto.DebateDto.projection;
import static seoul.democracy.debate.predicate.DebatePredicate.equalId;


/**
 * epic : 9. 토론관리
 * story : 9.3 관리자는 토론을 등록할 수 있다.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
    "file:src/test/resources/egovframework/spring-test/context-*.xml",
})
@Transactional
@Rollback
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class S_9_3_관리자는_토론을_등록할_수_있다 {

    private final static DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH-mm");
    private final static String ip = "127.0.0.2";

    @Autowired
    private DebateService debateService;

    private DebateCreateDto createDto;

    @Before
    public void setUp() throws Exception {
        createDto = DebateCreateDto.of("thumbnail.jpg", "복지", OpinionType.PROPOSAL,
            LocalDate.of(2019, 10, 10), LocalDate.of(2019, 12, 12),
            "토론 + 제안의견", "제안의견인 토론입니다.", Issue.Status.OPEN,
            Arrays.asList(IssueFileDto.of("파일1", "file1"), IssueFileDto.of("파일2", "file2")),
            Arrays.asList(1L, 11L));
    }

    /**
     * 1. 관리자는 제안의견으로 토론을 등록할 수 있다.
     */
    @Test
    @WithUserDetails("admin1@googl.co.kr")
    public void T_1_관리자는_제안의견으로_토론을_등록할_수_있다() {
        final String now = LocalDateTime.now().format(dateTimeFormatter);

        Debate debate = debateService.create(createDto, ip);
        assertThat(debate.getId(), is(notNullValue()));

        DebateDto debateDto = debateService.getDebate(equalId(debate.getId()), projection, true, true);
        assertThat(debateDto.getCreatedDate().format(dateTimeFormatter), is(now));
        assertThat(debateDto.getModifiedDate().format(dateTimeFormatter), is(now));
        assertThat(debateDto.getCreatedBy().getEmail(), is("admin1@googl.co.kr"));
        assertThat(debateDto.getModifiedBy().getEmail(), is("admin1@googl.co.kr"));
        assertThat(debateDto.getCreatedIp(), is(ip));
        assertThat(debateDto.getModifiedIp(), is(ip));

        assertThat(debateDto.getOpinionType(), is(createDto.getOpinionType()));
        assertThat(debateDto.getCategory().getName(), is(createDto.getCategory()));

        assertThat(debateDto.getStats().getViewCount(), is(0L));
        assertThat(debateDto.getStats().getLikeCount(), is(0L));
        assertThat(debateDto.getStats().getOpinionCount(), is(0L));
        assertThat(debateDto.getStats().getYesCount(), is(0L));
        assertThat(debateDto.getStats().getNoCount(), is(0L));
        assertThat(debateDto.getStats().getEtcCount(), is(0L));

        assertThat(debateDto.getThumbnail(), is(createDto.getThumbnail()));
        assertThat(debateDto.getStartDate(), is(createDto.getStartDate()));
        assertThat(debateDto.getEndDate(), is(createDto.getEndDate()));

        assertThat(debateDto.getTitle(), is(createDto.getTitle()));
        assertThat(debateDto.getContent(), is(createDto.getContent()));

        assertThat(debateDto.getStatus(), is(createDto.getStatus()));

        assertThat(debateDto.getFiles(), contains(createDto.getFiles().toArray(new IssueFileDto[0])));
        assertThat(debateDto.getRelations(), contains(createDto.getRelations().toArray(new Long[0])));
    }

    /**
     * 2. 관리자는 토론의견으로 토론을 등록할 수 있다.
     */
    @Test
    @WithUserDetails("admin1@googl.co.kr")
    public void T_2_관리자는_토론의견으로_토론을_등록할_수_있다() {
        createDto.setOpinionType(OpinionType.DEBATE);
        Debate debate = debateService.create(createDto, ip);

        DebateDto debateDto = debateService.getDebate(equalId(debate.getId()), projection, true, true);
        assertThat(debateDto.getOpinionType(), is(createDto.getOpinionType()));
    }

    /**
     * 3. 존재하지 않는 카테고리로 토론을 등록할 수 없다.
     */
    @Test(expected = BadRequestException.class)
    @WithUserDetails("admin1@googl.co.kr")
    public void T_3_존재하지_않는_카테고리로_토론을_등록할_수_없다() {
        createDto.setCategory("없는 카테고리");
        debateService.create(createDto, ip);
    }

    /**
     * 4. 매니저는 토론을 등록할 수 없다.
     */
    @Test(expected = AccessDeniedException.class)
    @WithUserDetails("manager1@googl.co.kr")
    public void T_4_매니저는_토론을_등록할_수_없다() {
        debateService.create(createDto, ip);
    }

    /**
     * 5. 사용자는 토론을 등록할 수 없다.
     */
    @Test(expected = AccessDeniedException.class)
    @WithUserDetails("user1@googl.co.kr")
    public void T_5_사용자는_토론을_등록할_수_없다() {
        debateService.create(createDto, ip);
    }
}
