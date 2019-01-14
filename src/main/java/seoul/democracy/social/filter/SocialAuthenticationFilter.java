package seoul.democracy.social.filter;

import com.github.scribejava.core.model.OAuth1AccessToken;
import com.github.scribejava.core.model.OAuth1RequestToken;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.oauth.OAuth10aService;
import com.github.scribejava.core.oauth.OAuth20Service;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
public class SocialAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    @Setter
    private SocialService socialService;

    public SocialAuthenticationFilter() {
        super("/auth/**");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        String uri = request.getRequestURI();
        String provider = uri.substring(uri.lastIndexOf("/") + 1);

        try {
            RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request, response));
            switch (provider) {
                case "naver": {
                    String code = getCode(request);

                    OAuth20Service service = socialService.naver();
                    OAuth2AccessToken accessToken = service.getAccessToken(code);
                    SocialAuthenticationToken authRequest = new SocialAuthenticationToken(provider, accessToken);

                    return this.getAuthenticationManager().authenticate(authRequest);
                }
                case "kakao": {
                    String code = getCode(request);

                    OAuth20Service service = socialService.kakao();
                    OAuth2AccessToken accessToken = service.getAccessToken(code);
                    SocialAuthenticationToken authRequest = new SocialAuthenticationToken(provider, accessToken);

                    return this.getAuthenticationManager().authenticate(authRequest);
                }
                case "twitter": {
                    String oauthVerifier = request.getParameter("oauth_verifier");
                    OAuth1RequestToken requestToken = (OAuth1RequestToken) socialService.getSession(request.getSession());

                    OAuth10aService service = socialService.twitter();
                    OAuth1AccessToken accessToken = service.getAccessToken(requestToken, oauthVerifier);
                    SocialAuthenticationToken authRequest = new SocialAuthenticationToken(provider, accessToken);

                    return this.getAuthenticationManager().authenticate(authRequest);
                }
                case "facebook": {
                    String code = getCode(request);

                    OAuth20Service service = socialService.facebook();
                    OAuth2AccessToken accessToken = service.getAccessToken(code);
                    SocialAuthenticationToken authRequest = new SocialAuthenticationToken(provider, accessToken);

                    return this.getAuthenticationManager().authenticate(authRequest);
                }
                default: {
                    return null;
                }
            }
        } catch (InterruptedException | ExecutionException e) {
            throw new BadCredentialsException("Unknown access token");
        } finally {
            RequestContextHolder.resetRequestAttributes();
        }
    }

    private String getCode(HttpServletRequest request) {
        String state = request.getParameter("state");
        if (state == null || !state.equals(socialService.getSession(request.getSession()))) {
            throw new BadCredentialsException("Unknown access token");
        }

        String code = request.getParameter("code");
        if (StringUtils.isEmpty(code)) throw new BadCredentialsException("Unknown access token");

        return code;
    }
}
