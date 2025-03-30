package com.wedding.backend.wedding_app.service;

import com.wedding.backend.wedding_app.dao.GuestDao;
import com.wedding.backend.wedding_app.entity.Guest;
import com.wedding.model.request.GuestRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class GuestService {

    private final GuestDao guestDao;

    public Guest addGuest(String firstName, String lastName, String email, String phone, boolean plusOneAllowed) {
        return guestDao.saveGuest(firstName, lastName, email, phone, plusOneAllowed);
    }

    public Optional<Guest> findGuestByName(String firstName, String lastName) {
        return guestDao.fetchGuestByFullName(firstName, lastName);
    }

    public List<Guest> getAllGuests() {
        return guestDao.fetchAllGuests();
    }

    public Guest updateGuest(GuestRequest request) {
        Optional<Guest> existingGuest = findGuestByName(request.getFirstName(), request.getLastName());

        if (existingGuest.isEmpty()) {
            log.error("Guest does not exist");
            throw new RuntimeException("Guest not exist exception");
        }

        Guest guest = existingGuest.get();
        guest.setEmail(request.getEmail());
        guest.setPhone(request.getPhone());
        guest.setPlusOneAllowed(request.isPlusOneAllowed());

        return guestDao.updateGuest(guest);
    }

    public void removeGuest(Long id) {
        guestDao.deleteGuest(id);
    }
}