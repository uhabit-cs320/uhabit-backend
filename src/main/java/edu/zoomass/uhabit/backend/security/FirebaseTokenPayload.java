package edu.zoomass.uhabit.backend.security;

import io.jsonwebtoken.Claims;
import lombok.Getter;

@Getter
public class FirebaseTokenPayload {
    private final String uid;
    private final String email;
    private final String name;
    private final boolean emailVerified;

    public FirebaseTokenPayload(Claims claims) {
        this.uid = claims.get("user_id", String.class);
        this.email = claims.get("email", String.class);
        this.name = claims.get("name", String.class);
        this.emailVerified = claims.get("email_verified", Boolean.class);
    }
}