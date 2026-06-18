package com.ebenezer.modules.prayer;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record EncouragementCreateRequest(
        @NotBlank(message = "A mensagem não pode estar em branco")
        @Size(max = 1000, message = "A mensagem deve ter no máximo 1000 caracteres")
        String message
) {}
