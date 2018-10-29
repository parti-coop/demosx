package seoul.democracy.issue.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import seoul.democracy.issue.domain.Category;

public interface CategoryRepository extends CategoryRepositoryCustom, JpaRepository<Category, Long>, QueryDslPredicateExecutor<Category> {
}
