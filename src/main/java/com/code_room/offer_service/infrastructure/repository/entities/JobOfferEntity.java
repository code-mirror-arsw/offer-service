package com.code_room.offer_service.infrastructure.repository.entities;
import com.code_room.offer_service.domain.enums.JobOfferStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "job_offers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JobOfferEntity {
    @Id
    @Column(columnDefinition = "VARCHAR(36)", nullable = false)
    private String id;

    @Column(name = "company", nullable = false)
    private String company;

    @Column(nullable = false)
    private String title;

    @Column(length = 2000)
    private String description;

    private int maxCandidates;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "expires_at")
    private LocalDateTime expiresAt;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private JobOfferStatus status;

    @Column(name = "admin_email")
    private String adminEmail;

    private String language;


    @ElementCollection
    @CollectionTable(name = "job_offer_participants", joinColumns = @JoinColumn(name = "job_offer_id"))
    @Column(name = "participant_id")
    private List<String> participants = new ArrayList<>();


    @PrePersist
    public void generateId() {
        if (this.id == null) {
            this.id = UUID.randomUUID().toString();
        }
    }
}




