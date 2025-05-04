package org.example.auctionmaerketrealtime.common.exception;

public class InternalException extends RuntimeException {

    private final ErrorCode errorCode;

    public InternalException(ErrorCode errorCode) {
        super(errorCode.getDefaultMessage());
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
