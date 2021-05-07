package com.deviget.minesweeperapi.controller;

import com.deviget.minesweeperapi.controller.response.ApiErrorResponse;
import com.deviget.minesweeperapi.exceptions.CellNotFoundException;
import com.deviget.minesweeperapi.exceptions.GameAlreadyFinishedException;
import com.deviget.minesweeperapi.exceptions.GameNotActiveException;
import com.deviget.minesweeperapi.exceptions.GameNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@Slf4j
@RestControllerAdvice
public class RestApiExceptionHandler {

    @ExceptionHandler(CellNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ApiErrorResponse> handleCellNotFoundException(CellNotFoundException ex) {
        log.error(ex.getMessage());
        ApiErrorResponse errorResponse = ApiErrorResponse.builder().message(ex.getMessage())
                .status(HttpStatus.NOT_FOUND).timestamp(LocalDateTime.now()).build();
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(GameAlreadyFinishedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ApiErrorResponse> handleGameFinishedException(GameAlreadyFinishedException ex) {
        log.error(ex.getMessage());
        ApiErrorResponse errorResponse = ApiErrorResponse.builder().message(ex.getMessage())
                .status(HttpStatus.BAD_REQUEST).timestamp(LocalDateTime.now()).build();
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(GameNotActiveException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ApiErrorResponse> handleGameNotActiveException(GameNotActiveException ex) {
        log.error(ex.getMessage());
        ApiErrorResponse errorResponse = ApiErrorResponse.builder().message(ex.getMessage())
                .status(HttpStatus.BAD_REQUEST).timestamp(LocalDateTime.now()).build();
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(GameNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ApiErrorResponse> handleGameNotFoundException(GameNotFoundException ex) {
        log.error(ex.getMessage());
        ApiErrorResponse errorResponse = ApiErrorResponse.builder().message(ex.getMessage())
                .status(HttpStatus.NOT_FOUND).timestamp(LocalDateTime.now()).build();
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ApiErrorResponse> handleRuntimeException(RuntimeException ex) {
        log.error("An unexpected exception occurred because of {}", ex.getMessage());
        ApiErrorResponse errorResponse = ApiErrorResponse.builder().message(ex.getMessage())
                .status(HttpStatus.INTERNAL_SERVER_ERROR).timestamp(LocalDateTime.now()).build();
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
