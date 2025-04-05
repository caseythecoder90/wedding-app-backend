package com.wedding.backend.wedding_app.service;

import com.wedding.backend.wedding_app.dao.GuestDao;
import com.wedding.backend.wedding_app.entity.Guest;
import com.wedding.model.request.GuestRequest;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.action.internal.EntityActionVetoException;
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

    public Guest findGuestByName(String firstName, String lastName) {
        Optional<Guest> guest = guestDao.fetchGuestByFullName(firstName, lastName);
        if (guest.isPresent()) {
            return guest.get();
        }
        throw new EntityNotFoundException("entity not found");
    }

    public List<Guest> getAllGuests() {
        return guestDao.fetchAllGuests();
    }

    public Guest updateGuest(GuestRequest request) {

        Guest existingGuest =
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