package seoul.democracy.admin.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import seoul.democracy.action.domain.Action;
import seoul.democracy.action.dto.ActionCreateDto;
import seoul.democracy.action.dto.ActionDto;
import seoul.democracy.action.dto.ActionUpdateDto;
import seoul.democracy.action.service.ActionService;
import seoul.democracy.issue.dto.CategoryDto;
import seoul.democracy.issue.dto.IssueDto;
import seoul.democracy.issue.service.CategoryService;
import seoul.democracy.issue.service.IssueService;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.function.Function.identity;
import static seoul.democracy.action.dto.ActionDto.projectionForAdminDetail;
import static seoul.democracy.action.predicate.ActionPredicate.equalId;
import static seoul.democracy.issue.dto.CategoryDto.projectionForFilter;
import static seoul.democracy.issue.dto.IssueDto.projectionForRelation;
import static seoul.democracy.issue.predicate.CategoryPredicate.enabled;
import static seoul.democracy.issue.predicate.IssuePredicate.equalIdIn;

@Controller
@RequestMapping("/admin/issue")
public class AdminActionController {

    private final CategoryService categoryService;
    private final ActionService actionService;
    private final IssueService issueService;

    @Autowired
    public AdminActionController(CategoryService categoryService,
                                 ActionService actionService,
                                 IssueService issueService) {
        this.categoryService = categoryService;
        this.actionService = actionService;
        this.issueService = issueService;
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
        ActionDto actionDto = actionService.getAction(equalId(id), projectionForAdminDetail, true, true);
        if (!CollectionUtils.isEmpty(actionDto.getRelations())) {
            List<IssueDto> issues = issueService.getIssues(equalIdIn(actionDto.getRelations()), projectionForRelation);
            actionDto.setIssueMap(issues.stream().collect(Collectors.toMap(IssueDto::getId, identity())));
        }
        model.addAttribute("action", actionDto);

        return "/admin/action/detail";
    }

    /**
     * 관리자 > 시민제안 > 실행관리 > 생성
     */
    @RequestMapping(value = "/action-new.do", method = RequestMethod.GET)
    public String actionNew(@ModelAttribute("createDto") ActionCreateDto createDto) {
        return "/admin/action/create";
    }

    @RequestMapping(value = "/action-new.do", method = RequestMethod.POST)
    public String actionNew(@ModelAttribute("createDto") @Valid ActionCreateDto createDto,
                            BindingResult result) {
        if (result.hasErrors()) {
            if (!CollectionUtils.isEmpty(createDto.getRelations())) {
                List<IssueDto> issues = issueService.getIssues(equalIdIn(createDto.getRelations()), projectionForRelation);
                createDto.setIssueMap(issues.stream().collect(Collectors.toMap(IssueDto::getId, identity())));
            }
            return "/admin/action/create";
        }

        Action action = actionService.create(createDto);

        return "redirect:/admin/issue/action-detail.do?id=" + action.getId();
    }

    /**
     * 관리자 > 시민제안 > 실행관리 > 수정
     */
    @RequestMapping(value = "/action-edit.do", method = RequestMethod.GET)
    public String actionEdit(@RequestParam("id") Long id,
                             Model model) {
        ActionDto actionDto = actionService.getAction(equalId(id), projectionForAdminDetail, true, true);
        if (!CollectionUtils.isEmpty(actionDto.getRelations())) {
            List<IssueDto> issues = issueService.getIssues(equalIdIn(actionDto.getRelations()), projectionForRelation);
            actionDto.setIssueMap(issues.stream().collect(Collectors.toMap(IssueDto::getId, identity())));
        }

        ActionUpdateDto updateDto = ActionUpdateDto.of(actionDto);
        model.addAttribute("updateDto", updateDto);

        return "/admin/action/update";
    }

    @RequestMapping(value = "/action-edit.do", method = RequestMethod.POST)
    public String actionEdit(@RequestParam("id") Long id,
                             @ModelAttribute("updateDto") @Valid ActionUpdateDto updateDto,
                             BindingResult result) {
        if (result.hasErrors()) {
            if (!CollectionUtils.isEmpty(updateDto.getRelations())) {
                List<IssueDto> issues = issueService.getIssues(equalIdIn(updateDto.getRelations()), projectionForRelation);
                updateDto.setIssueMap(issues.stream().collect(Collectors.toMap(IssueDto::getId, identity())));
            }
            return "/admin/action/update";
        }

        Action action = actionService.update(updateDto);

        return "redirect:/admin/issue/action-detail.do?id=" + action.getId();
    }
}
