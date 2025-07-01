package com.code_room.offer_service.domain.usecases;

import com.code_room.offer_service.domain.enums.JobOfferStatus;
import com.code_room.offer_service.domain.mapper.JobOfferMapper;
import com.code_room.offer_service.domain.ports.JobOfferService;
import com.code_room.offer_service.infrastructure.controller.dto.InterviewEventDto;
import com.code_room.offer_service.infrastructure.controller.dto.OfferJobDto;
import com.code_room.offer_service.infrastructure.messaging.KafkaProducer;
import com.code_room.offer_service.infrastructure.repository.JobOfferRepository;
import com.code_room.offer_service.infrastructure.repository.entities.JobOfferEntity;
import com.code_room.offer_service.infrastructure.restclient.UserApiService;

import com.code_room.offer_service.infrastructure.restclient.dto.UserDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import retrofit2.Response;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class JobOfferServiceImpl implements JobOfferService {
    @Autowired
    JobOfferRepository repository;
    @Autowired
    JobOfferMapper mapper;
    @Autowired
    KafkaProducer kafkaProducer;
    @Autowired
    UserApiService userApiService;
    @Value("${kafka.notify-topic.name}")
    private String topic;

    @Override
    public OfferJobDto createJobOffer(OfferJobDto dto) {
        JobOfferEntity entity = mapper.toEntity(dto);
        repository.save(entity);
        return dto;
    }

    @Override
    public Page<OfferJobDto> getAllJobOffers(int page) {
        Pageable pageable = PageRequest.of(page, 6);
        return repository.findAll(pageable)
                .map(mapper::toDto);
    }

    @Override
    public Page<OfferJobDto> newOffers(int page) {
        Pageable pageable = PageRequest.of(page, 6);
        return repository.findActive(pageable)
                .map(mapper::toDto);
    }

    @Override
    public Page<OfferJobDto> getByAdminEmail(String email, int page) {
        Pageable pageable = PageRequest.of(page, 6);
        return repository.findByAdminEmail(email, pageable)
                .map(mapper::toDto);
    }

    @Override
    public Page<OfferJobDto> getByClientId(String clientId,int page) {
        Pageable pageable = PageRequest.of(page, 6);
        return repository.findByParticipantId(clientId,pageable)
                .map(mapper::toDto);
    }


    @Override
    public OfferJobDto getJobOfferById(String id) {
        return repository.findById(id)
                .map(mapper::toDto)
                .orElseThrow(() -> new RuntimeException("Offer not found"));
    }

    @Override
    public OfferJobDto updateJobOffer(String id, OfferJobDto dto) {
        JobOfferEntity existing = repository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Job offer not found with ID: " + id));

        existing.setCompany(dto.getCompany());
        existing.setTitle(dto.getTitle());
        existing.setDescription(dto.getDescription());
        existing.setStatus(dto.getStatus());
        existing.setExpiresAt(LocalDateTime.now().plusDays(30));

        JobOfferEntity updated = repository.save(existing);
        return mapper.toDto(updated);
    }

    @Override
    public void deleteJobOffer(String id) {
        repository.deleteById(id);
    }

    @Override
    public OfferJobDto addParticipant(String offerId, String intervieweeId) throws IOException {
        JobOfferEntity offer = repository.findById(offerId)
                .orElseThrow(() -> new NoSuchElementException("Job offer not found"));

        if (offer.getParticipants().size() >= offer.getMaxCandidates()) {
            throw new IllegalStateException("Maximum number of participants reached");
        }

        if (offer.getParticipants().contains(intervieweeId)) {
            throw new IllegalArgumentException("Participant already added");
        }

        Response<UserDto> response = userApiService.findById(intervieweeId)
                        .execute();

        if (!response.isSuccessful() || response.body() == null) {
            String errorMsg = response.errorBody() != null ? response.errorBody().string() : "Empty error body";
            throw new RuntimeException("Error calling external API: " + errorMsg);
        }

        offer.getParticipants().add(response.body().getEmail());

        if (offer.getParticipants().size() == offer.getMaxCandidates()){
            String message = buildKafkaMessage(offer.getAdminEmail(),offerId,offer.getParticipants(),offer.getDescription());
            kafkaProducer.sendMessage(topic,message);
            offer.setStatus(JobOfferStatus.CLOSED);
        }
        repository.save(offer);
        return mapper.toDto(offer);
    }

    private String buildKafkaMessage(String adminEmail, String offerId, List<String> acceptedUsers,String description) {
        try {
            System.out.println(description);
            InterviewEventDto eventDto = InterviewEventDto.builder()
                    .adminEmail(adminEmail)
                    .offerId(offerId)
                    .description(description)
                    .acceptedUsers(acceptedUsers)
                    .build();

            ObjectMapper objectMapper = new ObjectMapper();

            return objectMapper.writeValueAsString(eventDto);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to convert InterviewEventDto to JSON string", e);
        }
    }


}
