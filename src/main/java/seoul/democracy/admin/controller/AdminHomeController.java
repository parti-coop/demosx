package seoul.democracy.admin.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import seoul.democracy.action.repository.ActionRepository;
import seoul.democracy.debate.repository.DebateRepository;
import seoul.democracy.issue.domain.IssueGroup;
import seoul.democracy.issue.domain.IssueType;
import seoul.democracy.issue.repository.IssueLikeRepository;
import seoul.democracy.opinion.repository.OpinionLikeRepository;
import seoul.democracy.opinion.repository.OpinionRepository;
import seoul.democracy.proposal.repository.ProposalRepository;
import seoul.democracy.user.repository.UserRepository;

import static seoul.democracy.debate.predicate.DebatePredicate.equalGroup;
import static seoul.democracy.opinion.predicate.OpinionPredicate.equalIssueType;
import static seoul.democracy.opinion.predicate.OpinionPredicate.equalIssueTypeAndIssueGroup;

@Controller
@RequestMapping("/admin")
public class AdminHomeController {

    private final UserRepository userRepository;
    private final OpinionRepository opinionRepository;
    private final OpinionLikeRepository opinionLikeRepository;
    private final IssueLikeRepository issueLikeRepository;

    private final ProposalRepository proposalRepository;
    private final DebateRepository debateRepository;
    private final ActionRepository actionRepository;

    @Autowired
    public AdminHomeController(UserRepository userRepository,
                               OpinionRepository opinionRepository,
                               OpinionLikeRepository opinionLikeRepository,
                               ProposalRepository proposalRepository,
                               DebateRepository debateRepository,
                               ActionRepository actionRepository,
                               IssueLikeRepository issueLikeRepository) {
        this.userRepository = userRepository;
        this.opinionRepository = opinionRepository;
        this.opinionLikeRepository = opinionLikeRepository;
        this.proposalRepository = proposalRepository;
        this.debateRepository = debateRepository;
        this.issueLikeRepository = issueLikeRepository;
        this.actionRepository = actionRepository;
    }

    @RequestMapping(value = "/index.do", method = RequestMethod.GET)
    public String index(Model model) {

        model.addAttribute("userCount", userRepository.count());
        model.addAttribute("opinionCount", opinionRepository.count());
        model.addAttribute("opinionLikeCount", opinionLikeRepository.count());

        model.addAttribute("proposalCount", proposalRepository.count());
        model.addAttribute("proposalOpinionCount", opinionRepository.count(equalIssueType(IssueType.P)));
        model.addAttribute("issueLikeCount", issueLikeRepository.count());

        model.addAttribute("debateCount", debateRepository.count(equalGroup(IssueGroup.USER)));
        model.addAttribute("debateOpinionCount", opinionRepository.count(equalIssueTypeAndIssueGroup(IssueType.D, IssueGroup.USER)));

        model.addAttribute("actionCount", actionRepository.count());

        model.addAttribute("orgDebateCount", debateRepository.count(equalGroup(IssueGroup.ORG)));
        model.addAttribute("orgDebateOpinionCount", opinionRepository.count(equalIssueTypeAndIssueGroup(IssueType.D, IssueGroup.ORG)));

        return "/admin/index";
    }
}
