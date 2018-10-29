package seoul.democracy.common.config;

import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;
import seoul.democracy.user.domain.User;
import seoul.democracy.user.utils.UserUtils;

@Component
public class UserAuditorAware implements AuditorAware<User> {

    @Override
    public User getCurrentAuditor() {
        return UserUtils.getLoginUser();
    }
}
