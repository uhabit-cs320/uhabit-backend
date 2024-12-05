package edu.zoomass.uhabit.backend.security;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.zoomass.uhabit.backend.user.UserProfile;
import edu.zoomass.uhabit.backend.user.UserService;
import io.jsonwebtoken.*;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.security.PublicKey;
import java.util.Base64;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class FirebaseJwtDecoder {
    private static final String FIREBASE_PUBLIC_KEYS_URL =
            "https://www.googleapis.com/robot/v1/metadata/x509/securetoken@system.gserviceaccount.com";
    private static final String FIREBASE_ISSUER_PREFIX = "https://securetoken.google.com/";
    private static final String FIREBASE_AUDIENCE_PREFIX = "firebase-adminsdk-";

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final UserService userService;
    private final Map<String, PublicKey> publicKeyCache;
    private static final String PROJECT_ID = "cs320-d0a69"; // Your Firebase project ID

    public FirebaseJwtDecoder(RestTemplate restTemplate, ObjectMapper objectMapper,
                              UserService userService) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
        this.userService = userService;
        this.publicKeyCache = new ConcurrentHashMap<>();
    }

    @Transactional
    public UserProfile verifyTokenAndGetUser(String token) {
        FirebaseTokenPayload payload = decodeToken(token);

        // Try to find existing user
        UserProfile user = userService.getUserByEmail(payload.getEmail());

        if (user == null) {
            // Create new user if doesn't exist
            user = new UserProfile();
            user.setEmail(payload.getEmail());
            user.setId(generateUserId(payload.getUserId()));
            user.setName(payload.getName());
            user.setPicture(payload.getPicture());
            user = userService.saveUser(user);
        }

        if (!payload.getName().equals(user.getName()) || !payload.getPicture().equals(user.getPicture())) {
            user.setName(payload.getName());
            user.setPicture(payload.getPicture());
            user = userService.saveUser(user);
        }

        return user;
    }

    public FirebaseTokenPayload decodeToken(String token) {
        try {
            String[] parts = token.split("\\.");
            if (parts.length != 2) {
                throw new FirebaseAuthException("Invalid token format: expected 2 parts but got " + parts.length);
            }

            // Decode the payload (second part)
            String payloadJson = new String(Base64.getUrlDecoder().decode(parts[1]));
            JsonNode payloadNode = objectMapper.readTree(payloadJson);

            return FirebaseTokenPayload.builder()
                    .userId(payloadNode.get("user_id").asText())
                    .email(payloadNode.get("email").asText())
                    .name(payloadNode.has("name") ? payloadNode.get("name").asText() : null)
                    .emailVerified(payloadNode.get("email_verified").asBoolean())
                    .picture(payloadNode.has("picture") ? payloadNode.get("picture").asText() : null)
                    .authTime(payloadNode.get("auth_time").asLong())
                    .build();

        } catch (Exception e) {
            throw new FirebaseAuthException("Failed to decode token: " + e.getMessage(), e);
        }
    }

    private String extractKeyId(String token) {
        try {
            System.out.println("Token: " + token);
            String[] parts = token.split("\\.");
            if (parts.length != 3) {
                throw new FirebaseAuthException("Invalid token format: " + parts.length + " parts");
            }

            String headerJson = new String(Base64.getUrlDecoder().decode(parts[0]));
            JsonNode headerNode = objectMapper.readTree(headerJson);

            if (!headerNode.has("kid")) {
                throw new FirebaseAuthException("Token header missing 'kid' claim");
            }

            return headerNode.get("kid").asText();
        } catch (Exception e) {
            throw new FirebaseAuthException("Failed to extract key ID from token", e);
        }
    }

    @Cacheable(value = "firebasePublicKeys", unless = "#result == null")
    public PublicKey getPublicKey(String kid) {
        try {
            // Check cache first
            PublicKey cachedKey = publicKeyCache.get(kid);
            if (cachedKey != null) {
                return cachedKey;
            }

            // Fetch fresh certificates if not in cache
            ResponseEntity<String> response = restTemplate.getForEntity(FIREBASE_PUBLIC_KEYS_URL, String.class);
            JsonNode publicKeysNode = objectMapper.readTree(response.getBody());

            if (!publicKeysNode.has(kid)) {
                throw new FirebaseAuthException("Public key not found for kid: " + kid);
            }

            String publicKeyPem = publicKeysNode.get(kid).asText();
            PublicKey publicKey = parseX509Certificate(publicKeyPem);

            // Update cache
            publicKeyCache.put(kid, publicKey);

            return publicKey;
        } catch (Exception e) {
            throw new FirebaseAuthException("Failed to retrieve public key", e);
        }
    }

    private PublicKey parseX509Certificate(String certificatePem) {
        try {
            // Remove PEM headers and newlines
            String encodedCert = certificatePem
                    .replace("-----BEGIN CERTIFICATE-----", "")
                    .replace("-----END CERTIFICATE-----", "")
                    .replaceAll("\\s+", "");

            byte[] certBytes = Base64.getDecoder().decode(encodedCert);

            // Extract public key from certificate
            java.security.cert.CertificateFactory cf =
                    java.security.cert.CertificateFactory.getInstance("X.509");
            java.security.cert.X509Certificate cert =
                    (java.security.cert.X509Certificate) cf.generateCertificate(
                            new java.io.ByteArrayInputStream(certBytes));

            return cert.getPublicKey();
        } catch (Exception e) {
            throw new FirebaseAuthException("Failed to parse X.509 certificate", e);
        }
    }

    private void validateTokenClaims(Claims claims) {
        long currentTimeSeconds = System.currentTimeMillis() / 1000;

        // Validate token time claims
        if (claims.getExpiration() == null ||
                claims.getExpiration().getTime() / 1000 < currentTimeSeconds) {
            throw new FirebaseAuthException("Token has expired");
        }

        if (claims.getIssuedAt() == null ||
                claims.getIssuedAt().getTime() / 1000 > currentTimeSeconds) {
            throw new FirebaseAuthException("Token issued at future timestamp");
        }

        // Validate Firebase-specific claims
        String authTime = claims.get("auth_time", String.class);
        if (authTime == null) {
            throw new FirebaseAuthException("Missing auth_time claim");
        }

        String sub = claims.getSubject();
        if (sub == null || sub.length() == 0 || sub.length() > 128) {
            throw new FirebaseAuthException("Invalid subject claim");
        }
    }

    private long generateUserId(String firebaseUid) {
        // Generate a consistent long ID from Firebase UID
        // You might want to implement a more sophisticated ID generation strategy
        return Math.abs(firebaseUid.hashCode());
    }
}
