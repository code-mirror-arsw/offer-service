package com.code_room.offer_service.infrastructure.controller;


import com.code_room.offer_service.domain.ports.JobOfferService;
import com.code_room.offer_service.infrastructure.controller.dto.OfferJobDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/offers")
public class JobOfferController {

    @Autowired
    private JobOfferService jobOfferService;

    @PostMapping
    public ResponseEntity<?> create(@RequestBody OfferJobDto dto) {
        try {
            OfferJobDto created = jobOfferService.createJobOffer(dto);
            return ResponseEntity.ok(created);
        } catch (Exception e) {
            return errorResponse("Failed to create job offer", e);
        }
    }

    @GetMapping("/newOffers")
    public ResponseEntity<?> getAll(@RequestParam(defaultValue = "0") int page) {
        try {
            Page<OfferJobDto> offers = jobOfferService.newOffers(page);
            return ResponseEntity.ok(offers);
        } catch (Exception e) {
            return errorResponse("Failed to fetch job offers", e);
        }
    }
    @GetMapping("/clientEmail/{clientEmail}")
    public ResponseEntity<?> getByclientEmail(@PathVariable String clientEmail,
                                              @RequestParam(defaultValue = "0") int page) {
        try {
            Page<OfferJobDto> offers = jobOfferService.getByClientId(clientEmail,page);
            return ResponseEntity.ok(offers);
        } catch (Exception e) {
            return errorResponse("Failed to fetch job offers", e);
        }
    }


    @GetMapping("/email/{email}")
    public ResponseEntity<?> getByAdminEmail(@PathVariable String email,
                                             @RequestParam(defaultValue = "0") int page) {
        try {
            Page<OfferJobDto> offers = jobOfferService.getByAdminEmail(email,page);
            return ResponseEntity.ok(offers);
        } catch (Exception e) {
            return errorResponse("Failed to fetch job offers", e);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable String id) {
        try {
            OfferJobDto offer = jobOfferService.getJobOfferById(id);
            return ResponseEntity.ok(offer);
        } catch (Exception e) {
            return errorResponse("Offer not found with ID: " + id, e);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable String id, @RequestBody OfferJobDto dto) {
        try {
            OfferJobDto updated = jobOfferService.updateJobOffer(id, dto);
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            return errorResponse("Failed to update offer with ID: " + id, e);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable String id) {
        try {
            jobOfferService.deleteJobOffer(id);
            Map<String, String> success = new HashMap<>();
            success.put("message", "Offer deleted successfully");
            return ResponseEntity.ok(success);
        } catch (Exception e) {
            return errorResponse("Failed to delete offer with ID: " + id, e);
        }
    }

    @PostMapping("/{offerId}/participants")
    public ResponseEntity<?> addParticipant(
            @PathVariable("offerId") String offerId,
            @RequestParam String intervieweeId,
            @RequestHeader("Authorization") String authHeader) {

        try {
            OfferJobDto updated = jobOfferService.addParticipant(offerId, intervieweeId,authHeader);
            return ResponseEntity.ok(updated);
        } catch (NoSuchElementException | IllegalStateException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("message", "Unexpected error" + e.getMessage()));
        }
    }


    private ResponseEntity<Map<String, String>> errorResponse(String msg, Exception e) {
        Map<String, String> error = new HashMap<>();
        error.put("message", msg);
        error.put("error", e.getMessage());
        return ResponseEntity.badRequest().body(error);
    }
}
