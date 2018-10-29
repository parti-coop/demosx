package seoul.democracy.issue.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import seoul.democracy.issue.dto.CategoryCreateDto;
import seoul.democracy.issue.dto.CategoryUpdateDto;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * 이슈범주 - 카테고리
 */
@Getter
@NoArgsConstructor
@Entity(name = "TB_ISSUE_CATEGORY")
public class Category {

    @Id
    @GeneratedValue(generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    @Column(name = "CATE_ID")
    private Long id;

    /**
     * 범주 이름
     */
    @Column(name = "CATE_NAME")
    private String name;

    /**
     * 사용 여부
     */
    @Column(name = "USE_YN")
    private Boolean enabled;

    /**
     * 범주 순번
     */
    @Column(name = "CATE_SEQ")
    private Integer sequence;

    private Category(String name, Boolean enabled, Integer sequence) {
        this.name = name;
        this.enabled = enabled;
        this.sequence = sequence;
    }

    public static Category create(CategoryCreateDto createDto) {
        return new Category(createDto.getName(), createDto.getEnabled(), createDto.getSequence());
    }

    public Category update(CategoryUpdateDto updateDto) {
        this.name = updateDto.getName();
        this.enabled = updateDto.getEnabled();
        this.sequence = updateDto.getSequence();
        return this;
    }
}
