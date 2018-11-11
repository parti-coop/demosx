package seoul.democracy.post.repository;

import com.mysema.query.types.Expression;
import com.mysema.query.types.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import seoul.democracy.post.dto.PostDto;

public interface PostRepositoryCustom {

    Page<PostDto> findAll(Predicate predicate, Pageable pageable, Expression<PostDto> projection);

    PostDto findOne(Predicate predicate, Expression<PostDto> projection);

}
