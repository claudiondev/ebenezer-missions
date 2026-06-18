package com.ebenezer.modules.auth;

import com.ebenezer.shared.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record
RegisterRequest(

        @NotBlank(message = "Nome é obrigatório")
        String name,

        @Email(message = "E-mail inválido")
        @NotBlank(message = "E-mail é obrigatório")
        String email,

        @NotBlank(message = "Senha é obrigatória")
        @Size(min = 8, message = "Senha deve ter no mínimo 8 caracteres")
        String password,

        @NotNull(message = "Papel é obrigatório")
        Role role,

        String field,
        String country,
        String bio,

        // campos exclusivos de psicólogos
        String phone,
        String specialty,
        String approach,
        java.math.BigDecimal pricePerSession
) {}
