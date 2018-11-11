package seoul.democracy.post.service;

import com.mysema.query.types.Expression;
import com.mysema.query.types.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import seoul.democracy.common.exception.NotFoundException;
import seoul.democracy.post.domain.Post;
import seoul.democracy.post.domain.PostType;
import seoul.democracy.post.dto.PostCreateDto;
import seoul.democracy.post.dto.PostDto;
import seoul.democracy.post.dto.PostUpdateDto;
import seoul.democracy.post.repository.PostRepository;

@Service
@Transactional(readOnly = true)
public class PostService {

    private final PostRepository postRepository;

    @Autowired
    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public Page<PostDto> getPosts(Predicate predicate, Pageable pageable, Expression<PostDto> projection) {
        return postRepository.findAll(predicate, pageable, projection);
    }

    public PostDto getPost(Predicate predicate, Expression<PostDto> projection) {
        return postRepository.findOne(predicate, projection);
    }

    /**
     * 글 등록
     */
    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public Post create(PostType type, PostCreateDto createDto) {
        Post post = Post.create(type, createDto);

        return postRepository.save(post);
    }

    /**
     * 글 수정
     */
    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public Post update(PostUpdateDto updateDto) {
        Post post = postRepository.findOne(updateDto.getId());
        if (post == null) throw new NotFoundException("해당 글을 찾을 수 없습니다.");

        return post.update(updateDto);
    }
}
