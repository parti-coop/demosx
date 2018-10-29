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
import seoul.democracy.common.exception.NotFoundException;
import seoul.democracy.issue.domain.Category;
import seoul.democracy.issue.dto.CategoryDto;
import seoul.democracy.issue.dto.CategoryUpdateDto;
import seoul.democracy.issue.service.CategoryService;

import static org.hamcrest.Matchers.is;
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
public class S_3_2_관리자는_카테고리를_수정할_수_있다 {

    @Autowired
    private CategoryService categoryService;

    @Before
    public void setUp() throws Exception {
    }

    /**
     * 1. 관리자는 이름, 사용여부, 순번을 변경할 수 있다.
     */
    @Test
    @WithUserDetails("admin1@googl.co.kr")
    public void T_1_관리자는_이름_사용여부_순번을_변경할_수_있다() {
        CategoryUpdateDto updateDto = CategoryUpdateDto.of(1L, "뉴복지", false, 10);
        Category category = categoryService.update(updateDto);

        CategoryDto categoryDto = categoryService.getCategory(equalId(category.getId()), CategoryDto.projection);
        assertThat(categoryDto.getId(), is(updateDto.getId()));
        assertThat(categoryDto.getName(), is(updateDto.getName()));
        assertThat(categoryDto.getEnabled(), is(updateDto.getEnabled()));
        assertThat(categoryDto.getSequence(), is(updateDto.getSequence()));
    }

    /**
     * 2. 이미 사용중인 이름으로 변경할 수 없다.
     */
    @Test(expected = AlreadyExistsException.class)
    @WithUserDetails("admin1@googl.co.kr")
    public void T_2_이미_사용중인_이름으로_변경할_수_없다() {
        CategoryUpdateDto updateDto = CategoryUpdateDto.of(1L, "건강", true, 1);
        categoryService.update(updateDto);
    }

    /**
     * 3. 카테고리를 숨김처리할 수 있다
     */
    @Test
    @WithUserDetails("admin1@googl.co.kr")
    public void T_3_카테고리를_숨김처리할_수_있다() {
        CategoryUpdateDto updateDto = CategoryUpdateDto.of(1L, "복지", false, 1);
        Category category = categoryService.update(updateDto);

        CategoryDto categoryDto = categoryService.getCategory(equalId(category.getId()), CategoryDto.projection);
        assertThat(categoryDto.getEnabled(), is(updateDto.getEnabled()));
    }

    /**
     * 4. 존재하지 않는 카테고리는 수정할 수 없다.
     */
    @Test(expected = NotFoundException.class)
    @WithUserDetails("admin1@googl.co.kr")
    public void T_4_존재하지_않는_카테고리는_수정할_수_없다() {
        CategoryUpdateDto updateDto = CategoryUpdateDto.of(999L, "없는카테고리", true, 1);
        categoryService.update(updateDto);
    }

    /**
     * 5. 매니저는 카테고리를 수정할 수 없다.
     */
    @Test(expected = AccessDeniedException.class)
    @WithUserDetails("manager1@googl.co.kr")
    public void T_5_매니저는_카테고리를_등록할_수_없다() {
        CategoryUpdateDto updateDto = CategoryUpdateDto.of(1L, "매니저카테고리", true, 1);
        categoryService.update(updateDto);
    }

    /**
     * 6. 사용자는 카테고리를 수정할 수 없다.
     */
    @Test(expected = AccessDeniedException.class)
    @WithUserDetails("user1@googl.co.kr")
    public void T_6_사용자는_카테고리를_수정할_수_없다() {
        CategoryUpdateDto updateDto = CategoryUpdateDto.of(1L, "사용자카테고리", true, 1);
        categoryService.update(updateDto);
    }
}