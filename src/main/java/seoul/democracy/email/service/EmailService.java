package seoul.democracy.email.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

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

    @Autowired
    public EmailService(JavaMailSender mailSender,
                        String resetPasswordEmailContent,
                        String registerProposalEmailContent,
                        String assignProposalEmailContent,
                        String dropProposalEmailContent,
                        String opinionProposalEmailContent,
                        String passProposalEmailContent) {
        this.mailSender = mailSender;
        this.resetPasswordEmailContent = resetPasswordEmailContent;
        this.registerProposalEmailContent = registerProposalEmailContent;
        this.assignProposalEmailContent = assignProposalEmailContent;
        this.dropProposalEmailContent = dropProposalEmailContent;
        this.opinionProposalEmailContent = opinionProposalEmailContent;
        this.passProposalEmailContent = passProposalEmailContent;
    }

    private void sendEmail(String email, String title, String content) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();

        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        helper.setFrom("민주주의 서울<hans@slowalk.co.kr>");
        helper.setTo(email);
        helper.setSubject(title);

        helper.setText(content, true);

        mailSender.send(message);
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
    public void assignProposal(String email, String name) throws MessagingException {
        String content = String.format(assignProposalEmailContent, name, host);
        sendEmail(email, "[서울특별시 응답소]민원 처리완료 알림", content);
    }

    /**
     * 민주주의 서울 시민 제안에 50공감을 얻지 못하였을 시
     * (제안등록 후 20일 이후)
     */
    public void dropProposal(String email, String name) throws MessagingException {
        String content = String.format(dropProposalEmailContent, name, host);
        sendEmail(email, "[서울특별시 민주주의 서울]제안 투표 탈락 알림", content);
    }

    /**
     * 민주주의 서울 시민 제안에 대한 부서 의견 등록시
     */
    public void passProposal(String email, String name) throws MessagingException {
        String content = String.format(dropProposalEmailContent, name, host);
        sendEmail(email, "[서울특별시 응답소] 제안 투표 통과 알림", content);
    }

    /**
     * 제안 댓글 등록 시 등록자에게 메일 발송
     */
    public void opinionProposal(String email, String name) throws MessagingException {
        String content = String.format(dropProposalEmailContent, name, host);
        sendEmail(email, "[민주주의 서울] 댓글 등록 알림", content);
    }
}
