package com.deviget.minesweeperapi.controller.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiErrorResponse {

    private HttpStatus status;
    private LocalDateTime timestamp;
    private String message;
}
