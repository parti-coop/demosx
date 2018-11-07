package seoul.democracy.admin.restcontroller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import seoul.democracy.opinion.dto.OpinionDto;
import seoul.democracy.opinion.service.OpinionService;

import static seoul.democracy.opinion.predicate.OpinionPredicate.equalIssueId;


@RestController
@RequestMapping("/admin/ajax/opinions")
public class AdminOpinionAjaxController {

    private final OpinionService opinionService;

    @Autowired
    public AdminOpinionAjaxController(OpinionService opinionService) {
        this.opinionService = opinionService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public Page<OpinionDto> getOpinions(@RequestParam("issueId") Long issueId,
                                        @PageableDefault Pageable pageable) {

        return opinionService.getOpinions(equalIssueId(issueId), pageable, OpinionDto.projection);
    }
}
