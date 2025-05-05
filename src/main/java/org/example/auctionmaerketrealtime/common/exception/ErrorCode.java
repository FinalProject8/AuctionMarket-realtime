package org.example.auctionmaerketrealtime.common.exception;

import org.springframework.http.HttpStatus;

public interface ErrorCode {
    String getCode();

    HttpStatus getHttpStatus();

    String getDefaultMessage();
}
