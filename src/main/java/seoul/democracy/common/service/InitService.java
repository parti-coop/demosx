package seoul.democracy.common.service;

import org.springframework.beans.factory.annotation.Autowired;
import seoul.democracy.debate.service.DebateService;

import javax.annotation.PostConstruct;

public class InitService {

    private final DebateService debateService;

    @Autowired
    public InitService(DebateService debateService) {
        this.debateService = debateService;
    }

    @PostConstruct
    private void init() {
        debateService.updateDebateProcess();
    }
}

