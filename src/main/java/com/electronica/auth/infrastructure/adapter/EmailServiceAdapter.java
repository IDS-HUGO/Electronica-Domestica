package com.electronica.auth.infrastructure.adapter;

import com.electronica.auth.domain.port.EmailServicePort;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class EmailServiceAdapter implements EmailServicePort {
    private final String fromEmail;
    private final String password;
    private final Properties properties;

    public EmailServiceAdapter(String fromEmail, String password, String smtpHost, String smtpPort) {
        this.fromEmail = fromEmail;
        this.password = password;

        this.properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", smtpHost);
        properties.put("mail.smtp.port", smtpPort);
    }

    @Override
    public void sendPasswordResetEmail(String toEmail, String resetLink) {
        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmail, password);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(fromEmail));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject("Recuperación de Contraseña - Electrónica Doméstica");

            String htmlContent = """
                <html>
                <body>
                    <h2>Recuperación de Contraseña</h2>
                    <p>Has solicitado restablecer tu contraseña.</p>
                    <p>Haz clic en el siguiente enlace para crear una nueva contraseña:</p>
                    <a href="%s">Restablecer Contraseña</a>
                    <p>Este enlace expirará en 1 hora.</p>
                    <p>Si no solicitaste este cambio, ignora este correo.</p>
                </body>
                </html>
            """.formatted(resetLink);

            message.setContent(htmlContent, "text/html; charset=utf-8");

            Transport.send(message);

            System.out.println("Email de recuperación enviado a: " + toEmail);

        } catch (MessagingException e) {
            System.err.println("Error al enviar email: " + e.getMessage());
        }
    }
}