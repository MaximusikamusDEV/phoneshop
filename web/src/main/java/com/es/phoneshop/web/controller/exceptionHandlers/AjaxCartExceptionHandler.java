package com.es.phoneshop.web.controller.exceptionHandlers;

import ch.qos.logback.classic.Logger;
import com.es.core.cart.exceptions.CartValidationException;
import com.es.core.order.OutOfStockException;
import com.es.phoneshop.web.constants.WebConstants;
import com.es.phoneshop.web.controller.AjaxCartController;
import com.es.phoneshop.web.dto.AjaxCartResponseDto;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice(assignableTypes = AjaxCartController.class)
public class AjaxCartExceptionHandler {
    private static final Logger logger = (Logger) LoggerFactory.getLogger(AjaxCartExceptionHandler.class);
    @ExceptionHandler(CartValidationException.class)
    public ResponseEntity<AjaxCartResponseDto> handleValidationExceptions(CartValidationException e) {
        AjaxCartResponseDto response = new AjaxCartResponseDto(WebConstants.ERROR_MESSAGE, e.getMessage());
        logger.error("{}", e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(OutOfStockException.class)
    public ResponseEntity<AjaxCartResponseDto> handleOutOfStockException() {
        AjaxCartResponseDto response = new AjaxCartResponseDto(WebConstants.ERROR_MESSAGE, WebConstants.ERROR_OUT_OF_STOCK_MESSAGE);
        logger.error("{}", WebConstants.ERROR_OUT_OF_STOCK_MESSAGE);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<AjaxCartResponseDto> handleHttpMessageNotReadableException() {
        AjaxCartResponseDto response = new AjaxCartResponseDto(WebConstants.ERROR_MESSAGE, WebConstants.ERROR_WRONG_FORMAT_MESSAGE);
        logger.error("{}", WebConstants.ERROR_WRONG_FORMAT_MESSAGE);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
}
