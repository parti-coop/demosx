package seoul.democracy.admin.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import seoul.democracy.issue.dto.CategoryDto;
import seoul.democracy.issue.service.CategoryService;

import java.util.List;

import static seoul.democracy.issue.dto.CategoryDto.projectionForFilter;
import static seoul.democracy.issue.predicate.CategoryPredicate.enabled;

@Controller
@RequestMapping("/admin/issue")
public class AdminProposalController {

    private final CategoryService categoryService;

    @Autowired
    public AdminProposalController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    /**
     * 관리자 > 시민제안 > 제안관리
     */
    @RequestMapping(value = "/proposal.do", method = RequestMethod.GET)
    public String proposal(Model model) {

        List<CategoryDto> categories = categoryService.getCategories(enabled(), projectionForFilter);
        model.addAttribute("categories", categories);

        return "/admin/proposal/list";
    }
}
