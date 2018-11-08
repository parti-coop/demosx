package seoul.democracy.admin.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import seoul.democracy.debate.dto.DebateDto;
import seoul.democracy.debate.predicate.DebatePredicate;
import seoul.democracy.debate.service.DebateService;
import seoul.democracy.issue.dto.CategoryDto;
import seoul.democracy.issue.service.CategoryService;

import java.util.List;

import static seoul.democracy.issue.dto.CategoryDto.projectionForFilter;
import static seoul.democracy.issue.predicate.CategoryPredicate.enabled;

@Controller
@RequestMapping("/admin/issue")
public class AdminDebateController {

    private final CategoryService categoryService;
    private final DebateService debateService;

    @Autowired
    public AdminDebateController(CategoryService categoryService,
                                 DebateService debateService) {
        this.categoryService = categoryService;
        this.debateService = debateService;
    }

    @ModelAttribute("categories")
    public List<CategoryDto> getCategories() {
        return categoryService.getCategories(enabled(), projectionForFilter);
    }

    /**
     * 관리자 > 시민제안 > 토론관리
     */
    @RequestMapping(value = "/debate.do", method = RequestMethod.GET)
    public String debateList() {
        return "/admin/debate/list";
    }

    /**
     * 관리자 > 시민제안 > 토론관리 > 상세
     */
    @RequestMapping(value = "/debate-detail.do", method = RequestMethod.GET)
    public String proposalDetail(@RequestParam("id") Long id,
                                 Model model) {

        DebateDto debateDto = debateService.getDebate(DebatePredicate.equalId(id), DebateDto.projection, true, true);
        model.addAttribute("debate", debateDto);

        return "/admin/debate/detail";
    }
}
