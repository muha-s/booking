package com.gmail.muha.booking.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ApiError> handleNotFoundException(NotFoundException exception, HttpServletRequest request) {
        return buildErrorResponse(exception.getMessage(), request, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BookingUpdateException.class)
    public ResponseEntity<ApiError> handleBookingUpdateException(
            BookingUpdateException exception, HttpServletRequest request) {
        return buildErrorResponse(exception.getMessage(), request, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(BookingCancellationException.class)
    public ResponseEntity<ApiError> handleBookingCancellationException(
            BookingCancellationException exception, HttpServletRequest request) {
        return buildErrorResponse(exception.getMessage(), request, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(RoomsException.class)
    public ResponseEntity<ApiError> handleRoomsException(RoomsException exception, HttpServletRequest request) {
        return buildErrorResponse(exception.getMessage(), request, HttpStatus.UNPROCESSABLE_CONTENT);
    }

    @ExceptionHandler(InsufficientAmountOfMoneyInAccountException.class)
    public ResponseEntity<ApiError> handleInsufficientAmountOfMoneyInAccountException(
            InsufficientAmountOfMoneyInAccountException exception, HttpServletRequest request) {
        return buildErrorResponse(exception.getMessage(), request, HttpStatus.UNPROCESSABLE_CONTENT);
    }

    @ExceptionHandler(WrongBookingDateException.class)
    public ResponseEntity<ApiError> handleWrongBookingDateException(
            WrongBookingDateException exception, HttpServletRequest request) {
        return buildErrorResponse(exception.getMessage(), request, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidationException(
            MethodArgumentNotValidException exception, HttpServletRequest request) {

        String message = exception.getBindingResult().getFieldErrors().getFirst().getDefaultMessage();
        return buildErrorResponse(message, request, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiError> handleBadCredentialsException(HttpServletRequest request) {
        return buildErrorResponse("Invalid email or password", request, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ApiError> handleUserAlreadyExistsException(
            UserAlreadyExistsException exception, HttpServletRequest request) {
        return buildErrorResponse(exception.getMessage(), request, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiError> handleIllegalArgumentException(
            IllegalArgumentException exception,
            HttpServletRequest request) {
        return buildErrorResponse(exception.getMessage(), request, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ApiError> handleIllegalStateException(
            IllegalStateException exception,
            HttpServletRequest request) {
        return buildErrorResponse(exception.getMessage(), request, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DisabledException.class)
    public ResponseEntity<ApiError> handleDisabledException(DisabledException exception, HttpServletRequest request) {
        return buildErrorResponse("Email is not verified", request, HttpStatus.UNAUTHORIZED
        );
    }

    @ExceptionHandler(HotelReviewException.class)
    public ResponseEntity<ApiError> handleHotelReviewException(
            HotelReviewException exception,
            HttpServletRequest request) {

        return buildErrorResponse(exception.getMessage(), request, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AuthenticationRoleException.class)
    public ResponseEntity<ApiError> handleAuthenticationRoleException(
            AuthenticationRoleException exception, HttpServletRequest request) {

        return buildErrorResponse(
                exception.getMessage(),
                request,
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    @ExceptionHandler(HotelAdminActivationException.class)
    public ResponseEntity<ApiError> handleHotelAdminActivationException(
            HotelAdminActivationException exception, HttpServletRequest request) {

        return buildErrorResponse(
                exception.getMessage(),
                request,
                HttpStatus.BAD_REQUEST
        );
    }

    private ResponseEntity<ApiError> buildErrorResponse(
            String message, HttpServletRequest request, HttpStatus status) {

        ApiError apiError = ApiError.builder()
                .timestamp(LocalDateTime.now())
                .status(status.value())
                .error(status.getReasonPhrase())
                .message(message)
                .path(request.getRequestURI())
                .build();

        return ResponseEntity
                .status(status)
                .body(apiError);
    }
}
