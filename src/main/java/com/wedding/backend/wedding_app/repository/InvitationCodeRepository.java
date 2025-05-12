package com.wedding.backend.wedding_app.repository;

import com.wedding.backend.wedding_app.entity.InvitationCodeEntity;
import com.wedding.backend.wedding_app.entity.GuestEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InvitationCodeRepository extends JpaRepository<InvitationCodeEntity, Long> {
    
    Optional<InvitationCodeEntity> findByCode(String code);
    
    List<InvitationCodeEntity> findByGuest(GuestEntity guest);
    
    List<InvitationCodeEntity> findByGuestAndCodeType(GuestEntity guest, String codeType);
    
    List<InvitationCodeEntity> findByUsed(Boolean used);
}