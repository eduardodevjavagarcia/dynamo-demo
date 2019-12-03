package com.example.dynamodemo.controllers.advice;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMappingException;
import com.example.dynamodemo.configurations.Internationalization;
import com.example.dynamodemo.exceptions.BadRequestException;
import com.example.dynamodemo.exceptions.NotFoundException;
import com.example.dynamodemo.exceptions.UnprocessableEntityException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@ControllerAdvice
@RequiredArgsConstructor
public class StudentControllerAdvice {

    private final Internationalization messageSource;

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<Error> mediaTypeNotFoundException(final HttpMediaTypeNotSupportedException e) {
        return new ResponseEntity<>(HttpStatus.UNSUPPORTED_MEDIA_TYPE);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<List<Error>> assertionException(final HttpMessageNotReadableException e) {
        return error();
    }

    @ExceptionHandler(DynamoDBMappingException.class)
    public ResponseEntity<List<Error>> assertionException(final DynamoDBMappingException e) {
        return error();
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<List<Error>> missingServletRequestParameterException(
            final MissingServletRequestParameterException e) {
        return error();
    }


    private ResponseEntity<List<Error>> error() {
        return new ResponseEntity<>(
                Collections.singletonList(Error.builder().code("400.000").description(this.messageSource.getMessage("400.000")).build()),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Void> handleNotFoundException(NotFoundException notFoundException) {
        return new ResponseEntity<>(notFoundException.getStatus());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<Error>> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException methodArgumentNotValidException) {
        List<Error> messageErrors = Optional.ofNullable(methodArgumentNotValidException)
                .filter(argumentNotValidException -> !ObjectUtils.isEmpty(argumentNotValidException.getBindingResult()))
                .map(MethodArgumentNotValidException::getBindingResult)
                .filter(bindingResult -> !ObjectUtils.isEmpty(bindingResult.getAllErrors()))
                .map(BindingResult::getAllErrors).map(Stream::of).orElseGet(Stream::empty).flatMap(Collection::stream)
                .filter(objectError -> !ObjectUtils.isEmpty(objectError))
                .map(this::createError)
                .collect(Collectors.toList());
        return new ResponseEntity<>(messageErrors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UnprocessableEntityException.class)
    public ResponseEntity<Error> handleUnprocessableEntityException(
            UnprocessableEntityException unprocessableEntityException) {
        return new ResponseEntity<>(Error
                .builder().code(unprocessableEntityException.getCode()).description(messageSource
                        .getMessage(unprocessableEntityException.getCode(), unprocessableEntityException.getArgs()))
                .build(), HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<List<Error>> handleBadRequestException(BadRequestException badRequestException) {
        return ResponseEntity.status(badRequestException.getStatus()).body(badRequestException.getErrors());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Error> handleException(Exception e) {
        log.error(e.getMessage(), e);
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private Error createError(ObjectError error) {
        String field = "";
        if (error instanceof FieldError) {
            field = ((FieldError) error).getField();
        }
        return Error.builder().code(error.getDefaultMessage())
                .description(messageSource.getMessage(error.getDefaultMessage(), field)).build();
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Error> methodArgumentTypeMismatchException(
            final MethodArgumentTypeMismatchException e) {
        return new ResponseEntity<>(Error.builder().code("400.000").description(e.getName()).build(), HttpStatus.BAD_REQUEST);
    }

}