package com.yogo.metacraft.mapdata.exception;


import com.yogo.metacraft.common.CustomApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InvalidSortParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<CustomApiResponse<Void>> handleInvalidSortParameterException(InvalidSortParameterException e) {
        return ResponseEntity
                .badRequest()
                .body(new CustomApiResponse<>(false, "Invalid sort parameters: " + e.getMessage()));
    }

    @ExceptionHandler(MapDataException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<CustomApiResponse<Void>> handleMapDataException(MapDataException e) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new CustomApiResponse<>(false, "MapData exception: " + e.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<CustomApiResponse<Void>> handleGlobalException(Exception e) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new CustomApiResponse<>(false, "An unexpected error occurredL: " + e.getMessage()));
    }


}
