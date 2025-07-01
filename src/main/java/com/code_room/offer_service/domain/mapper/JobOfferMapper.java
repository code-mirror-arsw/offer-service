package com.code_room.offer_service.domain.mapper;

import com.code_room.offer_service.domain.model.OfferJob;
import com.code_room.offer_service.infrastructure.controller.dto.OfferJobDto;
import com.code_room.offer_service.infrastructure.repository.entities.JobOfferEntity;

public interface JobOfferMapper {
    OfferJob toDomain(JobOfferEntity entity);

    JobOfferEntity toEntity(OfferJob domain);

    OfferJob toDomain(OfferJobDto dto);

    OfferJobDto toDto(OfferJob domain);

    JobOfferEntity toEntity(OfferJobDto dto);

    OfferJobDto toDto(JobOfferEntity entity);
}
