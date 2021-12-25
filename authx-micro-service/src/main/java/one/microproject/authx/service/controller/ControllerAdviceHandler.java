package one.microproject.authx.service.controller;

import one.microproject.authx.common.dto.ResponseMessage;
import one.microproject.authx.service.exceptions.DataConflictException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class ControllerAdviceHandler {

    @ExceptionHandler(DataConflictException.class)
    public ResponseEntity<ResponseMessage> handleDataConflictException(DataConflictException ex, WebRequest request) {
        ResponseMessage responseMessage = ResponseMessage.error(ex.getMessage());
        return new ResponseEntity<>(responseMessage, HttpStatus.CONFLICT);
    }

}
