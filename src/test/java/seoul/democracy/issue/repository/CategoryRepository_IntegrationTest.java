package seoul.democracy.issue.repository;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import seoul.democracy.issue.dto.CategoryDto;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static seoul.democracy.issue.dto.CategoryDto.projection;
import static seoul.democracy.issue.predicate.CategoryPredicate.equalId;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
    "file:src/test/resources/egovframework/spring-test/context-*.xml",
    "file:src/main/webapp/WEB-INF/config/egovframework/springmvc/egov-com-*.xml"
})
@Transactional
@Rollback
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CategoryRepository_IntegrationTest {

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    public void projection_동작확인() {
        CategoryDto categoryDto = categoryRepository.findOne(equalId(1L), projection);

        assertThat(categoryDto.getId(), is(1L));
        assertThat(categoryDto.getName(), is("복지"));
        assertThat(categoryDto.getEnabled(), is(true));
        assertThat(categoryDto.getSequence(), is(1));
    }

}