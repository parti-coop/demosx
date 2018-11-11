package seoul.democracy.post.predicate;

import com.mysema.query.types.Predicate;
import seoul.democracy.post.domain.PostType;

import static seoul.democracy.post.domain.QPost.post;

public class PostPredicate {

    public static Predicate equalId(Long id) {
        return post.id.eq(id);
    }

    public static Predicate equalType(PostType type) {
        return post.type.eq(type);
    }
}
