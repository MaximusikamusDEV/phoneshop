package com.es.phoneshop.web.controller;

import ch.qos.logback.classic.Logger;
import com.es.core.cart.exceptions.CartValidationException;
import com.es.core.cart.exceptions.ItemNotExistException;
import com.es.core.model.exceptions.DatabaseException;
import com.es.core.model.exceptions.DatabaseUpdateException;
import com.es.phoneshop.web.constants.WebConstants;
import com.es.phoneshop.web.dto.AjaxCartResponseDto;
import com.es.phoneshop.web.exceptions.InvalidPageNumber;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    Logger logger = (Logger) LoggerFactory.getLogger(GlobalExceptionHandler.class);
    @ExceptionHandler(CartValidationException.class)
    public ResponseEntity<AjaxCartResponseDto> handleValidationExceptions(CartValidationException e) {
        AjaxCartResponseDto response = new AjaxCartResponseDto(WebConstants.ERROR_ADD_MESSAGE, e.getMessage());
        logger.error(e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<AjaxCartResponseDto> handleHttpMessageNotReadableException() {
        AjaxCartResponseDto response = new AjaxCartResponseDto(WebConstants.ERROR_ADD_MESSAGE, WebConstants.ERROR_WRONG_FORMAT_MESSAGE);
        logger.error(WebConstants.ERROR_WRONG_FORMAT_MESSAGE);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(InvalidPageNumber.class)
    public String handleInvalidPageNumber(InvalidPageNumber e, Model model) {
        model.addAttribute(WebConstants.MESSAGE_PARAM, e.getMessage());
        logger.error(WebConstants.MESSAGE_PARAM + e.getMessage());
        return "errors/error404";
    }

    @ExceptionHandler(ItemNotExistException.class)
    public String handleItemNotExistException(ItemNotExistException e, Model model) {
        model.addAttribute(WebConstants.MESSAGE_PARAM, e.getMessage());
        logger.error(WebConstants.MESSAGE_PARAM + e.getMessage());
        return "errors/error404";
    }

    @ExceptionHandler(DatabaseUpdateException.class)
    public String handleDatabaseUpdateException(Exception e, Model model) {
        model.addAttribute(WebConstants.MESSAGE_PARAM, WebConstants.ERROR_UNEXPECTED_MESSAGE + e.getMessage());
        logger.error(WebConstants.ERROR_UNEXPECTED_MESSAGE + e.getMessage());
        return "errors/error500";
    }

    @ExceptionHandler(DatabaseException.class)
    public String handleDatabaseException(Exception e, Model model) {
        model.addAttribute(WebConstants.MESSAGE_PARAM, WebConstants.ERROR_UNEXPECTED_MESSAGE + e.getMessage());
        logger.error(WebConstants.ERROR_UNEXPECTED_MESSAGE + e.getMessage());
        return "errors/error500";
    }

    @ExceptionHandler(Exception.class)
    public String handleOtherExceptions(Exception e, Model model) {
        model.addAttribute(WebConstants.MESSAGE_PARAM, WebConstants.ERROR_UNEXPECTED_MESSAGE + e.getMessage());
        logger.error(WebConstants.ERROR_UNEXPECTED_MESSAGE + e.getMessage());
        return "errors/error";
    }
}
