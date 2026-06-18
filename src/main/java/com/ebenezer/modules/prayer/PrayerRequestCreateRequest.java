package com.ebenezer.modules.prayer;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record PrayerRequestCreateRequest(

        @NotBlank(message = "Título é obrigatório")
        @Size(max = 255, message = "Título deve ter no máximo 255 caracteres")
        String title,

        @NotBlank(message = "Descrição é obrigatória")
        String description
) {}
