package seoul.democracy.post.repository;

import com.mysema.query.SearchResults;
import com.mysema.query.jpa.JPQLQuery;
import com.mysema.query.support.Expressions;
import com.mysema.query.types.Expression;
import com.mysema.query.types.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QueryDslRepositorySupport;
import seoul.democracy.post.domain.Post;
import seoul.democracy.post.dto.PostDto;

import static seoul.democracy.post.domain.QPost.post;
import static seoul.democracy.user.dto.UserDto.createdBy;
import static seoul.democracy.user.dto.UserDto.modifiedBy;

public class PostRepositoryImpl extends QueryDslRepositorySupport implements PostRepositoryCustom {

    final private static Expression<Long> constant = Expressions.constant(1L);

    public PostRepositoryImpl() {
        super(Post.class);
    }

    private JPQLQuery getQuery(Expression<PostDto> projection) {
        JPQLQuery query = from(post);
        if (projection == PostDto.projection) {
            query.innerJoin(post.createdBy, createdBy);
            query.innerJoin(post.modifiedBy, modifiedBy);
        } else if (projection == PostDto.projectionForAdminList || projection == PostDto.projectionForAdminDetail) {
            query.innerJoin(post.createdBy, createdBy);
        }
        return query;
    }

    @Override
    public Page<PostDto> findAll(Predicate predicate, Pageable pageable, Expression<PostDto> projection) {
        SearchResults<PostDto> results = getQuerydsl()
                                             .applyPagination(
                                                 pageable,
                                                 getQuery(projection)
                                                     .where(predicate))
                                             .listResults(projection);
        return new PageImpl<>(results.getResults(), pageable, results.getTotal());
    }

    @Override
    public PostDto findOne(Predicate predicate, Expression<PostDto> projection) {
        return getQuery(projection)
                   .where(predicate)
                   .uniqueResult(projection);
    }

    @Override
    public void increaseViewCount(Long id) {
        update(post)
            .where(post.id.eq(id))
            .set(post.viewCount, post.viewCount.add(constant))
            .execute();
    }
}
