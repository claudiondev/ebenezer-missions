package com.ebenezer.modules.prayer;

import com.ebenezer.shared.enums.PrayerRequestStatus;
import jakarta.validation.constraints.NotNull;

public record PrayerRequestStatusUpdateRequest(

        @NotNull(message = "Status é obrigatório")
        PrayerRequestStatus status
) {}
