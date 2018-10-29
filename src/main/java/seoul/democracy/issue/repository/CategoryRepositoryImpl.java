package seoul.democracy.issue.repository;

import com.mysema.query.types.Expression;
import com.mysema.query.types.Predicate;
import org.springframework.data.jpa.repository.support.QueryDslRepositorySupport;
import seoul.democracy.issue.domain.Category;
import seoul.democracy.issue.dto.CategoryDto;

import java.util.List;

import static seoul.democracy.issue.domain.QCategory.category;

public class CategoryRepositoryImpl extends QueryDslRepositorySupport implements CategoryRepositoryCustom {

    public CategoryRepositoryImpl() {
        super(Category.class);
    }

    @Override
    public List<CategoryDto> findAll(Predicate predicate, Expression<CategoryDto> projection) {
        return from(category)
                   .where(predicate)
                   .orderBy(category.sequence.asc(), category.name.asc())
                   .list(projection);
    }

    @Override
    public CategoryDto findOne(Predicate predicate, Expression<CategoryDto> projection) {
        return from(category)
                   .where(predicate)
                   .uniqueResult(projection);
    }
}
