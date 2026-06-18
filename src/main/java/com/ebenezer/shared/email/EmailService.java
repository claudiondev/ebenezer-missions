package com.ebenezer.shared.email;

import com.ebenezer.modules.prayer.model.Encouragement;
import com.ebenezer.modules.prayer.model.Prayer;
import com.ebenezer.modules.prayer.model.PrayerRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username:}")
    private String mailUsername;

    private boolean isEmailEnabled() {
        return mailUsername != null && !mailUsername.isBlank();
    }

    @Async
    public void sendPrayerNotification(Prayer prayer) {
        if (!isEmailEnabled()) return;
        var request = prayer.getPrayerRequest();
        var intercessor = prayer.getUser();
        var missionary = request.getUser();
        try {
            var message = new SimpleMailMessage();
            message.setTo(missionary.getEmail());
            message.setSubject("Alguém orou pelo seu pedido!");
            message.setText(String.format(
                    "Olá, %s!%n%n%s orou pelo seu pedido \"%s\".%n%nContinuemos firmes na oração! 🙏",
                    missionary.getName(),
                    intercessor.getName(),
                    request.getTitle()
            ));
            mailSender.send(message);
        } catch (Exception e) {
            log.warn("Falha ao enviar e-mail de oração para {}: {}", missionary.getEmail(), e.getMessage());
        }
    }

    @Async
    public void sendEncouragementNotification(Encouragement encouragement) {
        if (!isEmailEnabled()) return;
        var request = encouragement.getPrayerRequest();
        var missionary = request.getUser();
        try {
            var message = new SimpleMailMessage();
            message.setTo(missionary.getEmail());
            message.setSubject("Você recebeu um encorajamento!");
            message.setText(String.format(
                    "Olá, %s!%n%n%s te enviou um encorajamento:%n%n\"%s\"%n%nDeus abençoe o seu ministério! 🙏",
                    missionary.getName(),
                    encouragement.getUser().getName(),
                    encouragement.getMessage()
            ));
            mailSender.send(message);
        } catch (Exception e) {
            log.warn("Falha ao enviar e-mail de encorajamento para {}: {}", missionary.getEmail(), e.getMessage());
        }
    }
}
