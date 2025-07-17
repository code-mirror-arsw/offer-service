package com.code_room.offer_service.domain.mapper;

import com.code_room.offer_service.domain.enums.JobOfferStatus;
import com.code_room.offer_service.domain.model.OfferJob;
import com.code_room.offer_service.infrastructure.controller.dto.OfferJobDto;
import com.code_room.offer_service.infrastructure.repository.entities.JobOfferEntity;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;
@Component
public class JobOfferMapperImpl implements JobOfferMapper {

    @Override
    public OfferJob toDomain(JobOfferEntity entity) {
        return OfferJob.builder()
                .company(entity.getCompany())
                .title(entity.getTitle())
                .language(entity.getLanguage())
                .description(entity.getDescription())
                .status(entity.getStatus())
                .maxCandidates(entity.getMaxCandidates())
                .adminEmail(entity.getAdminEmail())
                .build();
    }

    @Override
    public JobOfferEntity toEntity(OfferJob domain) {
        return JobOfferEntity.builder()
                .id(UUID.randomUUID().toString())
                .company(domain.getCompany())
                .title(domain.getTitle())
                .language(domain.getLanguage())
                .description(domain.getDescription())
                .status(domain.getStatus() != null ? domain.getStatus() : JobOfferStatus.ACTIVE)
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusDays(30))
                .maxCandidates(domain.getMaxCandidates())
                .adminEmail(domain.getAdminEmail())
                .build();
    }

    @Override
    public OfferJob toDomain(OfferJobDto dto) {
        return OfferJob.builder()
                .company(dto.getCompany())
                .title(dto.getTitle())
                .language(dto.getLanguage())
                .description(dto.getDescription())
                .status(dto.getStatus())
                .maxCandidates(dto.getMaxCandidates())
                .adminEmail(dto.getAdminEmail())
                .build();
    }

    @Override
    public OfferJobDto toDto(OfferJob domain) {
        return OfferJobDto.builder()
                .id(domain.getId())
                .language(domain.getLanguage())
                .company(domain.getCompany())
                .title(domain.getTitle())
                .description(domain.getDescription())
                .status(domain.getStatus())
                .maxCandidates(domain.getMaxCandidates())
                .adminEmail(domain.getAdminEmail())
                .build();
    }

    @Override
    public JobOfferEntity toEntity(OfferJobDto dto) {
        return JobOfferEntity.builder()
                .id(UUID.randomUUID().toString())
                .company(dto.getCompany())
                .title(dto.getTitle())
                .language(dto.getLanguage())
                .description(dto.getDescription())
                .status(dto.getStatus() != null ? dto.getStatus() : JobOfferStatus.ACTIVE)
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusDays(30))
                .maxCandidates(dto.getMaxCandidates())
                .adminEmail(dto.getAdminEmail())
                .build();
    }

    @Override
    public OfferJobDto toDto(JobOfferEntity entity) {
        return OfferJobDto.builder()
                .id(entity.getId())
                .language(entity.getLanguage())
                .company(entity.getCompany())
                .title(entity.getTitle())
                .description(entity.getDescription())
                .status(entity.getStatus())
                .maxCandidates(entity.getMaxCandidates())
                .adminEmail(entity.getAdminEmail())
                .build();
    }
}
