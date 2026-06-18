package com.ebenezer.modules.psychology;

import com.ebenezer.modules.user.repository.UserRepository;
import com.ebenezer.shared.enums.Role;
import com.ebenezer.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PsychologyService {

    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public Page<PsychologistResponse> findAll(Pageable pageable) {
        return userRepository.findAllByRole(Role.PSYCHOLOGIST, pageable)
                .map(PsychologistResponse::from);
    }

    @Transactional(readOnly = true)
    public PsychologistResponse findById(Long id) {
        return userRepository.findById(id)
                .filter(u -> u.getRole() == Role.PSYCHOLOGIST)
                .map(PsychologistResponse::from)
                .orElseThrow(() -> new ResourceNotFoundException("Psicólogo não encontrado"));
    }
}
