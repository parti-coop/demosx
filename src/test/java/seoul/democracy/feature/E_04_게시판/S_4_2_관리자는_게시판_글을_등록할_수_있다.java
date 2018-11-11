package seoul.democracy.feature.E_04_게시판;

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
import seoul.democracy.post.domain.Post;
import seoul.democracy.post.domain.PostType;
import seoul.democracy.post.dto.PostCreateDto;
import seoul.democracy.post.dto.PostDto;
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
 * story : 4.2 관리자는 게시판 글을 등록할 수 있다.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
    "file:src/test/resources/egovframework/spring-test/context-*.xml",
})
@Transactional
@Rollback
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class S_4_2_관리자는_게시판_글을_등록할_수_있다 {

    private final static DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH-mm");
    private final static String ip = "127.0.0.1";
    private MockHttpServletRequest request;

    @Autowired
    private PostService postService;
    private PostCreateDto createDto;

    @Before
    public void setUp() throws Exception {
        request = new MockHttpServletRequest();
        request.setRemoteAddr(ip);
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        createDto = PostCreateDto.of(Post.Status.OPEN, "공지사항글", "공지사항글입니다.");
    }

    /**
     * 1. 관리자는 글을 등록할 수 있다.
     */
    @Test
    @WithUserDetails("admin1@googl.co.kr")
    public void T_1_관리자는_글을_등록할_수_있다() {
        final String now = LocalDateTime.now().format(dateTimeFormatter);
        Post post = postService.create(PostType.NOTICE, createDto);
        assertThat(post.getId(), is(notNullValue()));

        PostDto postDto = postService.getPost(equalId(post.getId()), projection);
        assertThat(postDto.getCreatedDate().format(dateTimeFormatter), is(now));
        assertThat(postDto.getModifiedDate().format(dateTimeFormatter), is(now));
        assertThat(postDto.getCreatedBy().getEmail(), is("admin1@googl.co.kr"));
        assertThat(postDto.getModifiedBy().getEmail(), is("admin1@googl.co.kr"));
        assertThat(postDto.getCreatedIp(), is(ip));
        assertThat(postDto.getModifiedIp(), is(ip));

        assertThat(postDto.getType(), is(PostType.NOTICE));
        assertThat(postDto.getStatus(), is(createDto.getStatus()));

        assertThat(postDto.getTitle(), is(createDto.getTitle()));
        assertThat(postDto.getContent(), is(createDto.getContent()));
    }

    /**
     * 2. 매니저는 글을 등록할 수 없다.
     */
    @Test(expected = AccessDeniedException.class)
    @WithUserDetails("manager1@googl.co.kr")
    public void T_2_매니저는_글을_등록할_수_없다() {
        postService.create(PostType.NOTICE, createDto);
    }

    /**
     * 3. 사용자는 글을 등록할 수 없다.
     */
    @Test(expected = AccessDeniedException.class)
    @WithUserDetails("user1@googl.co.kr")
    public void T_3_사용자는_글을_등록할_수_없다() {
        postService.create(PostType.NOTICE, createDto);
    }
}
