package com.wedding.backend.wedding_app.repository;

import com.wedding.backend.wedding_app.entity.GuestEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface GuestRepository extends JpaRepository<GuestEntity, Long> {
    Optional<GuestEntity> findByFirstNameAndLastName(String firstName, String lastName);
    Optional<GuestEntity> findByFirstNameIgnoreCaseAndLastNameIgnoreCase(String firstName, String lastName);
    List<GuestEntity> findByLastName(String lastName);
}
