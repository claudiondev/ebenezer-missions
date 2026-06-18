package com.ebenezer.modules.psychology;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/psychologists")
@RequiredArgsConstructor
public class PsychologistController {

    private final PsychologyService psychologyService;

    @GetMapping
    public Page<PsychologistResponse> findAll(
            @PageableDefault(size = 10, sort = "name") Pageable pageable) {
        return psychologyService.findAll(pageable);
    }

    @GetMapping("/{id}")
    public PsychologistResponse findById(@PathVariable Long id) {
        return psychologyService.findById(id);
    }
}
