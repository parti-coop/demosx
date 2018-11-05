package seoul.democracy.user.utils;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import seoul.democracy.user.domain.User;
import seoul.democracy.user.dto.UserDetail;

public class UserUtils {

    public static User getLoginUser() {
        SecurityContext context = SecurityContextHolder.getContext();
        if (context == null) return null;

        Authentication authentication = context.getAuthentication();
        if (authentication == null) return null;

        if (authentication.getPrincipal() instanceof UserDetail)
            return ((UserDetail) authentication.getPrincipal()).getUser();

        return null;
    }

    public static boolean isLogin() {
        return getLoginUser() != null;
    }

    public static String getEmail() {
        return getLoginUser().getEmail();
    }

    public static Long getUserId() {
        return getLoginUser().getId();
    }
}
