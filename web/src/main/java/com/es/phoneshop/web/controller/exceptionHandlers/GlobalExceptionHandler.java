package com.es.phoneshop.web.controller.exceptionHandlers;

import ch.qos.logback.classic.Logger;
import com.es.core.cart.exceptions.ItemNotExistException;
import com.es.core.model.exceptions.DatabaseException;
import com.es.core.model.exceptions.DatabaseUpdateException;
import com.es.phoneshop.web.constants.WebConstants;
import com.es.phoneshop.web.exceptions.EmptyCartException;
import com.es.phoneshop.web.exceptions.EmptyOrderListException;
import com.es.phoneshop.web.exceptions.InvalidOrderIdException;
import com.es.phoneshop.web.exceptions.InvalidPageNumberException;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger logger = (Logger) LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler({InvalidPageNumberException.class,
            ItemNotExistException.class,
            EmptyCartException.class,
            InvalidOrderIdException.class,
            EmptyOrderListException.class})
    public String handleInvalidPageNumber(Exception e, Model model) {
        handleError(
                e.getMessage(),
                WebConstants.MESSAGE_PARAM,
                e.getMessage(),
                model);

        return "errors/error404";
    }

    @ExceptionHandler({DatabaseUpdateException.class, DatabaseException.class})
    public String handleDatabaseUpdateException(Exception e, Model model) {
        handleError(
                WebConstants.ERROR_UNEXPECTED_MESSAGE + e.getMessage(),
                WebConstants.ERROR_UNEXPECTED_MESSAGE,
                e.getMessage(),
                model);

        return "errors/error500";
    }

    @ExceptionHandler(Exception.class)
    public String handleOtherExceptions(Exception e, Model model) {
        handleError(
                WebConstants.ERROR_UNEXPECTED_MESSAGE + e.getMessage(),
                WebConstants.ERROR_UNEXPECTED_MESSAGE,
                e.getMessage(),
                model);

        return "errors/error";
    }

    private void handleError(String message, String loggStatus, String loggMessage, Model model) {
        logger.error("{}{}", loggStatus, loggMessage);
        model.addAttribute(WebConstants.MESSAGE_PARAM, message);
    }
}
