package edu.zoomass.uhabit.backend.security;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

import edu.zoomass.uhabit.backend.user.UserProfile;

public class FirebaseAuthenticationToken extends AbstractAuthenticationToken {
    private final UserProfile principal;
    private final String firebaseToken;

    public FirebaseAuthenticationToken(String firebaseToken) {
        super(null);
        this.principal = null;
        this.firebaseToken = firebaseToken;
        setAuthenticated(false);
    }

    public FirebaseAuthenticationToken(UserProfile principal, String firebaseToken,
                                       Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.principal = principal;
        this.firebaseToken = firebaseToken;
        setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return firebaseToken;
    }

    @Override
    public UserProfile getPrincipal() {
        return principal;
    }
}