package com.ebenezer.modules.prayer.service;

import com.ebenezer.modules.prayer.EncouragementCreateRequest;
import com.ebenezer.modules.prayer.EncouragementResponse;
import com.ebenezer.modules.prayer.model.Encouragement;
import com.ebenezer.modules.prayer.repository.EncouragementRepository;
import com.ebenezer.modules.prayer.repository.PrayerRequestRepository;
import com.ebenezer.modules.user.service.UserService;
import com.ebenezer.shared.email.EmailService;
import com.ebenezer.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EncouragementService {

    private final EncouragementRepository encouragementRepository;
    private final PrayerRequestRepository prayerRequestRepository;
    private final UserService userService;
    private final EmailService emailService;

    @Transactional
    public EncouragementResponse send(Long requestId, String userEmail, EncouragementCreateRequest body) {
        var user = userService.findByEmail(userEmail);
        var prayerRequest = prayerRequestRepository.findById(requestId)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido de oração não encontrado"));

        var encouragement = encouragementRepository.save(Encouragement.builder()
                .user(user)
                .prayerRequest(prayerRequest)
                .message(body.message())
                .build());

        emailService.sendEncouragementNotification(encouragement);
        return EncouragementResponse.from(encouragement);
    }

    public List<EncouragementResponse> list(Long requestId) {
        var prayerRequest = prayerRequestRepository.findById(requestId)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido de oração não encontrado"));

        return encouragementRepository.findAllByPrayerRequest(prayerRequest).stream()
                .map(EncouragementResponse::from)
                .toList();
    }
}
