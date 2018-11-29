package seoul.democracy.admin.restcontroller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import seoul.democracy.stats.domain.StatsIssue;
import seoul.democracy.stats.domain.StatsOpinion;
import seoul.democracy.stats.service.StatsService;

@RestController
@RequestMapping("/admin/ajax/stats")
public class AdminStatsAjaxController {

    private final StatsService statsService;

    @Autowired
    public AdminStatsAjaxController(StatsService statsService) {
        this.statsService = statsService;
    }

    @RequestMapping(value = "/issues", method = RequestMethod.GET)
    public Page<StatsIssue> getStatsIssues(@PageableDefault Pageable pageable) {
        return statsService.getStatsIssues(pageable);
    }

    @RequestMapping(value = "/opinions", method = RequestMethod.GET)
    public Page<StatsOpinion> getStatsOpinions(@PageableDefault Pageable pageable) {
        return statsService.getStatsOpinions(pageable);
    }
}
