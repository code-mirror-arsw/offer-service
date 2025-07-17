package com.code_room.offer_service.infrastructure.controller.dto;

import com.code_room.offer_service.domain.enums.JobOfferStatus;
import jakarta.persistence.Column;
import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class OfferJobDto {
    private String id;

    private String company;

    private String title;

    private String description;

    private JobOfferStatus status;

    private int maxCandidates;

    private String language;

    private String adminEmail;

}
