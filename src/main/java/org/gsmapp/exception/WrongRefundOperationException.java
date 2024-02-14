package org.gsmapp.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class WrongRefundOperationException extends RuntimeException {

    public WrongRefundOperationException(String message) {
        super(message);
    }
}
