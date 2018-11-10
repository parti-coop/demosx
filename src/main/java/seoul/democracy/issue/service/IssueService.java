package seoul.democracy.issue.service;

import com.mysema.query.types.Expression;
import com.mysema.query.types.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import seoul.democracy.issue.dto.IssueDto;
import seoul.democracy.issue.repository.IssueRepository;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class IssueService {

    private final IssueRepository issueRepository;

    @Autowired
    public IssueService(IssueRepository issueRepository) {
        this.issueRepository = issueRepository;
    }

    public List<IssueDto> getIssues(Predicate predicate, Expression<IssueDto> projection) {
        return issueRepository.findAll(predicate, projection);
    }
}
