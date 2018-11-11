package seoul.democracy.admin.restcontroller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import seoul.democracy.post.domain.PostType;
import seoul.democracy.post.dto.PostDto;
import seoul.democracy.post.service.PostService;

import static seoul.democracy.post.dto.PostDto.projectionForAdminList;
import static seoul.democracy.post.predicate.PostPredicate.equalType;

@RestController
@RequestMapping("/admin/ajax/posts")
public class AdminPostAjaxController {

    private final PostService postService;

    @Autowired
    public AdminPostAjaxController(PostService postService) {
        this.postService = postService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public Page<PostDto> getPosts(@RequestParam("type") PostType type,
                                  @PageableDefault Pageable pageable) {
        return postService.getPosts(equalType(type), pageable, projectionForAdminList);
    }
}
