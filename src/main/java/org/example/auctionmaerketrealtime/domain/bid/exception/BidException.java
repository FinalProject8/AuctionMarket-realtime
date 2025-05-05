package org.example.auctionmaerketrealtime.domain.bid.exception;

import org.example.auctionmaerketrealtime.common.exception.ErrorCode;

public class BidException extends RuntimeException {

    private final ErrorCode errorCode;

    public BidException(ErrorCode errorCode) {
        super(errorCode.getDefaultMessage());
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
