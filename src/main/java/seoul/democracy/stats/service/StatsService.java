package seoul.democracy.stats.service;

import com.mysema.query.Tuple;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import seoul.democracy.issue.domain.IssueGroup;
import seoul.democracy.issue.domain.IssueType;
import seoul.democracy.issue.repository.IssueRepository;
import seoul.democracy.opinion.repository.OpinionRepository;
import seoul.democracy.stats.domain.StatsIssue;
import seoul.democracy.stats.domain.StatsOpinion;
import seoul.democracy.stats.repository.StatsIssueRepository;
import seoul.democracy.stats.repository.StatsOpinionRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@Transactional(readOnly = true)
public class StatsService {

    private final StatsIssueRepository statsIssueRepository;
    private final StatsOpinionRepository statsOpinionRepository;

    private final IssueRepository issueRepository;
    private final OpinionRepository opinionRepository;

    private final Pageable pageByDateLimit1 = new PageRequest(0, 1, new Sort(Sort.Direction.DESC, "date"));

    @Autowired
    public StatsService(StatsIssueRepository statsIssueRepository,
                        StatsOpinionRepository statsOpinionRepository,
                        IssueRepository issueRepository,
                        OpinionRepository opinionRepository) {
        this.statsIssueRepository = statsIssueRepository;
        this.statsOpinionRepository = statsOpinionRepository;
        this.issueRepository = issueRepository;
        this.opinionRepository = opinionRepository;
    }

    public Page<StatsIssue> getStatsIssues(Pageable pageable) {
        return statsIssueRepository.findAll(pageable);
    }

    public Page<StatsOpinion> getStatsOpinions(Pageable pageable) {
        return statsOpinionRepository.findAll(pageable);
    }

    /**
     * 매일 자정마다 상태 변경 실행됨, 최초 서버 구동시 실행됨
     */
    @Transactional
    @Scheduled(cron = "0 10 0 * * *")
    public void updateStats() {
        updateStatsIssue();
        updateStatsOpinion();
    }

    private void updateStatsIssue() {
        LocalDate now = LocalDate.now();
        log.info("통계 처리 날짜 : {}", now);

        Page<StatsIssue> statsIssues = statsIssueRepository.findAll(pageByDateLimit1);
        StatsIssue latestStatsIssue = statsIssues.getContent().get(0);
        LocalDate date = latestStatsIssue.getDate().plusDays(1);

        List<StatsIssue> savedStatsIssues = new ArrayList<>();
        while (date.isBefore(now)) {
            Long proposalCount = 0L;
            Long debateCount = 0L;
            Long actionCount = 0L;
            Long orgDebateCount = 0L;
            List<Tuple> results = issueRepository.getStatsByDate(date);
            for (Tuple result : results) {
                IssueType type = result.get(0, IssueType.class);
                IssueGroup group = result.get(1, IssueGroup.class);
                Long count = result.get(2, Long.class);
                if (type == IssueType.P) proposalCount = count;
                else if (type == IssueType.D && group == IssueGroup.USER) debateCount = count;
                else if (type == IssueType.D && group == IssueGroup.ORG) orgDebateCount = count;
                else if (type == IssueType.A) actionCount = count;
            }

            StatsIssue statsIssue = StatsIssue.create(date, proposalCount, debateCount, actionCount, orgDebateCount);
            savedStatsIssues.add(statsIssue);
            date = date.plusDays(1);
        }
        statsIssueRepository.save(savedStatsIssues);
    }

    private void updateStatsOpinion() {
        LocalDate now = LocalDate.now();

        Page<StatsOpinion> statsOpinions = statsOpinionRepository.findAll(pageByDateLimit1);
        StatsOpinion latestStatsOpinion = statsOpinions.getContent().get(0);
        LocalDate date = latestStatsOpinion.getDate().plusDays(1);

        List<StatsOpinion> savedStatsOpinions = new ArrayList<>();
        while (date.isBefore(now)) {
            Long proposalCount = 0L;
            Long debateCount = 0L;
            Long orgDebateCount = 0L;
            List<Tuple> results = opinionRepository.getStatsByDate(date);
            for (Tuple result : results) {
                IssueType type = result.get(0, IssueType.class);
                IssueGroup group = result.get(1, IssueGroup.class);
                Long count = result.get(2, Long.class);
                if (type == IssueType.P) proposalCount = count;
                else if (type == IssueType.D && group == IssueGroup.USER) debateCount = count;
                else if (type == IssueType.D && group == IssueGroup.ORG) orgDebateCount = count;
            }
            StatsOpinion statsOpinion = StatsOpinion.create(date, proposalCount, debateCount, orgDebateCount);
            savedStatsOpinions.add(statsOpinion);
            date = date.plusDays(1);
        }
        statsOpinionRepository.save(savedStatsOpinions);
    }
}
