package seoul.democracy.site.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import seoul.democracy.action.dto.ActionDto;
import seoul.democracy.action.service.ActionService;
import seoul.democracy.common.exception.NotFoundException;
import seoul.democracy.issue.dto.CategoryDto;
import seoul.democracy.issue.dto.IssueDto;
import seoul.democracy.issue.service.CategoryService;
import seoul.democracy.issue.service.IssueService;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.function.Function.identity;
import static org.springframework.data.domain.Sort.Direction.DESC;
import static seoul.democracy.action.dto.ActionDto.projectionForSiteDetail;
import static seoul.democracy.action.dto.ActionDto.projectionForSiteList;
import static seoul.democracy.action.predicate.ActionPredicate.equalIdAndStatus;
import static seoul.democracy.action.predicate.ActionPredicate.predicateForSiteList;
import static seoul.democracy.issue.domain.Issue.Status.OPEN;
import static seoul.democracy.issue.dto.CategoryDto.projectionForFilter;
import static seoul.democracy.issue.dto.IssueDto.projectionForRelation;
import static seoul.democracy.issue.predicate.CategoryPredicate.enabled;
import static seoul.democracy.issue.predicate.IssuePredicate.equalIdIn;

@Controller
public class ActionController {

    private final ActionService actionService;
    private final IssueService issueService;
    private final CategoryService categoryService;

    @Autowired
    public ActionController(ActionService actionService,
                            IssueService issueService,
                            CategoryService categoryService) {
        this.actionService = actionService;
        this.issueService = issueService;
        this.categoryService = categoryService;
    }

    @RequestMapping(value = "/action-list.do", method = RequestMethod.GET)
    public String actionList(@RequestParam(value = "category", required = false) String category,
                             @PageableDefault(size = 4, sort = "createdDate", direction = DESC) Pageable pageable,
                             Model model) {

        Page<ActionDto> actions = actionService.getActions(predicateForSiteList(category), pageable, projectionForSiteList);
        model.addAttribute("page", actions);

        List<CategoryDto> categories = categoryService.getCategories(enabled(), projectionForFilter);
        model.addAttribute("categories", categories);
        model.addAttribute("category", category);

        return "/site/action/list";
    }

    @RequestMapping(value = "/action.do", method = RequestMethod.GET)
    public String proposal(@RequestParam("id") Long id,
                           Model model) {

        ActionDto actionDto = actionService.getAction(equalIdAndStatus(id, OPEN), projectionForSiteDetail, true, true);
        if (actionDto == null) throw new NotFoundException("해당 내용을 찾을 수 없습니다.");

        if (!CollectionUtils.isEmpty(actionDto.getRelations())) {
            List<IssueDto> issues = issueService.getIssues(equalIdIn(actionDto.getRelations()), projectionForRelation);
            actionDto.setIssueMap(issues.stream().collect(Collectors.toMap(IssueDto::getId, identity())));
        }
        model.addAttribute("action", actionDto);

        issueService.increaseViewCount(actionDto.getStatsId());

        return "/site/action/detail";
    }
}
