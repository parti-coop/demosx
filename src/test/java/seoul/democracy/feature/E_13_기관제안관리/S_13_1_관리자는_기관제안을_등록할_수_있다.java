package seoul.democracy.feature.E_13_기관제안관리;

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
import seoul.democracy.debate.domain.Debate;
import seoul.democracy.debate.dto.DebateCreateDto;
import seoul.democracy.debate.dto.DebateDto;
import seoul.democracy.debate.service.DebateService;
import seoul.democracy.issue.domain.Issue;
import seoul.democracy.issue.domain.IssueGroup;
import seoul.democracy.issue.dto.IssueFileDto;
import seoul.democracy.opinion.domain.OpinionType;

import java.time.LocalDate;
import java.util.Arrays;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
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
public class S_13_1_관리자는_기관제안을_등록할_수_있다 {

    private final static String ip = "127.0.0.2";
    private MockHttpServletRequest request;

    @Autowired
    private DebateService debateService;

    private DebateCreateDto createDto;

    @Before
    public void setUp() throws Exception {
        request = new MockHttpServletRequest();
        request.setRemoteAddr(ip);
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        createDto = DebateCreateDto.of("thumbnail.jpg", "복지", OpinionType.PROPOSAL,
            LocalDate.of(2019, 10, 10), LocalDate.of(2019, 12, 12),
            "토론 + 제안의견", "토론한줄성명","제안의견인 토론입니다.", Issue.Status.OPEN,
            Arrays.asList(IssueFileDto.of("파일1", "file1"), IssueFileDto.of("파일2", "file2")),
            Arrays.asList(1L, 11L), null);
    }

    /**
     * 1. 관리자는 기관제안을 등록할 수 있다.
     */
    @Test
    @WithUserDetails("admin1@googl.co.kr")
    public void T_1_관리자는_기관제안을_등록할_수_있다() {
        Debate debate = debateService.create(IssueGroup.ORG, createDto);
        assertThat(debate.getId(), is(notNullValue()));

        DebateDto debateDto = debateService.getDebate(equalId(debate.getId()), projection, true, true);
        assertThat(debateDto.getGroup(), is(IssueGroup.ORG));
    }

}
