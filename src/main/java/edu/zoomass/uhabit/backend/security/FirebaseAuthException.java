package edu.zoomass.uhabit.backend.security;

public class FirebaseAuthException extends RuntimeException {
    public FirebaseAuthException(String message) {
        super(message);
    }

    public FirebaseAuthException(String message, Throwable cause) {
        super(message, cause);
    }
}