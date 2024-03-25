package ru.practicum.shareit.booking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import ru.practicum.shareit.booking.dto.BookingResponse;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.data.domain.Sort.Direction.DESC;

@ExtendWith(MockitoExtension.class)
class BookingServiceTest {

    private BookingService bookingService;

    @Mock
    BookingRepository bookingRepository;

    @Mock
    ItemRepository itemRepository;

    @Mock
    UserRepository userRepository;

    @BeforeEach void SetUp() {
        bookingService = new BookingServiceImpl(bookingRepository, itemRepository, userRepository,
                new BookingMapperImpl());
    }

    @Test
    void testNotFindBookingById() {
        User user = new User("user@ya.ru", "User1");
        userRepository.save(user);
        assertThrows(NotFoundException.class, () ->  bookingService.getBookingById(user.getId(), 0L));
    }

    @Test
    void testFindOneBookingById() {

    }
}