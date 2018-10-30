package seoul.democracy.proposal.service;

import com.mysema.query.types.Expression;
import com.mysema.query.types.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import seoul.democracy.common.exception.BadRequestException;
import seoul.democracy.common.exception.NotFoundException;
import seoul.democracy.issue.domain.Category;
import seoul.democracy.issue.predicate.CategoryPredicate;
import seoul.democracy.issue.repository.CategoryRepository;
import seoul.democracy.proposal.domain.Proposal;
import seoul.democracy.proposal.dto.ProposalCreateDto;
import seoul.democracy.proposal.dto.ProposalDto;
import seoul.democracy.proposal.dto.ProposalUpdateDto;
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

    private Proposal getProposal(Long id) {
        Proposal proposal = proposalRepository.findOne(id);
        if (proposal == null || proposal.getStatus().isDelete() || proposal.getStatus().isBlock())
            throw new NotFoundException("해당 제안을 찾을 수 없습니다.");

        return proposal;
    }

    @Transactional
    public Proposal create(ProposalCreateDto createDto, String ip) {
        Category category = categoryRepository.findOne(CategoryPredicate.equalName(createDto.getCategory()));
        if (category == null || !category.getEnabled())
            throw new BadRequestException("category", "error.category", "카테고리를 확인해 주세요.");

        Proposal proposal = Proposal.create(createDto, category, ip);

        return proposalRepository.save(proposal);
    }

    @Transactional
    @PostAuthorize("returnObject.createdById == authentication.principal.user.id")
    public Proposal update(ProposalUpdateDto updateDto, String ip) {
        Proposal proposal = getProposal(updateDto.getId());
        return proposal.update(updateDto, ip);
    }

    @Transactional
    @PostAuthorize("returnObject.createdById == authentication.principal.user.id")
    public Proposal delete(Long id, String ip) {
        Proposal proposal = getProposal(id);
        return proposal.delete(ip);
    }
}
