package com.ebenezer.modules.user;

import com.ebenezer.modules.prayer.PrayerRequestResponse;
import com.ebenezer.modules.prayer.repository.PrayerRequestRepository;
import com.ebenezer.modules.user.model.UserEntity;
import com.ebenezer.modules.user.repository.UserRepository;
import com.ebenezer.shared.enums.PrayerRequestStatus;
import com.ebenezer.shared.enums.Role;
import com.ebenezer.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/missionaries")
@RequiredArgsConstructor
public class MissionaryController {

    private final UserRepository userRepository;
    private final PrayerRequestRepository prayerRequestRepository;

    @GetMapping
    public Page<MissionaryResponse> findAll(
            @PageableDefault(size = 10, sort = "createdAt") Pageable pageable) {
        return userRepository.findAllByRole(Role.MISSIONARY, pageable)
                .map(MissionaryResponse::from);
    }

    @GetMapping("/{id}")
    public MissionaryProfileResponse findById(@PathVariable Long id) {
        UserEntity missionary = userRepository.findById(id)
                .filter(u -> u.getRole() == Role.MISSIONARY)
                .orElseThrow(() -> new ResourceNotFoundException("Missionário não encontrado"));

        List<PrayerRequestResponse> openRequests = prayerRequestRepository
                .findAllByUserAndStatus(missionary, PrayerRequestStatus.OPEN, Pageable.unpaged())
                .stream()
                .map(PrayerRequestResponse::from)
                .toList();

        return MissionaryProfileResponse.from(missionary, openRequests);
    }
}
