package seoul.democracy.admin.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import seoul.democracy.action.dto.ActionCreateDto;
import seoul.democracy.action.dto.ActionDto;
import seoul.democracy.action.predicate.ActionPredicate;
import seoul.democracy.action.service.ActionService;
import seoul.democracy.issue.dto.CategoryDto;
import seoul.democracy.issue.service.CategoryService;

import java.util.List;

import static seoul.democracy.issue.dto.CategoryDto.projectionForFilter;
import static seoul.democracy.issue.predicate.CategoryPredicate.enabled;

@Controller
@RequestMapping("/admin/issue")
public class AdminActionController {

    private final CategoryService categoryService;
    private final ActionService actionService;

    @Autowired
    public AdminActionController(CategoryService categoryService,
                                 ActionService actionService) {
        this.categoryService = categoryService;
        this.actionService = actionService;
    }

    @ModelAttribute("categories")
    public List<CategoryDto> getCategories() {
        return categoryService.getCategories(enabled(), projectionForFilter);
    }

    /**
     * 관리자 > 시민제안 > 실행관리
     */
    @RequestMapping(value = "/action.do", method = RequestMethod.GET)
    public String actionList() {
        return "/admin/action/list";
    }

    /**
     * 관리자 > 시민제안 > 실행관리 > 상세
     */
    @RequestMapping(value = "/action-detail.do", method = RequestMethod.GET)
    public String actionDetail(@RequestParam("id") Long id,
                               Model model) {
        ActionDto actionDto = actionService.getAction(ActionPredicate.equalId(id), ActionDto.projectionForAdminDetail, true, true);

        return "/admin/action/detail";
    }

    /**
     * 관리자 > 시민제안 > 실행관리 > 생성
     */
    @RequestMapping(value = "/action-new.do", method = RequestMethod.GET)
    public String actionNew(@ModelAttribute("createDto") ActionCreateDto createDto) {
        return "/admin/action/create";
    }

    /**
     * 관리자 > 시민제안 > 실행관리 > 수정
     */
    @RequestMapping(value = "/action-edot.do", method = RequestMethod.GET)
    public String actionEdit() {
        return "/admin/action/update";
    }
}
