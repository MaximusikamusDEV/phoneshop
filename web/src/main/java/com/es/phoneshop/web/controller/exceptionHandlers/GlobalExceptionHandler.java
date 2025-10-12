package com.es.phoneshop.web.controller.exceptionHandlers;

import ch.qos.logback.classic.Logger;
import com.es.core.cart.exceptions.ItemNotExistException;
import com.es.core.model.exceptions.DatabaseException;
import com.es.core.model.exceptions.DatabaseUpdateException;
import com.es.phoneshop.web.constants.WebConstants;
import com.es.phoneshop.web.exceptions.InvalidPageNumberException;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger logger = (Logger) LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(InvalidPageNumberException.class)
    public String handleInvalidPageNumber(InvalidPageNumberException e, Model model) {
        model.addAttribute(WebConstants.MESSAGE_PARAM, e.getMessage());
        logger.error("{}{}", WebConstants.MESSAGE_PARAM, e.getMessage());
        return "errors/error404";
    }

    @ExceptionHandler(ItemNotExistException.class)
    public String handleItemNotExistException(ItemNotExistException e, Model model) {
        model.addAttribute(WebConstants.MESSAGE_PARAM, e.getMessage());
        logger.error("{}{}", WebConstants.MESSAGE_PARAM, e.getMessage());
        return "errors/error404";
    }

    @ExceptionHandler(DatabaseUpdateException.class)
    public String handleDatabaseUpdateException(Exception e, Model model) {
        model.addAttribute(WebConstants.MESSAGE_PARAM, WebConstants.ERROR_UNEXPECTED_MESSAGE + e.getMessage());
        logger.error("{}{}", WebConstants.ERROR_UNEXPECTED_MESSAGE, e.getMessage());
        return "errors/error500";
    }

    @ExceptionHandler(DatabaseException.class)
    public String handleDatabaseException(Exception e, Model model) {
        model.addAttribute(WebConstants.MESSAGE_PARAM, WebConstants.ERROR_UNEXPECTED_MESSAGE + e.getMessage());
        logger.error("{}{}", WebConstants.ERROR_UNEXPECTED_MESSAGE, e.getMessage());
        return "errors/error500";
    }

    @ExceptionHandler(Exception.class)
    public String handleOtherExceptions(Exception e, Model model) {
        model.addAttribute(WebConstants.MESSAGE_PARAM, WebConstants.ERROR_UNEXPECTED_MESSAGE + e.getMessage());
        logger.error("{}{}", WebConstants.ERROR_UNEXPECTED_MESSAGE, e.getMessage());
        return "errors/error";
    }
}
