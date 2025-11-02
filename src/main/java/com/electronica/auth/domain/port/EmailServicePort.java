package com.electronica.auth.domain.port;

public interface EmailServicePort {
    void sendPasswordResetEmail(String toEmail, String resetLink);
}