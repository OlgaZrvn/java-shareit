package ru.practicum.shareit.booking;
/*
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
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

import java.nio.charset.StandardCharsets;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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

    private final EasyRandom generator = new EasyRandom();

    @Test
    public void test() throws Exception {
/*
        BookingResponse booking = generator.nextObject(BookingResponse.class);
        booking.setId(0L);
        when(bookingService.saveBooking(Mockito.anyLong(), Mockito.any(BookingDto.class)))
                .thenAnswer(invocationOnMock -> {
                    BookingDto bookingDto = invocationOnMock.getArgument(1, BookingDto.class);
                    return bookingDto;
                });
        mvc.perform(post("/bookings")
                .content(mapper.writeValueAsString(booking))
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header("X-Sharer-User-Id", 0))

                .andExpect(status().isOk());
               /* .andExpect(jsonPath("$.id", is(1), Long.class))
                .andExpect(jsonPath("$.start").value(booking.getStart()))
                .andExpect(jsonPath("$.end").value(booking.getEnd()))
                .andExpect(jsonPath("$.item").value(booking.getItem()))
                .andExpect(jsonPath("$.booker").value(booking.getBooker()))
                .andExpect(jsonPath("$.status").value(booking.getStatus()));

                */

