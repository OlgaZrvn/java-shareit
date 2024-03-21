package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingResponse;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/bookings")
public class BookingController {

    private final BookingServiceImpl bookingService;

    @PostMapping
    public ResponseEntity<BookingResponse> createBooking(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                         @Valid @RequestBody BookingDto bookingDto,
                                                         BindingResult bindingResult) {
        return ResponseEntity.ok().body(bookingService.saveBooking(userId, bookingDto, bindingResult));
    }

    @PatchMapping("{bookingId}")
    public ResponseEntity<BookingResponse> updateBooking(@RequestHeader("X-Sharer-User-Id") Long userId,
                                        @PathVariable("bookingId") Long bookingId,
                                        @RequestParam String approved) {
        return ResponseEntity.ok().body(bookingService.updateBooking(userId, bookingId, approved));
    }

    @GetMapping("{bookingId}")
    public ResponseEntity<BookingResponse> getBookingById(@RequestHeader("X-Sharer-User-Id") Long userId,
                                      @PathVariable("bookingId") Long bookingId) {
        return ResponseEntity.ok().body(bookingService.getBookingById(userId, bookingId));
    }

    @GetMapping
    public ResponseEntity<List<BookingResponse>> getAllBookings(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                @RequestParam(defaultValue = "ALL") State state) {
        return ResponseEntity.ok().body(bookingService.getAllBookings(userId, state));
    }

    @GetMapping("/owner")
    public ResponseEntity<List<BookingResponse>> getAllBookingByItemOwner(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                          @RequestParam(defaultValue = "ALL") State state) {
        return ResponseEntity.ok().body(bookingService.getAllBookingByItemOwner(userId, state));
    }
}