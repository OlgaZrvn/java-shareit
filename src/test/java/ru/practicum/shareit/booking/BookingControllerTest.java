package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingResponse;
import ru.practicum.shareit.booking.model.Booking;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = BookingController.class)
@AutoConfigureMockMvc
class BookingControllerTest {

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private BookingServiceImpl bookingService;

    @Autowired
    private MockMvc mvc;

    private final BookingMapper bookingMapper = new BookingMapperImpl();

    private final EasyRandom generator = new EasyRandom();

    @Test
    public void shouldSaveNewBooking() throws Exception {
        mapper = new ObjectMapper().registerModule(new JavaTimeModule());
        BookingResponse bookingResponse = generator.nextObject(BookingResponse.class);
        when(bookingService.saveBooking(Mockito.anyLong(), Mockito.any(BookingDto.class)))
                .thenReturn(bookingResponse);

        Booking booking = bookingMapper.toBooking(bookingResponse);
        BookingDto bookingDto = bookingMapper.toBookingDto(booking);
        bookingDto.setItemId(bookingResponse.getItem().getId());
        mvc.perform(post("/bookings")
                .content(mapper.writeValueAsString(bookingDto))
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header("X-Sharer-User-Id", 0))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(bookingResponse.getId()))
                .andExpect(jsonPath("$.booker").value(bookingResponse.getBooker()));
    }

    @Test
    public void shouldNotSaveNewBookingWithoutHeader() throws Exception {
        mapper = new ObjectMapper().registerModule(new JavaTimeModule());
        BookingResponse bookingResponse = generator.nextObject(BookingResponse.class);
        when(bookingService.saveBooking(Mockito.anyLong(), Mockito.any(BookingDto.class)))
                .thenReturn(bookingResponse);

        Booking booking = bookingMapper.toBooking(bookingResponse);
        BookingDto bookingDto = bookingMapper.toBookingDto(booking);
        bookingDto.setItemId(bookingResponse.getItem().getId());
        mvc.perform(post("/bookings")
                        .content(mapper.writeValueAsString(bookingDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldUpdateBooking() throws Exception {
        mapper = new ObjectMapper().registerModule(new JavaTimeModule());
        BookingResponse bookingResponse = generator.nextObject(BookingResponse.class);
        bookingResponse.setId(0L);
        when(bookingService.updateBooking(Mockito.anyLong(), Mockito.anyLong(), Mockito.any(Boolean.class)))
                .thenReturn(bookingResponse);

        mvc.perform(patch("/bookings/0")
                        .param("approved", "true")
                        .header("X-Sharer-User-Id", 0))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(bookingResponse.getId()))
                .andExpect(jsonPath("$.booker").value(bookingResponse.getBooker()));
    }

    @Test
    public void shouldUpdateBookingWithApprovedFalse() throws Exception {
        mapper = new ObjectMapper().registerModule(new JavaTimeModule());
        BookingResponse bookingResponse = generator.nextObject(BookingResponse.class);
        bookingResponse.setId(0L);
        when(bookingService.updateBooking(Mockito.anyLong(), Mockito.anyLong(), Mockito.any(Boolean.class)))
                .thenReturn(bookingResponse);

        mvc.perform(patch("/bookings/0")
                        .param("approved", "false")
                        .header("X-Sharer-User-Id", 0))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(bookingResponse.getId()))
                .andExpect(jsonPath("$.booker").value(bookingResponse.getBooker()));
    }

    @Test
    public void shouldGetBookingById() throws Exception {
        mapper = new ObjectMapper().registerModule(new JavaTimeModule());
        BookingResponse bookingResponse = generator.nextObject(BookingResponse.class);
        bookingResponse.setId(0L);
        when(bookingService.getBookingById(Mockito.anyLong(), Mockito.anyLong()))
                .thenReturn(bookingResponse);

        mvc.perform(get("/bookings/0")
                        .header("X-Sharer-User-Id", 0))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(bookingResponse.getId()))
                .andExpect(jsonPath("$.booker").value(bookingResponse.getBooker()));
    }

    @Test
    public void shouldGetAllBookings() throws Exception {
        mapper = new ObjectMapper().registerModule(new JavaTimeModule());
        List<BookingResponse> bookingResponses = List.of(
                generator.nextObject(BookingResponse.class),
                generator.nextObject(BookingResponse.class));

        when(bookingService.getAllBookings(Mockito.anyLong(), Mockito.any(State.class), Mockito.anyInt(), Mockito.anyInt()))
                .thenReturn(bookingResponses);

        mvc.perform(get("/bookings")
                        .param("state", "ALL")
                        .param("from", "0")
                        .param("size", "20")
                        .header("X-Sharer-User-Id", 0))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldGetAllBookingsByItemOwner() throws Exception {
        mapper = new ObjectMapper().registerModule(new JavaTimeModule());
        List<BookingResponse> bookingResponses = List.of(
                generator.nextObject(BookingResponse.class),
                generator.nextObject(BookingResponse.class));

        when(bookingService.getAllBookingByItemOwner(Mockito.anyLong(), Mockito.any(State.class),
                Mockito.anyInt(), Mockito.anyInt()))
                .thenReturn(bookingResponses);

        mvc.perform(get("/bookings/owner")
                        .param("state", "ALL")
                        .param("from", "0")
                        .param("size", "20")
                        .header("X-Sharer-User-Id", 0))
                .andExpect(status().isOk());
    }
}

