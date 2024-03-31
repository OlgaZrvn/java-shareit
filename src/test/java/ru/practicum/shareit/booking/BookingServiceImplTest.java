package ru.practicum.shareit.booking;

import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
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
import java.util.List;
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

    @Mock
    BookingMapper bookingMapper;

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

    @Test
    void shouldSaveNewBooking() {
        BookingDto bookingDto = generator.nextObject(BookingDto.class);
        Item item = generator.nextObject(Item.class);
        item.setAvailable(true);
        when(itemRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(item));
        when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(user));
        bookingDto.setStart(LocalDateTime.now().plusHours(8));
        bookingDto.setEnd(LocalDateTime.now().plusHours(9));

        Booking booking = new Booking(
                bookingDto.getId(),
                bookingDto.getStart(),
                bookingDto.getEnd(),
                item,
                user,
                Status.WAITING
        );

        when(bookingMapper.toBooking(Mockito.any(BookingDto.class))).thenReturn(booking);
        when(bookingRepository.save(Mockito.any(Booking.class))).thenReturn(booking);

        BookingResponse bookingResponse = new BookingResponse(
                booking.getId(),
                booking.getStart(),
                booking.getEnd(),
                booking.getItem(),
                booking.getBooker(),
                booking.getStatus());
        when(bookingMapper.toBookingResponse(Mockito.any(Booking.class))).thenReturn(bookingResponse);
        BookingResponse savedBooking = bookingService.saveBooking(user.getId(), bookingDto);
        assertEquals(bookingResponse.getId(), savedBooking.getId());
    }

    @Test
    void shouldNotUpdateBookingWithoutBooking() {
        BookingDto booking = generator.nextObject(BookingDto.class);
        assertThrows(NotFoundException.class, () ->
                bookingService.updateBooking(user.getId(), booking.getId(), true));
    }

    @Test
    void shouldNotUpdateBookingWithUserNotOwner() {
        Booking booking = generator.nextObject(Booking.class);
        when(bookingRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(booking));
        assertThrows(NotFoundException.class, () ->
                bookingService.updateBooking(user.getId(), booking.getId(), true));
    }

    @Test
    void shouldNotUpdateBookingWithStatusApproved() {
        Booking booking = generator.nextObject(Booking.class);
        when(bookingRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(booking));
        booking.getItem().setOwner(user);
        booking.setStatus(Status.APPROVED);
        assertThrows(ValidationException.class, () ->
                bookingService.updateBooking(user.getId(), booking.getId(), true));
    }

    @Test
    void shouldUpdateBooking() {
        Booking booking = generator.nextObject(Booking.class);
        when(bookingRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(booking));
        booking.getItem().setOwner(user);
        when(bookingRepository.save(Mockito.any(Booking.class))).thenReturn(booking);
        BookingResponse bookingResponse = new BookingResponse(
                booking.getId(),
                booking.getStart(),
                booking.getEnd(),
                booking.getItem(),
                booking.getBooker(),
                Status.APPROVED);
        when(bookingMapper.toBookingResponse(Mockito.any(Booking.class))).thenReturn(bookingResponse);
        BookingResponse updatedBooking = bookingService.updateBooking(user.getId(), booking.getId(), true);
        assertEquals(bookingResponse, updatedBooking);
    }

    @Test
    void shouldUpdateBookingWithApprovedFalse() {
        Booking booking = generator.nextObject(Booking.class);
        when(bookingRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(booking));
        booking.getItem().setOwner(user);
        when(bookingRepository.save(Mockito.any(Booking.class))).thenReturn(booking);
        BookingResponse bookingResponse = new BookingResponse(
                booking.getId(),
                booking.getStart(),
                booking.getEnd(),
                booking.getItem(),
                booking.getBooker(),
                Status.REJECTED);
        when(bookingMapper.toBookingResponse(Mockito.any(Booking.class))).thenReturn(bookingResponse);
        BookingResponse updatedBooking = bookingService.updateBooking(user.getId(), booking.getId(), false);
        assertEquals(bookingResponse, updatedBooking);
    }

    @Test
    void shouldNotGetAllBookingWithNegativeFrom() {
        assertThrows(ValidationException.class, () ->
                bookingService.getAllBookings(user.getId(), State.ALL, -1, 1));
    }

    @Test
    void shouldNotGetAllBookingWithNegativeSize() {
        assertThrows(ValidationException.class, () ->
                bookingService.getAllBookings(user.getId(), State.ALL, 0, -1));
    }

    @Test
    void shouldNotGetAllBookingWithoutUser() {
        assertThrows(NotFoundException.class, () ->
                bookingService.getAllBookings(user.getId(), State.ALL, 0, 10));
    }

    @Test
    void shouldGetAllBookingWithStatusAll() {
        when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(user));
        Page<Booking> bookingPage = new PageImpl<>(List.of(
                generator.nextObject(Booking.class),
                generator.nextObject(Booking.class)));
        when(bookingRepository.findAllByBookerIdOrderByStartDesc(Mockito.anyLong(), Mockito.any()))
                .thenReturn(bookingPage);
        List<BookingResponse> bookingList = bookingService.getAllBookings(user.getId(), State.ALL, 0, 10);
        assertEquals(2, bookingList.size());
    }

    @Test
    void shouldGetAllBookingWithStatusCurrent() {
        when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(user));
        Page<Booking> bookingPage = new PageImpl<>(List.of(
                generator.nextObject(Booking.class),
                generator.nextObject(Booking.class)));
        when(bookingRepository.findAllByBookerIdAndStartBeforeAndEndAfterOrderByStartDesc(
                Mockito.anyLong(),
                Mockito.any(LocalDateTime.class),
                Mockito.any(LocalDateTime.class),
                Mockito.any()))
                .thenReturn(bookingPage);
        List<BookingResponse> bookingList = bookingService.getAllBookings(user.getId(), State.CURRENT, 0, 10);
        assertEquals(2, bookingList.size());
    }

    @Test
    void shouldGetAllBookingWithStatusPast() {
        when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(user));
        Page<Booking> bookingPage = new PageImpl<>(List.of(
                generator.nextObject(Booking.class),
                generator.nextObject(Booking.class)));
        when(bookingRepository.findAllByBookerIdAndEndBeforeOrderByStartDesc(
                Mockito.anyLong(),
                Mockito.any(LocalDateTime.class),
                Mockito.any()))
                .thenReturn(bookingPage);
        List<BookingResponse> bookingList = bookingService.getAllBookings(user.getId(), State.PAST, 0, 10);
        assertEquals(2, bookingList.size());
    }

    @Test
    void shouldGetAllBookingWithStatusFuture() {
        when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(user));
        Page<Booking> bookingPage = new PageImpl<>(List.of(
                generator.nextObject(Booking.class),
                generator.nextObject(Booking.class)));
        when(bookingRepository.findAllByBookerIdAndStartAfterOrderByStartDesc(
                Mockito.anyLong(),
                Mockito.any(LocalDateTime.class),
                Mockito.any()))
                .thenReturn(bookingPage);
        List<BookingResponse> bookingList = bookingService.getAllBookings(user.getId(), State.FUTURE, 0, 10);
        assertEquals(2, bookingList.size());
    }

    @Test
    void shouldGetAllBookingWithStatusWaiting() {
        when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(user));
        Page<Booking> bookingPage = new PageImpl<>(List.of(
                generator.nextObject(Booking.class),
                generator.nextObject(Booking.class)));
        when(bookingRepository.findAllByBookerIdAndStatusOrderByStartDesc(
                Mockito.anyLong(),
                Mockito.any(Status.class),
                Mockito.any()))
                .thenReturn(bookingPage);
        List<BookingResponse> bookingList = bookingService.getAllBookings(user.getId(), State.WAITING, 0, 10);
        assertEquals(2, bookingList.size());
    }

    @Test
    void shouldGetAllBookingWithStatusRejected() {
        when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(user));
        Page<Booking> bookingPage = new PageImpl<>(List.of(
                generator.nextObject(Booking.class),
                generator.nextObject(Booking.class)));
        when(bookingRepository.findAllByBookerIdAndStatusOrderByStartDesc(
                Mockito.anyLong(),
                Mockito.any(Status.class),
                Mockito.any()))
                .thenReturn(bookingPage);
        List<BookingResponse> bookingList = bookingService.getAllBookings(user.getId(), State.REJECTED, 0, 10);
        assertEquals(2, bookingList.size());
    }

    @Test
    void shouldGetAllBookingWithStatusUnknown() {
        when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(user));
        assertThrows(ValidationException.class, () ->
                bookingService.getAllBookings(user.getId(), State.UNSUPPORTED_STATUS, 0, 10));
    }

    @Test
    void shouldNotGetAllBookingByOwnerWithNegativeFrom() {
        assertThrows(ValidationException.class, () ->
                bookingService.getAllBookingByItemOwner(user.getId(), State.ALL, -1, 1));
    }

    @Test
    void shouldNotGetAllBookingByOwnerWithNegativeSize() {
        assertThrows(ValidationException.class, () ->
                bookingService.getAllBookingByItemOwner(user.getId(), State.ALL, 0, -1));
    }

    @Test
    void shouldNotGetAllBookingByOwnerWithoutUser() {
        assertThrows(NotFoundException.class, () ->
                bookingService.getAllBookingByItemOwner(user.getId(), State.ALL, 0, 10));
    }

    @Test
    void shouldGetAllBookingByOwnerWithStatusAll() {
        when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(user));
        Page<Booking> bookingPage = new PageImpl<>(List.of(
                generator.nextObject(Booking.class),
                generator.nextObject(Booking.class)));
        when(bookingRepository.findAllByItemOwnerOrderByStartDesc(Mockito.any(User.class), Mockito.any()))
                .thenReturn(bookingPage);
        List<BookingResponse> bookingList = bookingService.getAllBookingByItemOwner(user.getId(), State.ALL, 0, 10);
        assertEquals(2, bookingList.size());
    }

    @Test
    void shouldGetAllBookingByOwnerWithStateCurrent() {
        when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(user));
        Page<Booking> bookingPage = new PageImpl<>(List.of(
                generator.nextObject(Booking.class),
                generator.nextObject(Booking.class)));
        when(bookingRepository.findAllByItemOwnerAndStartBeforeAndEndAfterOrderByStartDesc(
                Mockito.any(User.class),
                Mockito.any(LocalDateTime.class),
                Mockito.any(LocalDateTime.class),
                Mockito.any()))
                .thenReturn(bookingPage);
        List<BookingResponse> bookingList = bookingService.getAllBookingByItemOwner(user.getId(), State.CURRENT, 0, 10);
        assertEquals(2, bookingList.size());
    }

    @Test
    void shouldGetAllBookingByOwnerWithStatePast() {
        when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(user));
        Page<Booking> bookingPage = new PageImpl<>(List.of(
                generator.nextObject(Booking.class),
                generator.nextObject(Booking.class)));
        when(bookingRepository.findAllByItemOwnerAndEndBeforeOrderByStartDesc(
                Mockito.any(User.class),
                Mockito.any(LocalDateTime.class),
                Mockito.any()))
                .thenReturn(bookingPage);
        List<BookingResponse> bookingList = bookingService.getAllBookingByItemOwner(user.getId(), State.PAST, 0, 10);
        assertEquals(2, bookingList.size());
    }

    @Test
    void shouldGetAllBookingByOwnerWithStateFuture() {
        when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(user));
        Page<Booking> bookingPage = new PageImpl<>(List.of(
                generator.nextObject(Booking.class),
                generator.nextObject(Booking.class)));
        when(bookingRepository.findAllByItemOwnerAndStartAfterOrderByStartDesc(
                Mockito.any(User.class),
                Mockito.any(LocalDateTime.class),
                Mockito.any()))
                .thenReturn(bookingPage);
        List<BookingResponse> bookingList =
                bookingService.getAllBookingByItemOwner(user.getId(), State.FUTURE, 0, 10);
        assertEquals(2, bookingList.size());
    }

    @Test
    void shouldGetAllBookingByOwnerWithStateWaiting() {
        when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(user));
        Page<Booking> bookingPage = new PageImpl<>(List.of(
                generator.nextObject(Booking.class),
                generator.nextObject(Booking.class)));
        when(bookingRepository.findAllByItemOwnerAndStatusOrderByStartDesc(
                Mockito.any(User.class),
                Mockito.any(Status.class),
                Mockito.any()))
                .thenReturn(bookingPage);
        List<BookingResponse> bookingList =
                bookingService.getAllBookingByItemOwner(user.getId(), State.WAITING, 0, 10);
        assertEquals(2, bookingList.size());
    }

    @Test
    void shouldGetAllBookingByOwnerWithStateRejected() {
        when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(user));
        Page<Booking> bookingPage = new PageImpl<>(List.of(
                generator.nextObject(Booking.class),
                generator.nextObject(Booking.class)));
        when(bookingRepository.findAllByItemOwnerAndStatusOrderByStartDesc(
                Mockito.any(User.class),
                Mockito.any(Status.class),
                Mockito.any()))
                .thenReturn(bookingPage);
        List<BookingResponse> bookingList =
                bookingService.getAllBookingByItemOwner(user.getId(), State.REJECTED, 0, 10);
        assertEquals(2, bookingList.size());
    }

    @Test
    void shouldGetAllBookingByOwnerWithStatusUnknown() {
        when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(user));
        assertThrows(ValidationException.class, () ->
                bookingService.getAllBookingByItemOwner(user.getId(), State.UNSUPPORTED_STATUS, 0, 10));
    }
}