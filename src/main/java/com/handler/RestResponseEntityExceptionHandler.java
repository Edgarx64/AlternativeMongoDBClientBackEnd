package com.handler;

import com.bean.ErrorBean;
import com.exception.ParseEnumRuntimeException;
import com.exception.ParsingRuntimeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @ExceptionHandler(value = ParsingRuntimeException.class)
    protected ResponseEntity<ErrorBean> handleConflictParsiongQuery(RuntimeException re) {
        logger.error(re.getMessage());
        String message = "Please, enter a valid query.\n" + re.getMessage();
        return new ResponseEntity<>(new ErrorBean(message), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = ParseEnumRuntimeException.class)
    protected ResponseEntity<ErrorBean> handleConflictParseEnum(RuntimeException re) {
        logger.error(re.getMessage());
        String message = "Please correct this. You made a mistake.\n" + re.getMessage();
        return new ResponseEntity<>(new ErrorBean(message), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = IllegalArgumentException.class)
    protected ResponseEntity<ErrorBean> handleConflictIllegalArgument(RuntimeException re) {
        logger.error(re.getMessage());
        String message = "Internal error. Notify administrator.\n" + re.getMessage();
        return new ResponseEntity<>(new ErrorBean(message), HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
