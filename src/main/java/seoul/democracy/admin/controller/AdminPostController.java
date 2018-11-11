package seoul.democracy.admin.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import seoul.democracy.post.domain.Post;
import seoul.democracy.post.domain.PostType;
import seoul.democracy.post.dto.PostCreateDto;
import seoul.democracy.post.dto.PostDto;
import seoul.democracy.post.dto.PostUpdateDto;
import seoul.democracy.post.predicate.PostPredicate;
import seoul.democracy.post.service.PostService;

import javax.validation.Valid;

@Controller
@RequestMapping("/admin/post")
public class AdminPostController {

    private final PostService postService;

    @Autowired
    public AdminPostController(PostService postService) {
        this.postService = postService;
    }

    /**
     * 관리자 > 게시판 > 공지사항 관리
     */
    @RequestMapping(value = "/notice.do", method = RequestMethod.GET)
    public String noticeList() {
        return "/admin/post/list";
    }

    /**
     * 관리자 > 게시판 > 공지사항 상세
     */
    @RequestMapping(value = "/notice-detail.do", method = RequestMethod.GET)
    public String noticeDetail(@RequestParam("id") Long id,
                               Model model) {
        PostDto postDto = postService.getPost(PostPredicate.equalId(id), PostDto.projectionForAdminDetail);

        model.addAttribute("post", postDto);

        return "/admin/post/detail";
    }

    /**
     * 관리자 > 게시판 > 공지사항 생성
     */
    @RequestMapping(value = "/notice-new.do", method = RequestMethod.GET)
    public String noticeNew(@ModelAttribute("createDto") PostCreateDto createDto) {
        return "/admin/post/create";
    }

    @RequestMapping(value = "/notice-new.do", method = RequestMethod.POST)
    public String noticeNew(@ModelAttribute("createDto") @Valid PostCreateDto createDto,
                            BindingResult result) {
        if (result.hasErrors()) {
            return "/admin/post/create";
        }

        Post post = postService.create(PostType.NOTICE, createDto);

        return "redirect:/admin/post/notice-detail.do?id=" + post.getId();
    }

    /**
     * 관리자 > 게시판 > 공지사항 수정
     */
    @RequestMapping(value = "/notice-edit.do", method = RequestMethod.GET)
    public String noticeEdit(@RequestParam("id") Long id,
                             Model model) {
        PostDto postDto = postService.getPost(PostPredicate.equalId(id), PostDto.projectionForAdminDetail);

        PostUpdateDto updateDto = PostUpdateDto.of(postDto);
        model.addAttribute("updateDto", updateDto);

        return "/admin/post/update";
    }

    @RequestMapping(value = "/notice-edit.do", method = RequestMethod.POST)
    public String noticeEdit(@RequestParam("id") Long id,
                             @ModelAttribute("updateDto") @Valid PostUpdateDto updateDto,
                             BindingResult result) {
        if (result.hasErrors()) {
            return "/admin/post/update";
        }

        Post post = postService.update(updateDto);

        return "redirect:/admin/post/notice-detail.do?id=" + post.getId();
    }

}
