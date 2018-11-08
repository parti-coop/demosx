package seoul.democracy.admin.restcontroller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import seoul.democracy.common.dto.ResultInfo;
import seoul.democracy.opinion.dto.OpinionDto;
import seoul.democracy.opinion.service.OpinionService;

import java.net.InetAddress;

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

    @RequestMapping(value = "/{opinionId}/block", method = RequestMethod.PATCH)
    public ResultInfo blockOpinion(@PathVariable("opinionId") Long opinionId,
                                   InetAddress address) {

        opinionService.blockOpinion(opinionId, address.getHostAddress());

        return ResultInfo.of("비공개 상태입니다.");
    }

    /*@RequestMapping(value = "/{opinionId}/open", method = RequestMethod.PATCH)
    public ResultInfo openOpinion(@PathVariable("opinionId") Long opinionId,
                                   InetAddress address) {

        opinionService.openOpinion(opinionId, address.getHostAddress());

        return ResultInfo.of("비공개 상태입니다.");
    }*/
}
