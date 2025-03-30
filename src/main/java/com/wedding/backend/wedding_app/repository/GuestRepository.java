package com.wedding.backend.wedding_app.repository;

import com.wedding.backend.wedding_app.entity.Guest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface GuestRepository extends JpaRepository<Guest, Long> {
    Optional<Guest> findByFirstNameAndLastName(String firstName, String lastName);
    List<Guest> findByLastName(String lastName);
}
