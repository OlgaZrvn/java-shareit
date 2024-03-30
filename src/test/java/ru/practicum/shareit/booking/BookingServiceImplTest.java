package ru.practicum.shareit.booking;

import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingResponse;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookingServiceImplTest {

    private BookingService bookingService;

    @Mock
    BookingRepository bookingRepository;

    @Mock
    ItemRepository itemRepository;

    @Mock
    UserRepository userRepository;

    private final BookingMapper bookingMapper = new BookingMapperImpl();

    private final EasyRandom generator = new EasyRandom();

    private final User user = new User(0L, "User", "user@ya.ru");

    @BeforeEach void setUp() {
        bookingService = new BookingServiceImpl(bookingRepository, itemRepository, userRepository,
                bookingMapper);
    }

    @Test
    void shouldNotFindBookingByIdWithoutBooking() {
        assertThrows(NotFoundException.class, () ->  bookingService.getBookingById(user.getId(), 0L));
    }

    @Test
    void shouldNotFindBookingByIdWithUserNotOwner() {
        Booking booking = generator.nextObject(Booking.class);
        when(bookingRepository.findById(Mockito.anyLong())).thenReturn(Optional.ofNullable(booking));
        assertThrows(NotFoundException.class, () ->  bookingService.getBookingById(user.getId(), booking.getId()));
    }

    @Test
    void shouldFindBookingById() {
        Booking booking = generator.nextObject(Booking.class);
        booking.getItem().setOwner(user);
        when(bookingRepository.findById(Mockito.anyLong())).thenReturn(Optional.ofNullable(booking));
        BookingResponse returnedBooking = bookingService.getBookingById(user.getId(), booking.getId());
        BookingResponse bookingResponse = bookingMapper.toBookingResponse(booking);
        assertEquals(bookingResponse, returnedBooking);
    }

    @Test
    void shouldNotSaveBookingWithoutItem() {
        BookingDto booking = generator.nextObject(BookingDto.class);
        assertThrows(NotFoundException.class, () ->  bookingService.saveBooking(0L, booking));
    }

    @Test
    void shouldNotSaveBookingWithItemNotAvailable() {
        BookingDto bookingDto = generator.nextObject(BookingDto.class);
        Item item = generator.nextObject(Item.class);
        item.setAvailable(false);
        when(itemRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(item));
        assertThrows(ValidationException.class, () ->  bookingService.saveBooking(0L, bookingDto));
    }

    @Test
    void shouldNotSaveBookingWithoutUser() {
        BookingDto bookingDto = generator.nextObject(BookingDto.class);
        Item item = generator.nextObject(Item.class);
        item.setAvailable(true);
        when(itemRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(item));
        assertThrows(NotFoundException.class, () ->  bookingService.saveBooking(0L, bookingDto));
    }

    @Test
    void shouldNotSaveBookingWithBookingStartNull() {
        BookingDto bookingDto = generator.nextObject(BookingDto.class);
        Item item = generator.nextObject(Item.class);
        item.setAvailable(true);
        when(itemRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(item));
        when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(user));
        bookingDto.setStart(null);
        assertThrows(ValidationException.class, () ->  bookingService.saveBooking(0L, bookingDto));
    }

    @Test
    void shouldNotSaveBookingWithBookingEndNull() {
        BookingDto bookingDto = generator.nextObject(BookingDto.class);
        Item item = generator.nextObject(Item.class);
        item.setAvailable(true);
        when(itemRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(item));
        when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(user));
        bookingDto.setEnd(null);
        assertThrows(ValidationException.class, () ->  bookingService.saveBooking(0L, bookingDto));
    }

    @Test
    void shouldNotSaveBookingWithBookingStartInPast() {
        BookingDto bookingDto = generator.nextObject(BookingDto.class);
        Item item = generator.nextObject(Item.class);
        item.setAvailable(true);
        when(itemRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(item));
        when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(user));
        bookingDto.setStart(LocalDateTime.now().minusHours(9));
        assertThrows(ValidationException.class, () ->  bookingService.saveBooking(0L, bookingDto));
    }

    @Test
    void shouldNotSaveBookingWithBookingEndInPast() {
        BookingDto bookingDto = generator.nextObject(BookingDto.class);
        Item item = generator.nextObject(Item.class);
        item.setAvailable(true);
        when(itemRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(item));
        when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(user));
        bookingDto.setEnd(LocalDateTime.now().minusHours(9));
        assertThrows(ValidationException.class, () ->  bookingService.saveBooking(0L, bookingDto));
    }

    @Test
    void shouldNotSaveBookingWithBookingEndEqualsBookingStart() {
        BookingDto bookingDto = generator.nextObject(BookingDto.class);
        Item item = generator.nextObject(Item.class);
        item.setAvailable(true);
        when(itemRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(item));
        when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(user));
        bookingDto.setStart(LocalDateTime.now().plusHours(9));
        bookingDto.setEnd(bookingDto.getStart());
        assertThrows(ValidationException.class, () ->  bookingService.saveBooking(0L, bookingDto));
    }

    @Test
    void shouldNotSaveBookingWithBookingEndBeforeBookingStart() {
        BookingDto bookingDto = generator.nextObject(BookingDto.class);
        Item item = generator.nextObject(Item.class);
        item.setAvailable(true);
        when(itemRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(item));
        when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(user));
        bookingDto.setStart(LocalDateTime.now().plusHours(10));
        bookingDto.setEnd(LocalDateTime.now().plusHours(9));
        assertThrows(ValidationException.class, () ->  bookingService.saveBooking(0L, bookingDto));
    }

    @Test
    void shouldNotSaveBookingByItemOwner() {
        BookingDto bookingDto = generator.nextObject(BookingDto.class);
        Item item = generator.nextObject(Item.class);
        item.setAvailable(true);
        item.setOwner(user);
        when(itemRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(item));
        when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(user));
        bookingDto.setStart(LocalDateTime.now().plusHours(8));
        bookingDto.setEnd(LocalDateTime.now().plusHours(9));
        assertThrows(NotFoundException.class, () ->  bookingService.saveBooking(0L, bookingDto));
    }

 /*   @Test
    void shouldSaveNewBooking() {
        BookingDto bookingDto = generator.nextObject(BookingDto.class);
        Item item = generator.nextObject(Item.class);
        item.setAvailable(true);
        when(itemRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(item));
        when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(user));
        bookingDto.setStart(LocalDateTime.now().plusHours(8));
        bookingDto.setEnd(LocalDateTime.now().plusHours(9));
        Booking booking = bookingMapper.toBooking(bookingDto);
        booking.setItem(item);
        booking.setBooker(user);
        booking.setStatus(Status.WAITING);
        when(bookingRepository.save(booking)).thenReturn(booking);
        BookingResponse bookingResponse = bookingMapper.toBookingResponse(booking);
        BookingResponse savedBooking = bookingService.saveBooking(0L, bookingDto);
        assertEquals(bookingResponse, savedBooking);
    }

  */
}