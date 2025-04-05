package com.wedding.backend.wedding_app.repository;

import com.wedding.backend.wedding_app.entity.RSVPEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RSVPRespository extends JpaRepository<RSVPEntity, Long> {
}
