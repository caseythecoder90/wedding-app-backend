package com.wedding.backend.wedding_app.repository;

import com.wedding.backend.wedding_app.entity.RSVP;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RSVPRespository extends JpaRepository<RSVP, Long> {
}
