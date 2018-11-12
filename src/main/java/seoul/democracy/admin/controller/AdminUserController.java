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
@RequestMapping("/admin/users")
public class AdminUserController {

    private final CategoryService categoryService;

    @Autowired
    public AdminUserController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @RequestMapping(value = "/list.do", method = RequestMethod.GET)
    public String userList() {
        return "/admin/user/list";
    }

    @RequestMapping(value = "/manager.do", method = RequestMethod.GET)
    public String managerList(Model model) {

        List<CategoryDto> categories = categoryService.getCategories(enabled(), projectionForFilter);
        model.addAttribute("categories", categories);

        return "/admin/user/manager-list";
    }
}
