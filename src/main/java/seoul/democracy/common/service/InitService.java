package seoul.democracy.common.service;

import org.springframework.beans.factory.annotation.Autowired;
import seoul.democracy.debate.service.DebateService;
import seoul.democracy.stats.service.StatsService;

import javax.annotation.PostConstruct;

public class InitService {

    private final DebateService debateService;
    private final StatsService statsService;

    @Autowired
    public InitService(DebateService debateService,
                       StatsService statsService) {
        this.debateService = debateService;
        this.statsService = statsService;
    }

    @PostConstruct
    private void init() {
        debateService.updateDebateProcess();
        statsService.updateStats();
    }
}

