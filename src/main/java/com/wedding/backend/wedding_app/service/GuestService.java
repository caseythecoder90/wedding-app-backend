package com.wedding.backend.wedding_app.service;

import com.wedding.backend.wedding_app.dao.GuestDao;
import com.wedding.backend.wedding_app.entity.GuestEntity;
import com.wedding.model.request.GuestRequest;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class GuestService {

    private final GuestDao guestDao;

    public GuestEntity addGuest(String firstName, String lastName, String email, String phone, boolean plusOneAllowed) {
        return guestDao.saveGuest(firstName, lastName, email, phone, plusOneAllowed);
    }

    public GuestEntity findGuestByName(String firstName, String lastName) {
        Optional<GuestEntity> guest = guestDao.fetchGuestByFullName(firstName, lastName);
        if (guest.isPresent()) {
            return guest.get();
        }
        throw new EntityNotFoundException("entity not found");
    }

    public List<GuestEntity> getAllGuests() {
        return guestDao.fetchAllGuests();
    }

    public GuestEntity updateGuest(GuestRequest request) {

        GuestEntity existingGuest =
                findGuestByName(request.getFirstName(), request.getLastName());

        existingGuest.setEmail(request.getEmail());
        existingGuest.setPhone(request.getPhone());
        existingGuest.setPlusOneAllowed(request.isPlusOneAllowed());

        return guestDao.updateGuest(existingGuest);
    }

    public void removeGuest(Long id) {
        guestDao.deleteGuest(id);
    }
}