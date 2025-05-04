package org.example.auctionmaerketrealtime.domain.auction.exception;

import org.example.auctionmaerketrealtime.common.exception.ErrorCode;

public class AuctionException extends RuntimeException {

    private final ErrorCode errorCode;

    public AuctionException(ErrorCode errorCode) {
        super(errorCode.getDefaultMessage());
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
