package seoul.democracy.proposal.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import seoul.democracy.issue.domain.Category;
import seoul.democracy.issue.domain.Issue;
import seoul.democracy.issue.domain.IssueFile;
import seoul.democracy.issue.domain.IssueStats;
import seoul.democracy.opinion.domain.OpinionType;
import seoul.democracy.proposal.dto.ProposalCreateDto;

import javax.persistence.*;
import java.util.List;

@Getter
@NoArgsConstructor
@Entity
@DiscriminatorValue("P")
public class Proposal extends Issue {

    /**
     * 이슈 상태
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "ISSUE_STATUS")
    private Status status;

    /**
     * 이슈 제목
     */
    @Column(name = "ISSUE_TITLE")
    private String title;

    /**
     * 이슈 내용
     */
    @Lob
    @Column(name = "ISSUE_CONTENT")
    private String content;

    public Proposal(Category category, String title, String content, List<IssueFile> files, String ip) {
        this.stats = IssueStats.create();
        this.status = Status.OPEN;
        this.opinionType = OpinionType.PROPOSAL;
        this.category = category;
        this.title = title;
        this.content = content;
        this.files = files;
        this.createdIp = ip;
        this.modifiedIp = ip;

    }

    public static Proposal create(ProposalCreateDto createDto, Category category, String ip) {
        return new Proposal(category, createDto.getTitle(), createDto.getContent(), createDto.getFiles(), ip);
    }

    public enum Status {
        OPEN,       // 공개
        DELETE,     // 삭제
        BLOCK        // 관리자삭제
    }
}
