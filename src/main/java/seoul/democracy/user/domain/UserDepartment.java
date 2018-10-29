package seoul.democracy.user.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class UserDepartment {

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

}
