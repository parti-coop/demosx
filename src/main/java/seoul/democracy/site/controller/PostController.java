package seoul.democracy.site.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import seoul.democracy.common.exception.NotFoundException;
import seoul.democracy.post.dto.PostDto;
import seoul.democracy.post.predicate.PostPredicate;
import seoul.democracy.post.service.PostService;

import static org.springframework.data.domain.Sort.Direction.DESC;
import static seoul.democracy.post.domain.Post.Status.OPEN;
import static seoul.democracy.post.domain.PostType.NOTICE;
import static seoul.democracy.post.dto.PostDto.projectionForSiteList;
import static seoul.democracy.post.predicate.PostPredicate.predicateForSiteList;

@Controller
public class PostController {

    private final PostService postService;

    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

    @RequestMapping(value = "/notice-list.do", method = RequestMethod.GET)
    public String noticeList(@RequestParam(value = "search", required = false) String search,
                             @PageableDefault(sort = "createdDate", direction = DESC) Pageable pageable,
                             Model model) {
        Page<PostDto> posts = postService.getPosts(predicateForSiteList(NOTICE, search), pageable, projectionForSiteList);
        model.addAttribute("posts", posts);

        model.addAttribute("search", search);

        return "/site/post/list";
    }

    @RequestMapping(value = "/notice.do", method = RequestMethod.GET)
    public String noticeDetail(@RequestParam("id") Long id,
                               Model model) {
        PostDto postDto = postService.getPost(PostPredicate.equalIdAndTypeAndStatus(id, NOTICE, OPEN), PostDto.projectionForSiteDetail);
        if (postDto == null) throw new NotFoundException("해당 내용을 찾을 수 없습니다.");

        model.addAttribute("post", postDto);

        postService.increaseViewCount(id);

        return "/site/post/detail";
    }
}
