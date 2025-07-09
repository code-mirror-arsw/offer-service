package com.code_room.offer_service.domain.ports;

import com.code_room.offer_service.infrastructure.controller.dto.OfferJobDto;
import org.springframework.data.domain.Page;

import java.io.IOException;
import java.util.List;

public interface JobOfferService {
    OfferJobDto createJobOffer(OfferJobDto dto);


    Page<OfferJobDto> getAllJobOffers(int page);

    Page<OfferJobDto> newOffers(int page);

    Page<OfferJobDto> getByAdminEmail(String email, int page);

    Page<OfferJobDto> getByClientId(String clientId,int page);

    OfferJobDto getJobOfferById(String id);

    OfferJobDto updateJobOffer(String id, OfferJobDto dto);

    void deleteJobOffer(String id);

    OfferJobDto addParticipant(String offerId, String intervieweeId,String authHeader) throws IOException;
}
