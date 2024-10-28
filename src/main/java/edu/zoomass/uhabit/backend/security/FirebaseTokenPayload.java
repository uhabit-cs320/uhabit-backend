package edu.zoomass.uhabit.backend.security;

import io.jsonwebtoken.Claims;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FirebaseTokenPayload {
    private final String userId;
    private final String email;
    private final String name;
    private final boolean emailVerified;
    private final String picture;
    private final Long authTime;
}