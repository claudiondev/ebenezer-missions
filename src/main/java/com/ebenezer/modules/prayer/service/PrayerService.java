package com.ebenezer.modules.prayer.service;

import com.ebenezer.modules.prayer.model.Prayer;
import com.ebenezer.modules.prayer.model.PrayerRequest;
import com.ebenezer.modules.prayer.repository.PrayerRepository;
import com.ebenezer.modules.prayer.repository.PrayerRequestRepository;
import com.ebenezer.modules.user.model.UserEntity;
import com.ebenezer.modules.user.service.UserService;
import com.ebenezer.shared.email.EmailService;
import com.ebenezer.shared.exception.BusinessException;
import com.ebenezer.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PrayerService {

    private final PrayerRepository prayerRepository;
    private final PrayerRequestRepository prayerRequestRepository;
    private final UserService userService;
    private final EmailService emailService;

    @Transactional
    public void pray(Long requestId, String userEmail) {
        var user = userService.findByEmail(userEmail);
        var prayerRequest = findRequest(requestId);

        if (prayerRepository.existsByUserAndPrayerRequest(user, prayerRequest)) {
            throw new BusinessException("Você já orou por este pedido");
        }

        var prayer = prayerRepository.save(Prayer.builder()
                .user(user)
                .prayerRequest(prayerRequest)
                .build());

        prayerRequestRepository.incrementPrayerCount(requestId);
        emailService.sendPrayerNotification(prayer);
    }

    @Transactional
    public void unpray(Long requestId, String userEmail) {
        var user = userService.findByEmail(userEmail);
        var prayerRequest = findRequest(requestId);

        if (!prayerRepository.existsByUserAndPrayerRequest(user, prayerRequest)) {
            throw new BusinessException("Você ainda não orou por este pedido");
        }

        prayerRepository.deleteByUserAndPrayerRequest(user, prayerRequest);
        prayerRequestRepository.decrementPrayerCount(requestId);
    }

    private PrayerRequest findRequest(Long id) {
        return prayerRequestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido de oração não encontrado"));
    }
}
