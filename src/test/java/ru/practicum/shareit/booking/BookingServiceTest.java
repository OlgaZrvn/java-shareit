package ru.practicum.shareit.booking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class BookingServiceTest {

    private BookingService bookingService;

    @Mock
    BookingRepository bookingRepository;

    @Mock
    ItemRepository itemRepository;

    @Mock
    UserRepository userRepository;

    @BeforeEach void setUp() {
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