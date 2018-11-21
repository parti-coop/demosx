package seoul.democracy.features.E_04_게시판;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import seoul.democracy.common.exception.NotFoundException;
import seoul.democracy.post.domain.Post;
import seoul.democracy.post.domain.PostType;
import seoul.democracy.post.dto.PostDto;
import seoul.democracy.post.dto.PostUpdateDto;
import seoul.democracy.post.service.PostService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;
import static seoul.democracy.post.dto.PostDto.projection;
import static seoul.democracy.post.predicate.PostPredicate.equalId;

/**
 * epic : 4. 게시판
 * story : 4.3 관리자는 게시판 글을 수정할 수 있다.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
    "file:src/test/resources/egovframework/spring-test/context-*.xml",
    "file:src/main/webapp/WEB-INF/config/egovframework/springmvc/egov-com-*.xml"
})
@Transactional
@Rollback
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class S_4_3_관리자는_게시판_글을_수정할_수_있다 {

    private final static DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH-mm");
    private final static String ip = "127.0.0.2";

    @Autowired
    private PostService postService;
    private PostUpdateDto updateDto;

    @Before
    public void setUp() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRemoteAddr(ip);
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        updateDto = PostUpdateDto.of(401L, Post.Status.OPEN, "공지사항글", "공지사항글입니다.");
    }

    /**
     * 1. 관리자는 글을 수정할 수 있다.
     */
    @Test
    @WithUserDetails("admin2@googl.co.kr")
    public void T_1_관리자는_글을_수정할_수_있다() {
        final String now = LocalDateTime.now().format(dateTimeFormatter);
        Post post = postService.update(updateDto);
        assertThat(post.getId(), is(notNullValue()));

        PostDto postDto = postService.getPost(equalId(post.getId()), projection);
        assertThat(postDto.getModifiedDate().format(dateTimeFormatter), is(now));
        assertThat(postDto.getModifiedBy().getEmail(), is("admin2@googl.co.kr"));
        assertThat(postDto.getModifiedIp(), is(ip));

        assertThat(postDto.getType(), is(PostType.NOTICE));
        assertThat(postDto.getStatus(), is(updateDto.getStatus()));

        assertThat(postDto.getTitle(), is(updateDto.getTitle()));
        assertThat(postDto.getContent(), is(updateDto.getContent()));
    }

    /**
     * 2. 매니저는 글을 수정할 수 없다.
     */
    @Test(expected = AccessDeniedException.class)
    @WithUserDetails("manager1@googl.co.kr")
    public void T_2_매니저는_글을_수정할_수_없다() {
        postService.update(updateDto);
    }

    /**
     * 3. 사용자는 글을 수정할 수 없다.
     */
    @Test(expected = AccessDeniedException.class)
    @WithUserDetails("user1@googl.co.kr")
    public void T_3_사용자는_글을_수정할_수_없다() {
        postService.update(updateDto);
    }

    /**
     * 4. 없는 글을 수정할 수 없다.
     */
    @Test(expected = NotFoundException.class)
    @WithUserDetails("admin1@googl.co.kr")
    public void T_4_없는_글을_수정할_수_없다() {
        Long notExistsId = 999L;
        updateDto.setId(notExistsId);
        postService.update(updateDto);
    }
}
