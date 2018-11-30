package seoul.democracy.email.service;

import com.mysema.query.Tuple;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import seoul.democracy.opinion.repository.OpinionRepository;
import seoul.democracy.proposal.domain.Proposal;
import seoul.democracy.proposal.predicate.ProposalPredicate;
import seoul.democracy.proposal.repository.ProposalRepository;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.List;

@Slf4j
@Service
@Transactional(readOnly = true)
public class EmailService {

    private final String host = "http://localhost:8091";
    private final JavaMailSender mailSender;

    private final String resetPasswordEmailContent;
    private final String registerProposalEmailContent;
    private final String assignProposalEmailContent;
    private final String dropProposalEmailContent;
    private final String opinionProposalEmailContent;
    private final String passProposalEmailContent;

    private final ProposalRepository proposalRepository;
    private final OpinionRepository opinionRepository;

    @Autowired
    public EmailService(JavaMailSender mailSender,
                        String resetPasswordEmailContent,
                        String registerProposalEmailContent,
                        String assignProposalEmailContent,
                        String dropProposalEmailContent,
                        String opinionProposalEmailContent,
                        String passProposalEmailContent,
                        ProposalRepository proposalRepository,
                        OpinionRepository opinionRepository) {

        this.mailSender = mailSender;
        this.resetPasswordEmailContent = resetPasswordEmailContent;
        this.registerProposalEmailContent = registerProposalEmailContent;
        this.assignProposalEmailContent = assignProposalEmailContent;
        this.dropProposalEmailContent = dropProposalEmailContent;
        this.opinionProposalEmailContent = opinionProposalEmailContent;
        this.passProposalEmailContent = passProposalEmailContent;
        this.proposalRepository = proposalRepository;
        this.opinionRepository = opinionRepository;
    }

    private void sendEmail(String email, String title, String content) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();

        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        helper.setFrom("민주주의 서울<hans@slowalk.co.kr>");
        helper.setTo(email);
        helper.setSubject(title);

        helper.setText(content, true);

        log.info("{} : {}", email, title);
        //mailSender.send(message);
    }

    /**
     * 비밀번호 찾기
     */
    public void resetPassword(String email, String token) throws MessagingException {
        String content = String.format(resetPasswordEmailContent, host + "/reset-password.do?token=" + token);
        sendEmail(email, "[민주주의 서울] 비밀번호를 설정해 주세요.", content);
    }

    /**
     * 민주주의 제안 등록 시
     */
    public void registerProposal(String email, String name) throws MessagingException {
        String content = String.format(registerProposalEmailContent, name, host);
        sendEmail(email, "[서울특별시 응답소]민원 등록 알림", content);
    }

    /**
     * 민주주의 서울 시민 제안에 50공감을 얻어 부서 검토로 넘어 갔을시
     */
    public void assignedProposal(String email, String name) throws MessagingException {
        String content = String.format(assignProposalEmailContent, name, host);
        sendEmail(email, "[서울특별시 응답소]민원 처리완료 알림", content);
    }

    /**
     * 민주주의 서울 시민 제안에 대한 부서 의견 등록시
     */
    public void completedProposal(String email, String name) throws MessagingException {
        String content = String.format(passProposalEmailContent, name, host);
        sendEmail(email, "[서울특별시 응답소] 제안 투표 통과 알림", content);
    }

    /**
     * 민주주의 서울 시민 제안에 50공감을 얻지 못하였을 시, 매일 2시에
     * (제안등록 후 20일 이후)
     */
    public void dropProposal(String email, String name) throws MessagingException {
        String content = String.format(dropProposalEmailContent, name, host);
        sendEmail(email, "[서울특별시 민주주의 서울]제안 투표 탈락 알림", content);
    }

    /**
     * 제안 댓글 등록 시 등록자에게 메일 발송, 매일 2시에
     */
    public void newOpinionProposal(String email, String name) throws MessagingException {
        String content = String.format(opinionProposalEmailContent, name, host);
        sendEmail(email, "[민주주의 서울] 댓글 등록 알림", content);
    }


    /**
     * 매일 자정마다 상태 변경 실행됨, 최초 서버 구동시 실행됨
     */
    @Scheduled(cron = "0 0 14 * * *")
    public void sendEmailSchedule() {
        sendDropProposalEmail();
        sendOpinionProposalEmail();
    }

    private void sendDropProposalEmail() {
        Iterable<Proposal> proposals = proposalRepository.findAll(ProposalPredicate.predicateForSendDropEmail());
        proposals.forEach(proposal -> {
            try {
                dropProposal(proposal.getCreatedBy().getEmail(), proposal.getCreatedBy().getName());
            } catch (MessagingException e) {
                e.printStackTrace();
            }
        });
    }

    private void sendOpinionProposalEmail() {
        List<Tuple> results = opinionRepository.getNewOpinionProposal();
        for (Tuple result : results) {
            //Long id = result.get(0, Long.class);
            String email = result.get(1, String.class);
            String name = result.get(2, String.class);

            try {
                newOpinionProposal(email, name);
            } catch (MessagingException e) {
                e.printStackTrace();
            }
        }
    }
}
