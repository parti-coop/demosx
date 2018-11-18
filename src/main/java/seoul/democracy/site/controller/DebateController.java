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
import seoul.democracy.debate.domain.Debate;
import seoul.democracy.debate.dto.DebateDto;
import seoul.democracy.debate.service.DebateService;
import seoul.democracy.issue.dto.CategoryDto;
import seoul.democracy.issue.service.CategoryService;

import java.util.List;

import static org.springframework.data.domain.Sort.Direction.DESC;
import static seoul.democracy.debate.dto.DebateDto.projectionForSiteList;
import static seoul.democracy.debate.predicate.DebatePredicate.equalIdAndGroupAndStatus;
import static seoul.democracy.debate.predicate.DebatePredicate.predicateForSiteList;
import static seoul.democracy.issue.domain.Issue.Status.OPEN;
import static seoul.democracy.issue.domain.IssueGroup.USER;
import static seoul.democracy.issue.dto.CategoryDto.projectionForFilter;
import static seoul.democracy.issue.predicate.CategoryPredicate.enabled;

@Controller
public class DebateController {

    private final DebateService debateService;
    private final CategoryService categoryService;

    @Autowired
    public DebateController(DebateService debateService,
                            CategoryService categoryService) {
        this.debateService = debateService;
        this.categoryService = categoryService;
    }

    @RequestMapping(value = "/debate-list.do", method = RequestMethod.GET)
    public String debateList(@RequestParam(value = "process", required = false) Debate.Process process,
                             @RequestParam(value = "category", required = false) String category,
                             @RequestParam(value = "search", required = false) String search,
                             @PageableDefault(sort = "createdDate", direction = DESC) Pageable pageable,
                             Model model) {

        Page<DebateDto> page = debateService.getDebates(predicateForSiteList(process, category, search), pageable, projectionForSiteList);
        model.addAttribute("page", page);

        List<CategoryDto> categories = categoryService.getCategories(enabled(), projectionForFilter);
        model.addAttribute("categories", categories);
        model.addAttribute("process", process);
        model.addAttribute("category", category);
        model.addAttribute("search", search);

        return "/site/debate/list";
    }

    @RequestMapping(value = "/debate.do", method = RequestMethod.GET)
    public String proposal(@RequestParam("id") Long id,
                           Model model) {
        DebateDto debateDto = debateService.getDebate(equalIdAndGroupAndStatus(id, USER, OPEN),
            DebateDto.projectionForAdminDetail, true, true);

        model.addAttribute("debate", debateDto);

        return "/site/debate/detail";
    }
}
