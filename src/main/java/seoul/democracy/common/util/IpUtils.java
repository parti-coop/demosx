package seoul.democracy.common.util;

import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

public class IpUtils {

    public static String getIp() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        return getIp(request);
    }

    public static String getIp(HttpServletRequest request) {
        if (request == null) return "empty";

        String remoteAddr = request.getHeader("X-FORWARDED_FOR");
        if (StringUtils.isEmpty(remoteAddr)) {
            return request.getRemoteAddr();
        }
        return "empty";
    }
}
