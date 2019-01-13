package seoul.democracy.email.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.PayloadApplicationEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import seoul.democracy.email.service.EmailService;
import seoul.democracy.proposal.domain.Proposal;
import seoul.democracy.proposal.event.ProposalAssignedManagerEvent;
import seoul.democracy.proposal.event.ProposalCompletedEvent;
import seoul.democracy.proposal.event.ProposalCreatedEvent;

import javax.mail.MessagingException;

@Slf4j
@Service
public class EmailEventListener {

    private final EmailService emailService;

    @Autowired
    public EmailEventListener(EmailService emailService) {
        this.emailService = emailService;
    }

    @EventListener
    public void listenProposalCreatedEvent(PayloadApplicationEvent<ProposalCreatedEvent> event) {
        ProposalCreatedEvent createdEvent = event.getPayload();

        Proposal proposal = createdEvent.getProposal();

        if (!proposal.getCreatedBy().getProvider().equals("email")) return;

        try {
            emailService.registerProposal(proposal.getCreatedBy().getEmail(), proposal.getCreatedBy().getName());
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    @EventListener
    public void listenProposalAssignedManagerEvent(PayloadApplicationEvent<ProposalAssignedManagerEvent> event) {
        ProposalAssignedManagerEvent assignedManagerEvent = event.getPayload();

        Proposal proposal = assignedManagerEvent.getProposal();

        if (!proposal.getCreatedBy().getProvider().equals("email")) return;

        try {
            emailService.assignedProposal(proposal.getCreatedBy().getEmail(), proposal.getCreatedBy().getName());
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    @EventListener
    public void listenProposalCompletedEvent(PayloadApplicationEvent<ProposalCompletedEvent> event) {
        ProposalCompletedEvent completedEvent = event.getPayload();

        Proposal proposal = completedEvent.getProposal();

        if (!proposal.getCreatedBy().getProvider().equals("email")) return;

        try {
            emailService.completedProposal(proposal.getCreatedBy().getEmail(), proposal.getCreatedBy().getName());
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
