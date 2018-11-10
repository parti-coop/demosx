package seoul.democracy.admin.controller;

import com.mysema.query.types.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import seoul.democracy.debate.domain.Debate;
import seoul.democracy.debate.dto.DebateCreateDto;
import seoul.democracy.debate.dto.DebateDto;
import seoul.democracy.debate.dto.DebateUpdateDto;
import seoul.democracy.debate.service.DebateService;
import seoul.democracy.issue.domain.IssueGroup;
import seoul.democracy.issue.dto.CategoryDto;
import seoul.democracy.issue.dto.IssueDto;
import seoul.democracy.issue.service.CategoryService;
import seoul.democracy.issue.service.IssueService;

import javax.validation.Valid;
import java.util.List;

import static seoul.democracy.debate.dto.DebateDto.projection;
import static seoul.democracy.debate.predicate.DebatePredicate.equalIdAndGroup;
import static seoul.democracy.issue.dto.CategoryDto.projectionForFilter;
import static seoul.democracy.issue.dto.IssueDto.projectionForBasic;
import static seoul.democracy.issue.predicate.CategoryPredicate.enabled;
import static seoul.democracy.issue.predicate.IssuePredicate.equalIdIn;

@Controller
@RequestMapping("/admin/issue")
public class AdminDebateController {

    private final CategoryService categoryService;
    private final DebateService debateService;
    private final IssueService issueService;

    @Autowired
    public AdminDebateController(CategoryService categoryService,
                                 DebateService debateService,
                                 IssueService issueService) {
        this.categoryService = categoryService;
        this.debateService = debateService;
        this.issueService = issueService;
    }

    private String getDebateDetail(Predicate predicate, Model model) {
        DebateDto debateDto = debateService.getDebate(predicate, projection, true, true);
        if (!CollectionUtils.isEmpty(debateDto.getRelations())) {
            List<IssueDto> issues = issueService.getIssues(equalIdIn(debateDto.getRelations()), projectionForBasic);
            debateDto.setIssues(issues);
        }
        model.addAttribute("debate", debateDto);

        return "/admin/debate/detail";
    }

    private String createDebate(IssueGroup group, String groupPrefix,
                                DebateCreateDto createDto,
                                BindingResult result) {
        if (result.hasErrors()) {
            if (!CollectionUtils.isEmpty(createDto.getRelations())) {
                List<IssueDto> issues = issueService.getIssues(equalIdIn(createDto.getRelations()), projectionForBasic);
                createDto.setIssues(issues);
            }
            return "/admin/debate/create";
        }

        Debate debate = debateService.create(group, createDto);

        return "redirect:/admin/issue/" + groupPrefix + "debate-detail.do?id=" + debate.getId();
    }

    private String getUpdateDebate(Predicate predicate, Model model) {
        DebateDto debateDto = debateService.getDebate(predicate, projection, true, true);
        if (!CollectionUtils.isEmpty(debateDto.getRelations())) {
            List<IssueDto> issues = issueService.getIssues(equalIdIn(debateDto.getRelations()), projectionForBasic);
            debateDto.setIssues(issues);
        }

        DebateUpdateDto updateDto = DebateUpdateDto.of(debateDto);
        model.addAttribute("updateDto", updateDto);

        return "/admin/debate/update";
    }

    private String updateDebate(String groupPrefix,
                                DebateUpdateDto updateDto,
                                BindingResult result) {
        if (result.hasErrors()) {
            if (!CollectionUtils.isEmpty(updateDto.getRelations())) {
                List<IssueDto> issues = issueService.getIssues(equalIdIn(updateDto.getRelations()), projectionForBasic);
                updateDto.setIssues(issues);
            }
            return "/admin/debate/update";
        }

        Debate debate = debateService.update(updateDto);

        return "redirect:/admin/issue/" + groupPrefix + "debate-detail.do?id=" + debate.getId();
    }

    @ModelAttribute("categories")
    public List<CategoryDto> getCategories() {
        return categoryService.getCategories(enabled(), projectionForFilter);
    }

    /**
     * 관리자 > 시민제안 > 토론관리
     */
    private void userGroupAttribute(Model model) {
        model.addAttribute("issueGroup", IssueGroup.USER);
        model.addAttribute("groupText", "토론");
    }

    @RequestMapping(value = "/debate.do", method = RequestMethod.GET)
    public String debateList(Model model) {
        userGroupAttribute(model);
        return "/admin/debate/list";
    }

    /**
     * 관리자 > 시민제안 > 토론관리 > 상세
     */
    @RequestMapping(value = "/debate-detail.do", method = RequestMethod.GET)
    public String debateDetail(@RequestParam("id") Long id,
                               Model model) {
        userGroupAttribute(model);
        return getDebateDetail(equalIdAndGroup(id, IssueGroup.USER), model);
    }

    @RequestMapping(value = "/debate-new.do", method = RequestMethod.GET)
    public String debateNew(@ModelAttribute("createDto") DebateCreateDto createDto,
                            Model model) {
        userGroupAttribute(model);
        return "/admin/debate/create";
    }

    @RequestMapping(value = "/debate-new.do", method = RequestMethod.POST)
    public String debateNew(@ModelAttribute("createDto") @Valid DebateCreateDto createDto,
                            BindingResult result,
                            Model model) {
        userGroupAttribute(model);
        return createDebate(IssueGroup.USER, "", createDto, result);
    }

    @RequestMapping(value = "/debate-edit.do", method = RequestMethod.GET)
    public String debateEdit(@RequestParam("id") Long id,
                             Model model) {
        userGroupAttribute(model);
        return getUpdateDebate(equalIdAndGroup(id, IssueGroup.USER), model);
    }

    @RequestMapping(value = "/debate-edit.do", method = RequestMethod.POST)
    public String debateEdit(@RequestParam("id") Long id,
                             @ModelAttribute("updateDto") @Valid DebateUpdateDto updateDto,
                             BindingResult result,
                             Model model) {
        userGroupAttribute(model);
        return updateDebate("", updateDto, result);
    }

    /**
     * 관리자 > 기관제안 > 기관제안관리
     */
    private void orgGroupAttribute(Model model) {
        model.addAttribute("issueGroup", IssueGroup.ORG);
        model.addAttribute("groupText", "기관제안");
        model.addAttribute("groupPrefix", "org-");
    }

    @RequestMapping(value = "/org-debate.do", method = RequestMethod.GET)
    public String orgDebateList(Model model) {
        orgGroupAttribute(model);
        return "/admin/debate/list";
    }

    /**
     * 관리자 > 기관제안 > 기관제안관리 > 상세
     */
    @RequestMapping(value = "/org-debate-detail.do", method = RequestMethod.GET)
    public String orgDebateDetail(@RequestParam("id") Long id,
                                  Model model) {
        orgGroupAttribute(model);
        return getDebateDetail(equalIdAndGroup(id, IssueGroup.ORG), model);
    }

    @RequestMapping(value = "/org-debate-new.do", method = RequestMethod.GET)
    public String orgDebateNew(@ModelAttribute("createDto") DebateCreateDto createDto,
                               Model model) {
        orgGroupAttribute(model);
        return "/admin/debate/create";
    }

    @RequestMapping(value = "/org-debate-new.do", method = RequestMethod.POST)
    public String orgDebateNew(@ModelAttribute("createDto") @Valid DebateCreateDto createDto,
                               BindingResult result,
                               Model model) {
        orgGroupAttribute(model);
        return createDebate(IssueGroup.ORG, "org-", createDto, result);
    }

    @RequestMapping(value = "/org-debate-edit.do", method = RequestMethod.GET)
    public String orgDebateEdit(@RequestParam("id") Long id,
                                Model model) {
        orgGroupAttribute(model);
        return getUpdateDebate(equalIdAndGroup(id, IssueGroup.ORG), model);
    }

    @RequestMapping(value = "/org-debate-edit.do", method = RequestMethod.POST)
    public String orgDebateEdit(@RequestParam("id") Long id,
                                @ModelAttribute("updateDto") @Valid DebateUpdateDto updateDto,
                                BindingResult result,
                                Model model) {
        orgGroupAttribute(model);
        return updateDebate("org-", updateDto, result);
    }
}
