package com.ebenezer.modules.psychology;

import com.ebenezer.modules.user.model.UserEntity;

import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public record PsychologistResponse(
        Long id,
        String name,
        String bio,
        String specialty,
        String approach,
        BigDecimal pricePerSession,
        String whatsappLink
) {
    private static final String WHATSAPP_MESSAGE =
            "Olá! Vi seu perfil no portal Ebenezer Missions e gostaria de saber mais sobre seu atendimento. Poderia me ajudar?";

    public static PsychologistResponse from(UserEntity u) {
        String link = null;
        if (u.getPhone() != null && !u.getPhone().isBlank()) {
            String phone = u.getPhone().replaceAll("[^0-9]", "");
            if (!phone.startsWith("55")) phone = "55" + phone;
            String encoded = URLEncoder.encode(WHATSAPP_MESSAGE, StandardCharsets.UTF_8);
            link = "https://wa.me/" + phone + "?text=" + encoded;
        }
        return new PsychologistResponse(
                u.getId(),
                u.getName(),
                u.getBio(),
                u.getSpecialty(),
                u.getApproach(),
                u.getPricePerSession(),
                link
        );
    }
}
