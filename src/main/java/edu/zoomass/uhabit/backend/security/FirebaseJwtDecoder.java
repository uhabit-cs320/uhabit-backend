package edu.zoomass.uhabit.backend.security;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.RSAPublicKeySpec;
import java.util.Base64;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class FirebaseJwtDecoder {
    private static final String FIREBASE_PUBLIC_KEYS_URL = 
        "https://www.googleapis.com/robot/v1/metadata/x509/securetoken@system.gserviceaccount.com";
    
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final Map<String, PublicKey> publicKeys = new ConcurrentHashMap<>();
    
    public FirebaseJwtDecoder(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }
    
    public FirebaseTokenPayload decodeToken(String token) {
        String kid = extractKeyId(token);
        PublicKey publicKey = getPublicKey(kid);
        
        JwtParser parser = Jwts.parserBuilder()
            .setSigningKey(publicKey)
            .build();
        
        Jws<Claims> claims = parser.parseClaimsJws(token);
        return new FirebaseTokenPayload(claims.getBody());
    }
    
    private String extractKeyId(String token) {
        String[] parts = token.split("\\.");
        if (parts.length != 3) {
            throw new IllegalArgumentException("Invalid token format");
        }
        
        String header = new String(Base64.getUrlDecoder().decode(parts[0]));
        try {
            JsonNode headerNode = objectMapper.readTree(header);
            return headerNode.get("kid").asText();
        } catch (Exception e) {
            throw new IllegalArgumentException("Could not extract key ID from token", e);
        }
    }
    
    @Cacheable(value = "firebasePublicKeys", key = "#kid")
    public PublicKey getPublicKey(String kid) {
        return publicKeys.computeIfAbsent(kid, k -> {
            try {
                String publicKeyPem = restTemplate.getForObject(FIREBASE_PUBLIC_KEYS_URL, String.class);
                JsonNode publicKeysNode = objectMapper.readTree(publicKeyPem);
                String keyValue = publicKeysNode.get(kid).asText();
                
                // Convert PEM to RSA Public Key
                byte[] publicKeyBytes = Base64.getDecoder().decode(keyValue
                    .replace("-----BEGIN CERTIFICATE-----", "")
                    .replace("-----END CERTIFICATE-----", "")
                    .replaceAll("\\s+", ""));
                
                BigInteger modulus = new BigInteger(1, publicKeyBytes);
                BigInteger exponent = BigInteger.valueOf(65537); // Standard RSA exponent
                
                RSAPublicKeySpec spec = new RSAPublicKeySpec(modulus, exponent);
                KeyFactory factory = KeyFactory.getInstance("RSA");
                return factory.generatePublic(spec);
            } catch (Exception e) {
                throw new RuntimeException("Failed to load public key", e);
            }
        });
    }
}