package seoul.democracy.site.controller;

import com.mysema.query.types.Predicate;
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
import seoul.democracy.debate.domain.Debate;
import seoul.democracy.debate.dto.DebateDto;
import seoul.democracy.debate.service.DebateService;
import seoul.democracy.history.dto.IssueHistoryDto;
import seoul.democracy.history.service.IssueHistoryService;
import seoul.democracy.issue.domain.IssueGroup;
import seoul.democracy.issue.dto.CategoryDto;
import seoul.democracy.issue.dto.IssueDto;
import seoul.democracy.issue.service.CategoryService;
import seoul.democracy.issue.service.IssueService;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.function.Function.identity;
import static org.springframework.data.domain.Sort.Direction.DESC;
import static seoul.democracy.debate.dto.DebateDto.projectionForSiteDetail;
import static seoul.democracy.debate.dto.DebateDto.projectionForSiteList;
import static seoul.democracy.debate.predicate.DebatePredicate.equalIdAndStatus;
import static seoul.democracy.debate.predicate.DebatePredicate.predicateForSiteList;
import static seoul.democracy.history.predicate.IssueHistoryPredicate.predicateForSite;
import static seoul.democracy.issue.domain.Issue.Status.OPEN;
import static seoul.democracy.issue.domain.IssueGroup.ORG;
import static seoul.democracy.issue.domain.IssueGroup.USER;
import static seoul.democracy.issue.dto.CategoryDto.projectionForFilter;
import static seoul.democracy.issue.dto.IssueDto.projectionForRelation;
import static seoul.democracy.issue.predicate.CategoryPredicate.enabled;
import static seoul.democracy.issue.predicate.IssuePredicate.equalIdIn;

@Controller
public class DebateController {

    private final DebateService debateService;
    private final IssueService issueService;
    private final IssueHistoryService issueHistoryService;
    private final CategoryService categoryService;

    @Autowired
    public DebateController(DebateService debateService,
                            IssueService issueService,
                            IssueHistoryService issueHistoryService,
                            CategoryService categoryService) {
        this.debateService = debateService;
        this.issueService = issueService;
        this.issueHistoryService = issueHistoryService;
        this.categoryService = categoryService;
    }

    private String getDebateList(IssueGroup group,
                                 Debate.Process process,
                                 String category,
                                 String search,
                                 Pageable pageable,
                                 Model model) {
        model.addAttribute("group", group);

        Predicate predicate = predicateForSiteList(group, process, category, search);
        Page<DebateDto> page = debateService.getDebates(predicate, pageable, projectionForSiteList);
        model.addAttribute("page", page);

        List<CategoryDto> categories = categoryService.getCategories(enabled(), projectionForFilter);
        model.addAttribute("categories", categories);
        model.addAttribute("process", process);
        model.addAttribute("category", category);
        model.addAttribute("search", search);

        return "/site/debate/list";
    }

    @RequestMapping(value = "/debate-list.do", method = RequestMethod.GET)
    public String debateList(@RequestParam(value = "process", required = false) Debate.Process process,
                             @RequestParam(value = "category", required = false) String category,
                             @RequestParam(value = "search", required = false) String search,
                             @PageableDefault(sort = "createdDate", direction = DESC) Pageable pageable,
                             Model model) {

        return getDebateList(USER, process, category, search, pageable, model);
    }

    @RequestMapping(value = "/org-debate-list.do", method = RequestMethod.GET)
    public String orgDebateList(@RequestParam(value = "process", required = false) Debate.Process process,
                                @RequestParam(value = "category", required = false) String category,
                                @RequestParam(value = "search", required = false) String search,
                                @PageableDefault(sort = "createdDate", direction = DESC) Pageable pageable,
                                Model model) {

        return getDebateList(ORG, process, category, search, pageable, model);
    }

    @RequestMapping(value = "/debate.do", method = RequestMethod.GET)
    public String debate(@RequestParam("id") Long id,
                         Model model) {

        Predicate predicate = equalIdAndStatus(id, OPEN);
        DebateDto debateDto = debateService.getDebate(predicate, projectionForSiteDetail, true, true);
        if (!CollectionUtils.isEmpty(debateDto.getRelations())) {
            List<IssueDto> issues = issueService.getIssues(equalIdIn(debateDto.getRelations()), projectionForRelation);
            debateDto.setIssueMap(issues.stream().collect(Collectors.toMap(IssueDto::getId, identity())));
        }

        model.addAttribute("debate", debateDto);

        return debateDto.getOpinionType().isProposal() ? "/site/debate/detail-proposal" : "site/debate/detail-debate";
    }

    @RequestMapping(value = "/debate-history.do", method = RequestMethod.GET)
    public String debateHistory(@RequestParam("id") Long id,
                                Model model) {

        Predicate predicate = equalIdAndStatus(id, OPEN);
        DebateDto debateDto = debateService.getDebate(predicate, projectionForSiteDetail, false, false);
        model.addAttribute("debate", debateDto);

        List<IssueHistoryDto> histories = issueHistoryService.getHistories(predicateForSite(debateDto.getId()), IssueHistoryDto.projectionForSite);
        model.addAttribute("histories", histories);

        return debateDto.getOpinionType().isProposal() ? "/site/debate/detail-proposal-history" : "site/debate/detail-debate-history";
    }
}
