package seoul.democracy.site.restcontroller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import seoul.democracy.common.dto.ResultInfo;
import seoul.democracy.opinion.dto.OpinionCreateDto;
import seoul.democracy.opinion.dto.OpinionDto;
import seoul.democracy.opinion.dto.OpinionUpdateDto;
import seoul.democracy.opinion.service.OpinionService;
import seoul.democracy.user.utils.UserUtils;

import javax.validation.Valid;

import static seoul.democracy.opinion.domain.Opinion.Status.OPEN;
import static seoul.democracy.opinion.dto.OpinionDto.projectionForIssueDetail;
import static seoul.democracy.opinion.dto.OpinionDto.projectionForMyOpinion;
import static seoul.democracy.opinion.predicate.OpinionPredicate.equalIssueIdAndCreatedByIdAndStatus;
import static seoul.democracy.opinion.predicate.OpinionPredicate.predicateForOpinionList;

@RestController
public class OpinionAjaxController {

    private final OpinionService opinionService;

    @Autowired
    public OpinionAjaxController(OpinionService opinionService) {
        this.opinionService = opinionService;
    }

    @RequestMapping(value = "/ajax/opinions", method = RequestMethod.GET)
    public Page<OpinionDto> getOpinions(@RequestParam("issueId") Long issueId,
                                        @PageableDefault Pageable pageable) {
        return opinionService.getOpinionsWithLiked(predicateForOpinionList(issueId), pageable, projectionForIssueDetail);
    }

    @RequestMapping(value = "/ajax/mypage/opinions", method = RequestMethod.POST)
    public ResultInfo createOpinion(@RequestBody @Valid OpinionCreateDto createDto) {
        opinionService.createOpinion(createDto);

        return ResultInfo.of("의견을 등록하였습니다.");
    }

    @RequestMapping(value = "/ajax/mypage/opinions/{id}", method = RequestMethod.PUT)
    public ResultInfo updateOpinion(@RequestBody @Valid OpinionUpdateDto updateDto) {
        opinionService.updateOpinion(updateDto);

        return ResultInfo.of("의견을 수정하였습니다.");
    }

    @RequestMapping(value = "/ajax/mypage/opinions/{id}", method = RequestMethod.DELETE)
    public ResultInfo deleteOpinion(@PathVariable("id") Long id) {
        opinionService.deleteOpinion(id);

        return ResultInfo.of("의견을 삭제하였습니다.");
    }

    @RequestMapping(value = "/ajax/mypage/opinions/{id}/selectLike", method = RequestMethod.PUT)
    public ResultInfo selectLikeOpinion(@PathVariable("id") Long id) {
        opinionService.selectOpinionLike(id);

        return ResultInfo.of("공감하였습니다.");
    }

    @RequestMapping(value = "/ajax/mypage/opinions/{id}/deselectLike", method = RequestMethod.PUT)
    public ResultInfo deselectLikeOpinion(@PathVariable("id") Long id) {
        opinionService.deselectOpinionLike(id);

        return ResultInfo.of("공감해제하였습니다.");
    }

    @RequestMapping(value = "/ajax/mypage/opinions", method = RequestMethod.GET)
    public OpinionDto getDebateOpinionByIssue(@RequestParam("issueId") Long issueId) {
        OpinionDto opinionDto = opinionService.getOpinion(equalIssueIdAndCreatedByIdAndStatus(issueId, UserUtils.getUserId(), OPEN), projectionForMyOpinion);
        if (opinionDto == null) return new OpinionDto();

        return opinionDto;
    }

}
