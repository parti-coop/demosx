package seoul.democracy.social.service;

import com.github.scribejava.core.model.Token;
import lombok.Getter;
import org.springframework.security.authentication.AbstractAuthenticationToken;

@Getter
public class SocialAuthenticationToken extends AbstractAuthenticationToken {

    private String provider;
    private Token token;

    /**
     * Creates a token with the supplied array of authorities.
     */
    public SocialAuthenticationToken(String provider, Token token) {
        super(null);
        this.provider = provider;
        this.token = token;
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return getDetails();
    }
}
