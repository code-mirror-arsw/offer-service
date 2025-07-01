package com.code_room.offer_service.infrastructure.repository;

import com.code_room.offer_service.infrastructure.repository.entities.JobOfferEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface JobOfferRepository extends JpaRepository<JobOfferEntity,String> {

    Page<JobOfferEntity> findByAdminEmail(String email, Pageable pageable);

    @Query("""
           SELECT j
           FROM JobOfferEntity j
           JOIN j.participants p
           WHERE p = :participantId
           """)
    Page<JobOfferEntity> findByParticipantId(@Param("participantId") String participantId,Pageable pageable);

    @Query("""
       SELECT j
       FROM JobOfferEntity j
       WHERE j.status = com.code_room.offer_service.domain.enums.JobOfferStatus.ACTIVE
       """)
    Page<JobOfferEntity> findActive(Pageable pageable);

}
