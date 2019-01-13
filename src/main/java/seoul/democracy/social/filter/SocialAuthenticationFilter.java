package seoul.democracy.social.filter;

import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.oauth.OAuth20Service;
import lombok.Setter;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import seoul.democracy.social.service.SocialAuthenticationToken;
import seoul.democracy.social.service.SocialService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class SocialAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    @Setter
    private SocialService socialService;

    public SocialAuthenticationFilter() {
        super("/auth");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        String provider = request.getParameter("p");

        try {
            RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request, response));

            if ("naver".equals(provider)) {
                String state = request.getParameter("state");
                if (state == null || !state.equals(request.getSession().getAttribute("auth_state"))) {
                    return null;
                }

                String code = request.getParameter("code");
                if (StringUtils.isEmpty(code)) return null;

                OAuth20Service service = socialService.naver();

                try {
                    OAuth2AccessToken accessToken = service.getAccessToken(code);
                    SocialAuthenticationToken authRequest = new SocialAuthenticationToken(provider, accessToken);

                    return this.getAuthenticationManager().authenticate(authRequest);
                } catch (InterruptedException | ExecutionException e) {
                    throw new BadCredentialsException("Unknown access token");
                }
            }
        } finally {
            RequestContextHolder.resetRequestAttributes();
        }

        return null;
    }
}
