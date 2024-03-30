package ru.practicum.shareit.booking;

import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class BookingServiceImplTest {

    private BookingService bookingService;

    @Mock
    BookingRepository bookingRepository;

    @Mock
    ItemRepository itemRepository;

    @Mock
    UserRepository userRepository;

    private BookingMapperImpl bookingMapper;

    private final EasyRandom generator = new EasyRandom();

    @BeforeEach void setUp() {
        bookingService = new BookingServiceImpl(bookingRepository, itemRepository, userRepository,
                bookingMapper);
    }

    @Test
    void shouldNotFindBookingById() {
        User user = new User("user@ya.ru", "User1");
        assertThrows(NotFoundException.class, () ->  bookingService.getBookingById(user.getId(), 0L));
    }

    @Test
    void shouldNotSaveBookingWithoutItem() {
        BookingDto booking = generator.nextObject(BookingDto.class);
        assertThrows(NotFoundException.class, () ->  bookingService.saveBooking(0L, booking));
    }

 /*   @Test
    void shouldNotSaveBookingWithoutItemAvailable() {
        BookingDto bookingDto = generator.nextObject(BookingDto.class);
        Item item = generator.nextObject(Item.class);
        item.setAvailable(false);
        when(itemRepository.save(Mockito.any(Item.class))).thenReturn(item);
        Booking booking = bookingMapper.toBooking(bookingDto);
        booking.setItem(item);
        when(bookingRepository.save(Mockito.any(Booking.class))).thenReturn(booking);
        assertThrows(ValidationException.class, () ->  bookingService.saveBooking(0L, bookingDto));
    }

  /*  public BookingResponse saveBooking(Long userId, BookingDto bookingDto) {
        Item item = itemRepository.findById(bookingDto.getItemId()).orElseThrow(() ->
                new NotFoundException("Товар не найден"));
        isAvailable(item);
        User user = userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException("Пользователь не найден"));
        checkBookingTime(bookingDto);
        checkOwner(userId, item);
        Booking booking = bookingMapper.toBooking(bookingDto);
        booking.setItem(item);
        booking.setBooker(user);
        booking.setStatus(Status.WAITING);
        bookingRepository.save(booking);
        log.info("Добавлено новое бронирование");
        return bookingMapper.toBookingResponse(booking);

   */
}