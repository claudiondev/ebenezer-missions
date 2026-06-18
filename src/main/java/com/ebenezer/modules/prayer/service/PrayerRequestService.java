package com.ebenezer.modules.prayer.service;

import com.ebenezer.modules.prayer.PrayerRequestCreateRequest;
import com.ebenezer.modules.prayer.PrayerRequestResponse;
import com.ebenezer.modules.prayer.PrayerRequestStatusUpdateRequest;
import com.ebenezer.modules.prayer.model.PrayerRequest;
import com.ebenezer.modules.prayer.repository.PrayerRequestRepository;
import com.ebenezer.modules.user.service.UserService;
import com.ebenezer.shared.enums.Role;
import com.ebenezer.shared.enums.PrayerRequestStatus;
import com.ebenezer.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PrayerRequestService {

    private final PrayerRequestRepository prayerRequestRepository;
    private final UserService userService;

    @Transactional
    public PrayerRequestResponse create(String email, PrayerRequestCreateRequest request) {
        var user = userService.findByEmail(email);
        var pr = PrayerRequest.builder()
                .user(user)
                .title(request.title())
                .description(request.description())
                .build();
        return PrayerRequestResponse.from(prayerRequestRepository.save(pr));
    }

    @Transactional(readOnly = true)
    public Page<PrayerRequestResponse> findAll(PrayerRequestStatus status, Pageable pageable) {
        var page = status != null
                ? prayerRequestRepository.findAllByStatus(status, pageable)
                : prayerRequestRepository.findAll(pageable);
        return page.map(PrayerRequestResponse::from);
    }

    @Transactional(readOnly = true)
    public PrayerRequestResponse findById(Long id) {
        return PrayerRequestResponse.from(findOrThrow(id));
    }

    @Transactional(readOnly = true)
    public Page<PrayerRequestResponse> findByCurrentUser(String email, PrayerRequestStatus status, Pageable pageable) {
        var user = userService.findByEmail(email);
        var page = status != null
                ? prayerRequestRepository.findAllByUserAndStatus(user, status, pageable)
                : prayerRequestRepository.findAllByUser(user, pageable);
        return page.map(PrayerRequestResponse::from);
    }

    @Transactional
    public PrayerRequestResponse update(Long id, String email, PrayerRequestCreateRequest request) {
        var pr = findOrThrow(id);
        checkOwner(pr, email);
        pr.setTitle(request.title());
        pr.setDescription(request.description());
        return PrayerRequestResponse.from(prayerRequestRepository.save(pr));
    }

    @Transactional
    public PrayerRequestResponse updateStatus(Long id, String email, PrayerRequestStatusUpdateRequest request) {
        var pr = findOrThrow(id);
        checkOwner(pr, email);
        pr.setStatus(request.status());
        return PrayerRequestResponse.from(prayerRequestRepository.save(pr));
    }

    @Transactional
    public void delete(Long id, String email) {
        var pr = findOrThrow(id);
        var user = userService.findByEmail(email);
        if (user.getRole() != Role.ADMIN && !pr.getUser().getEmail().equals(email)) {
            throw new AccessDeniedException("Sem permissão para excluir este pedido");
        }
        prayerRequestRepository.delete(pr);
    }

    private PrayerRequest findOrThrow(Long id) {
        return prayerRequestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido de oração não encontrado"));
    }

    private void checkOwner(PrayerRequest pr, String email) {
        if (!pr.getUser().getEmail().equals(email)) {
            throw new AccessDeniedException("Sem permissão para modificar este pedido");
        }
    }
}
