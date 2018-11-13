package seoul.democracy.issue.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.CollectionUtils;
import seoul.democracy.issue.dto.IssueFileDto;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.ArrayList;
import java.util.List;

/**
 * 이슈 파일
 */
@Getter
@Embeddable
@NoArgsConstructor
@AllArgsConstructor(staticName = "create")
public class IssueFile {

    /**
     * 파일 순번
     */
    @Column(name = "FILE_SEQ")
    private Integer seq;

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

    public static List<IssueFile> create(List<IssueFileDto> createFiles) {
        if (CollectionUtils.isEmpty(createFiles)) return null;

        List<IssueFile> files = new ArrayList<>();
        for (int i = 0; i < createFiles.size(); i++) {
            IssueFileDto fileDto = createFiles.get(i);
            files.add(IssueFile.create(i, fileDto.getName(), fileDto.getUrl()));
        }
        return files;
    }
}
