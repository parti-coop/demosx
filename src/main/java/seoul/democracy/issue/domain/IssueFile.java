package seoul.democracy.issue.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * 이슈 파일
 */
@Getter
@Embeddable
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
public class IssueFile {

    /**
     * 파일 이름
     */
    @Column(name = "FILE_NAME")
    private String name;

    /**
     * 파일 URL
     */
    @Column(name = "FILE_URL")
    private String url;
}
