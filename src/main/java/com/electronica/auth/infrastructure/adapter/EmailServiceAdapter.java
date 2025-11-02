package com.electronica.auth.infrastructure.adapter;

import com.electronica.auth.domain.port.EmailServicePort;
import com.electronica.auth.infrastructure.config.EnvConfig;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class EmailServiceAdapter implements EmailServicePort {
    private final String fromEmail;
    private final String password;
    private final Properties properties;

    public EmailServiceAdapter() {
        // Cargar configuración desde variables de entorno
        this.fromEmail = EnvConfig.getEmailFrom();
        this.password = EnvConfig.getEmailPassword();

        this.properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", EnvConfig.getEmailSmtpHost());
        properties.put("mail.smtp.port", EnvConfig.getEmailSmtpPort());
        properties.put("mail.smtp.ssl.trust", EnvConfig.getEmailSmtpHost());
        properties.put("mail.smtp.ssl.protocols", "TLSv1.2");

        System.out.println("📧 EmailService inicializado con: " + fromEmail);
    }

    @Override
    public void sendPasswordResetEmail(String toEmail, String resetLink) {
        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmail, password);
            }
        });

        // Habilitar debug (opcional, para ver logs detallados)
        // session.setDebug(true);

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(fromEmail));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject("🔐 Recuperación de Contraseña - " + EnvConfig.getAppName());

            String htmlContent = """
                <!DOCTYPE html>
                <html lang="es">
                <head>
                    <meta charset="UTF-8">
                    <meta name="viewport" content="width=device-width, initial-scale=1.0">
                    <style>
                        body {
                            font-family: Arial, sans-serif;
                            line-height: 1.6;
                            color: #333;
                            max-width: 600px;
                            margin: 0 auto;
                            padding: 20px;
                        }
                        .container {
                            background-color: #f9f9f9;
                            border-radius: 10px;
                            padding: 30px;
                            box-shadow: 0 2px 5px rgba(0,0,0,0.1);
                        }
                        .header {
                            background-color: #4CAF50;
                            color: white;
                            padding: 20px;
                            text-align: center;
                            border-radius: 10px 10px 0 0;
                            margin: -30px -30px 20px -30px;
                        }
                        .button {
                            display: inline-block;
                            padding: 12px 30px;
                            background-color: #4CAF50;
                            color: white !important;
                            text-decoration: none;
                            border-radius: 5px;
                            margin: 20px 0;
                            font-weight: bold;
                        }
                        .button:hover {
                            background-color: #45a049;
                        }
                        .footer {
                            margin-top: 30px;
                            padding-top: 20px;
                            border-top: 1px solid #ddd;
                            font-size: 12px;
                            color: #666;
                            text-align: center;
                        }
                        .warning {
                            background-color: #fff3cd;
                            border-left: 4px solid #ffc107;
                            padding: 10px;
                            margin: 20px 0;
                        }
                    </style>
                </head>
                <body>
                    <div class="container">
                        <div class="header">
                            <h1>🔐 Recuperación de Contraseña</h1>
                        </div>
                        
                        <p>Hola,</p>
                        
                        <p>Has solicitado restablecer tu contraseña en <strong>%s</strong>.</p>
                        
                        <p>Haz clic en el siguiente botón para crear una nueva contraseña:</p>
                        
                        <div style="text-align: center;">
                            <a href="%s" class="button">Restablecer Contraseña</a>
                        </div>
                        
                        <p>O copia y pega este enlace en tu navegador:</p>
                        <p style="word-break: break-all; background-color: #f0f0f0; padding: 10px; border-radius: 5px;">
                            %s
                        </p>
                        
                        <div class="warning">
                            <strong>⚠️ Importante:</strong>
                            <ul>
                                <li>Este enlace expirará en <strong>1 hora</strong></li>
                                <li>Solo puedes usar este enlace una vez</li>
                                <li>Si no solicitaste este cambio, ignora este correo</li>
                            </ul>
                        </div>
                        
                        <div class="footer">
                            <p>Este es un correo automático, por favor no respondas.</p>
                            <p>&copy; 2024 %s. Todos los derechos reservados.</p>
                        </div>
                    </div>
                </body>
                </html>
            """.formatted(
                    EnvConfig.getAppName(),
                    resetLink,
                    resetLink,
                    EnvConfig.getAppName()
            );

            message.setContent(htmlContent, "text/html; charset=utf-8");

            Transport.send(message);

            System.out.println("✅ Email de recuperación enviado a: " + toEmail);

        } catch (MessagingException e) {
            System.err.println("❌ Error al enviar email: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("No se pudo enviar el email de recuperación", e);
        }
    }
}