package com.ebenezer.modules.prayer;

import com.ebenezer.modules.prayer.service.EncouragementService;
import com.ebenezer.modules.prayer.service.PrayerRequestService;
import com.ebenezer.modules.prayer.service.PrayerService;
import com.ebenezer.shared.enums.PrayerRequestStatus;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/prayer-requests")
@RequiredArgsConstructor
public class PrayerRequestController {

    private final PrayerRequestService prayerRequestService;
    private final PrayerService prayerService;
    private final EncouragementService encouragementService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('MISSIONARY')")
    public PrayerRequestResponse create(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody PrayerRequestCreateRequest request) {
        return prayerRequestService.create(userDetails.getUsername(), request);
    }

    @GetMapping
    public Page<PrayerRequestResponse> findAll(
            @RequestParam(required = false) PrayerRequestStatus status,
            @PageableDefault(size = 10, sort = "createdAt") Pageable pageable) {
        return prayerRequestService.findAll(status, pageable);
    }

    @GetMapping("/my")
    public Page<PrayerRequestResponse> findMine(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(required = false) PrayerRequestStatus status,
            @PageableDefault(size = 10, sort = "createdAt") Pageable pageable) {
        return prayerRequestService.findByCurrentUser(userDetails.getUsername(), status, pageable);
    }

    @GetMapping("/{id}")
    public PrayerRequestResponse findById(@PathVariable Long id) {
        return prayerRequestService.findById(id);
    }

    @PutMapping("/{id}")
    public PrayerRequestResponse update(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody PrayerRequestCreateRequest request) {
        return prayerRequestService.update(id, userDetails.getUsername(), request);
    }

    @PatchMapping("/{id}/status")
    public PrayerRequestResponse updateStatus(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody PrayerRequestStatusUpdateRequest request) {
        return prayerRequestService.updateStatus(id, userDetails.getUsername(), request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        prayerRequestService.delete(id, userDetails.getUsername());
    }

    @PostMapping("/{id}/pray")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void pray(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        prayerService.pray(id, userDetails.getUsername());
    }

    @DeleteMapping("/{id}/pray")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void unpray(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        prayerService.unpray(id, userDetails.getUsername());
    }

    @PostMapping("/{id}/encouragements")
    @ResponseStatus(HttpStatus.CREATED)
    public EncouragementResponse sendEncouragement(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody EncouragementCreateRequest request) {
        return encouragementService.send(id, userDetails.getUsername(), request);
    }

    @GetMapping("/{id}/encouragements")
    public List<EncouragementResponse> listEncouragements(@PathVariable Long id) {
        return encouragementService.list(id);
    }
}
