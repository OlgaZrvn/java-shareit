package ru.practicum.shareit.booking;

import org.mapstruct.Mapper;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingResponse;
import ru.practicum.shareit.booking.model.Booking;

@Mapper(componentModel = "spring")
public interface BookingMapper {

    Booking toBooking(BookingDto bookingDto);

    BookingDto toBookingDto(Booking booking);

    Booking toBooking(BookingResponse bookingResponse);

    BookingResponse toBookingResponse(Booking booking);
}
