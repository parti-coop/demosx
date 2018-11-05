package seoul.democracy.history.service;

import com.mysema.query.types.Predicate;
import com.mysema.query.types.QBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import seoul.democracy.common.exception.NotFoundException;
import seoul.democracy.history.domain.IssueHistory;
import seoul.democracy.history.dto.IssueHistoryCreateDto;
import seoul.democracy.history.dto.IssueHistoryDto;
import seoul.democracy.history.repository.IssueHistoryRepository;
import seoul.democracy.issue.domain.Issue;
import seoul.democracy.issue.repository.IssueRepository;

@Service
@Transactional(readOnly = true)
public class IssueHistoryService {

    private final IssueHistoryRepository historyRepository;
    private final IssueRepository issueRepository;

    @Autowired
    public IssueHistoryService(IssueHistoryRepository historyRepository,
                               IssueRepository issueRepository) {
        this.historyRepository = historyRepository;
        this.issueRepository = issueRepository;
    }

    public IssueHistoryDto getHistory(Predicate predicate, QBean<IssueHistoryDto> projection) {
        return historyRepository.findOne(predicate, projection);
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public IssueHistory create(IssueHistoryCreateDto createDto, String ip) {
        Issue issue = issueRepository.findOne(createDto.getIssueId());
        if (issue == null)
            throw new NotFoundException("해당 글을 찾을 수 없습니다.");

        IssueHistory history = issue.createHistory(createDto.getContent(), ip);

        return historyRepository.save(history);
    }

}
