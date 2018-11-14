package seoul.democracy.user.domain;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import seoul.democracy.issue.domain.Category;
import seoul.democracy.user.dto.UserManagerUpdateDto;

import javax.persistence.*;
import java.io.Serializable;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor(staticName = "create")
public class UserDepartment implements Serializable {

    /**
     * 카테고리
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CATE_ID")
    private Category category;

    /**
     * 부서명1
     */
    @Column(name = "DEPART1")
    private String department1;

    /**
     * 부서명2
     */
    @Column(name = "DEPART2")
    private String department2;

    /**
     * 부서명3
     */
    @Column(name = "DEPART3")
    private String department3;

    public void update(UserManagerUpdateDto updateDto, Category category) {
        this.category = category;
        this.department1 = updateDto.getDepartment1();
        this.department2 = updateDto.getDepartment2();
        this.department3 = updateDto.getDepartment3();
    }
}
