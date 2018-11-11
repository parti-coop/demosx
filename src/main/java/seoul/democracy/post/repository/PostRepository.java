package seoul.democracy.post.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import seoul.democracy.post.domain.Post;

public interface PostRepository extends PostRepositoryCustom, JpaRepository<Post, Long>, QueryDslPredicateExecutor<Post> {
}
