package seoul.democracy.proposal.service;

import com.mysema.query.types.Expression;
import com.mysema.query.types.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import seoul.democracy.common.exception.BadRequestException;
import seoul.democracy.issue.domain.Category;
import seoul.democracy.issue.predicate.CategoryPredicate;
import seoul.democracy.issue.repository.CategoryRepository;
import seoul.democracy.proposal.domain.Proposal;
import seoul.democracy.proposal.dto.ProposalCreateDto;
import seoul.democracy.proposal.dto.ProposalDto;
import seoul.democracy.proposal.repository.ProposalRepository;

@Service
@Transactional(readOnly = true)
public class ProposalService {

    private final ProposalRepository proposalRepository;
    private final CategoryRepository categoryRepository;

    @Autowired
    public ProposalService(ProposalRepository proposalRepository,
                           CategoryRepository categoryRepository) {
        this.proposalRepository = proposalRepository;
        this.categoryRepository = categoryRepository;
    }

    public ProposalDto getProposal(Predicate predicate, Expression<ProposalDto> projection) {
        return proposalRepository.findOne(predicate, projection);
    }

    @Transactional
    public Proposal create(ProposalCreateDto createDto, String ip) {
        Category category = categoryRepository.findOne(CategoryPredicate.equalName(createDto.getCategory()));
        if (category == null || !category.getEnabled()) {
            throw new BadRequestException("category", "error.category", "카테고리를 확인해 주세요.");
        }
        Proposal proposal = Proposal.create(createDto, category, ip);

        return proposalRepository.save(proposal);
    }
}
