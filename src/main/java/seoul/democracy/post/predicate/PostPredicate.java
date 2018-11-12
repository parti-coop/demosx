package seoul.democracy.post.predicate;

import com.mysema.query.types.ExpressionUtils;
import com.mysema.query.types.Predicate;
import org.springframework.util.StringUtils;
import seoul.democracy.post.domain.Post;
import seoul.democracy.post.domain.PostType;

import static seoul.democracy.post.domain.Post.Status.OPEN;
import static seoul.democracy.post.domain.QPost.post;

public class PostPredicate {

    public static Predicate equalId(Long id) {
        return post.id.eq(id);
    }

    public static Predicate equalType(PostType type) {
        return post.type.eq(type);
    }

    public static Predicate predicateForSiteList(PostType type, String search) {
        Predicate predicate = ExpressionUtils.and(post.type.eq(type), post.status.eq(OPEN));

        if (StringUtils.isEmpty(search)) return predicate;

        return ExpressionUtils.and(predicate, post.title.contains(search));
    }

    public static Predicate equalIdAndTypeAndStatus(Long id, PostType type, Post.Status status) {
        return ExpressionUtils.allOf(post.id.eq(id), post.type.eq(type), post.status.eq(status));
    }
}
