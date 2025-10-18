package com.es.phoneshop.web.controller.exceptionHandlers;

import ch.qos.logback.classic.Logger;
import com.es.core.cart.exceptions.CartValidationException;
import com.es.core.model.exceptions.HighQuantityException;
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

    @ExceptionHandler({CartValidationException.class, HighQuantityException.class})
    public ResponseEntity<AjaxCartResponseDto> handleValidationExceptions(Exception e) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(handleError(e.getMessage(), e.getMessage()));
    }

    @ExceptionHandler(OutOfStockException.class)
    public ResponseEntity<AjaxCartResponseDto> handleOutOfStockException(OutOfStockException e) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(handleError(
                        String.format(WebConstants.ERROR_OUT_OF_STOCK_MESSAGE, e.getStock()),
                        String.format(WebConstants.ERROR_OUT_OF_STOCK_MESSAGE, e.getStock())));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<AjaxCartResponseDto> handleHttpMessageNotReadableException() {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(handleError(WebConstants.ERROR_WRONG_FORMAT_MESSAGE, WebConstants.ERROR_WRONG_FORMAT_MESSAGE));
    }

    private AjaxCartResponseDto handleError(String message, String loggMessage) {
        logger.error("{}", loggMessage);
        return new AjaxCartResponseDto(WebConstants.ERROR_MESSAGE, message);
    }
}
