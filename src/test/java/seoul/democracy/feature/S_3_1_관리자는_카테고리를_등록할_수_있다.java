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
import seoul.democracy.common.exception.AlreadyExistsException;
import seoul.democracy.issue.domain.Category;
import seoul.democracy.issue.dto.CategoryCreateDto;
import seoul.democracy.issue.dto.CategoryDto;
import seoul.democracy.issue.service.CategoryService;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;
import static seoul.democracy.issue.predicate.CategoryPredicate.equalId;


/**
 * epic : 3. 카테고리 관리
 * story : 3.1 관리자는 카테고리를 등록할 수 있다.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
    "file:src/test/resources/egovframework/spring-test/context-*.xml",
})
@Transactional
@Rollback
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class S_3_1_관리자는_카테고리를_등록할_수_있다 {

    @Autowired
    private CategoryService categoryService;

    @Before
    public void setUp() throws Exception {
    }

    /**
     * 1. 관리자는 이름, 사용여부, 순번으로 카테고리를 등록할 수 있다.
     */
    @Test
    @WithUserDetails("admin1@googl.co.kr")
    public void T_1_관리자는_이름_사용여부_순번으로_카테고리를_등록할_수_있다() {
        CategoryCreateDto createDto = CategoryCreateDto.of("뉴카테고리", true, 10);
        Category category = categoryService.create(createDto);
        assertThat(category.getId(), is(notNullValue()));

        CategoryDto categoryDto = categoryService.getCategory(equalId(category.getId()), CategoryDto.projection);
        assertThat(categoryDto.getName(), is(createDto.getName()));
        assertThat(categoryDto.getEnabled(), is(createDto.getEnabled()));
        assertThat(categoryDto.getSequence(), is(createDto.getSequence()));
    }

    /**
     * 2. 동일한 이름의 카테고리를 등록할 수 없다.
     */
    @Test(expected = AlreadyExistsException.class)
    @WithUserDetails("admin1@googl.co.kr")
    public void T_2_동일한_이름의_카테고리를_등록할_수_없다() {
        CategoryCreateDto createDto = CategoryCreateDto.of("건강", true, 10);
        categoryService.create(createDto);
    }

    /**
     * 3. 매니저는 카테고리를 등록할 수 없다.
     */
    @Test(expected = AccessDeniedException.class)
    @WithUserDetails("manager1@googl.co.kr")
    public void T_3_매니저는_카테고리를_등록할_수_없다() {
        CategoryCreateDto createDto = CategoryCreateDto.of("매니저카테고리", true, 10);
        categoryService.create(createDto);
    }

    /**
     * 4. 유저는 카테고리를 등록할 수 없다.
     */
    @Test(expected = AccessDeniedException.class)
    @WithUserDetails("user1@googl.co.kr")
    public void T_4_유저는_카테고리를_등록할_수_없다() {
        CategoryCreateDto createDto = CategoryCreateDto.of("유저카테고리", true, 10);
        categoryService.create(createDto);
    }
}