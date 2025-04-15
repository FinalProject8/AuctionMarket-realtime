package org.example.auctionmaerketrealtime.domain.bid.exception;

import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice
public class StompExceptionHandler {

    @MessageExceptionHandler
    @SendToUser("/queue/errors")
    public String handleException(Throwable throwable) {
        return throwable.getMessage();
    }
}
