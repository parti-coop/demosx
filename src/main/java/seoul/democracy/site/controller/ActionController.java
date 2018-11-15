package seoul.democracy.site.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import seoul.democracy.action.service.ActionService;
import seoul.democracy.issue.dto.CategoryDto;
import seoul.democracy.issue.service.CategoryService;

import java.util.List;

import static org.springframework.data.domain.Sort.Direction.DESC;
import static seoul.democracy.issue.dto.CategoryDto.projectionForFilter;
import static seoul.democracy.issue.predicate.CategoryPredicate.enabled;

@Controller
public class ActionController {

    private final ActionService actionService;
    private final CategoryService categoryService;

    @Autowired
    public ActionController(ActionService actionService,
                            CategoryService categoryService) {
        this.actionService = actionService;
        this.categoryService = categoryService;
    }

    @RequestMapping(value = "/action-list.do", method = RequestMethod.GET)
    public String actionList(@RequestParam(value = "category", required = false) String category,
                             @RequestParam(value = "search", required = false) String search,
                             @PageableDefault(sort = "createdDate", direction = DESC) Pageable pageable,
                             Model model) {


        List<CategoryDto> categories = categoryService.getCategories(enabled(), projectionForFilter);
        model.addAttribute("categories", categories);

        return "/site/action/list";
    }

    @RequestMapping(value = "/action.do", method = RequestMethod.GET)
    public String proposal(@RequestParam("id") Long id,
                           Model model) {

        return "/site/proposal/detail";
    }
}
