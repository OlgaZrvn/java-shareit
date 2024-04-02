package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.ItemRequestRepository;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.data.domain.Sort.Direction.DESC;

@DataJpaTest
class BookingRepositoryTest {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ItemRequestRepository itemRequestRepository;

    @Test
    void testFindOneBookingByBookerId() {
        PageRequest page = PageRequest.of(0, 10, Sort.by(DESC, "start"));
        User user = new User("user@ya.ru", "User1");
        userRepository.save(user);
        Item item = new Item("Item1", "Desc1", true, user);
        itemRepository.save(item);
        Booking booking = new Booking(0L, LocalDateTime.now().plusHours(2), LocalDateTime.now().plusHours(3),
                item, user);
        booking.setStatus(Status.WAITING);
        bookingRepository.save(booking);
        List<Booking> bookings = bookingRepository.findAllByBookerIdOrderByStartDesc(user.getId(), page).toList();
        assertEquals(1, bookings.size());
    }
}