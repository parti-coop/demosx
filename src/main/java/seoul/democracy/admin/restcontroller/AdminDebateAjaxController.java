package seoul.democracy.admin.restcontroller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import seoul.democracy.debate.dto.DebateDto;
import seoul.democracy.debate.service.DebateService;

import static seoul.democracy.debate.dto.DebateDto.projectionForAdminList;
import static seoul.democracy.debate.predicate.DebatePredicate.containsTitleOrCreatedByNameAndEqualCategory;

@RestController
@RequestMapping("/admin/ajax/issue/debates")
public class AdminDebateAjaxController {

    private final DebateService debateService;

    @Autowired
    public AdminDebateAjaxController(DebateService debateService) {
        this.debateService = debateService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public Page<DebateDto> getProposals(@RequestParam(value = "search") String search,
                                        @RequestParam(value = "category", required = false) String category,
                                        @PageableDefault Pageable pageable) {
        return debateService.getDebates(containsTitleOrCreatedByNameAndEqualCategory(search, category), pageable,
            projectionForAdminList, false, false);
    }

}
