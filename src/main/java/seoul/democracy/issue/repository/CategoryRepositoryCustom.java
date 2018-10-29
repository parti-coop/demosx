package seoul.democracy.issue.repository;

import com.mysema.query.types.Expression;
import com.mysema.query.types.Predicate;
import seoul.democracy.issue.dto.CategoryDto;

import java.util.List;

public interface CategoryRepositoryCustom {

    List<CategoryDto> findAll(Predicate predicate, Expression<CategoryDto> projection);

    CategoryDto findOne(Predicate predicate, Expression<CategoryDto> projection);
}
