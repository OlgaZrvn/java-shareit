package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class BookingDto {
    private Long id;
  //  @NotNull(message = "Время начала бронирования не может быть пустым")
  //  @FutureOrPresent(message = "Время начала бронирования не может быть в прошлом")
    private LocalDateTime start;
 //   @NotNull(message = "Время окончания бронирования не может быть пустым")
 //   @Future(message = "Время окончания бронирования не может быть в прошлом")
    private LocalDateTime end;
    @NotNull
    private Long itemId;
}
