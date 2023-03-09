package com.java.test.junior.exception;

import com.java.test.junior.load.exception.GenericLoaderException;
import com.java.test.junior.load.exception.GenericReaderException;
import com.java.test.junior.load.exception.GenericWriterException;
import com.java.test.junior.product.exception.ProductNotFoundException;
import com.java.test.junior.product.exception.UserIsNotProductOwnerException;
import com.java.test.junior.user.exception.UserAlreadyExistsException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * @author artiom.spac
 * @version java-test-junior
 * @apiNote 08.03.2023
 */
@ControllerAdvice
public class ExceptionsHandler {

    @ExceptionHandler({ProductNotFoundException.class})
    public ResponseEntity<ExceptionModel> handleObjectsNotFoundException(RuntimeException ex) {
        return new ResponseEntity<>(new ExceptionModel(ex.getMessage(), HttpStatus.NOT_FOUND.value()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({UserIsNotProductOwnerException.class, UserAlreadyExistsException.class, GenericReaderException.class, GenericLoaderException.class, GenericWriterException.class})
    public ResponseEntity<ExceptionModel> handleBadRequests(RuntimeException ex) {
        return new ResponseEntity<>(new ExceptionModel(ex.getMessage(), HttpStatus.BAD_REQUEST.value()), HttpStatus.BAD_REQUEST);
    }
}
