package com.ebenezer.modules.prayer;

import com.ebenezer.modules.prayer.model.Encouragement;

import java.time.LocalDateTime;

public record EncouragementResponse(
        Long id,
        String authorName,
        String message,
        LocalDateTime sentAt
) {
    public static EncouragementResponse from(Encouragement e) {
        return new EncouragementResponse(e.getId(), e.getUser().getName(), e.getMessage(), e.getSentAt());
    }
}
