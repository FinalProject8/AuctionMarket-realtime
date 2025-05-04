package org.example.auctionmaerketrealtime.common.exception;

import org.springframework.http.HttpStatus;

public enum InternalErrorCode implements ErrorCode {

    INVALID_BID_FORMAT(HttpStatus.BAD_REQUEST, "INVALID_BID_FORMAT", "입찰가는 숫자만 입력할 수 있습니다"),
    SESSION_URI_MISSING(HttpStatus.BAD_REQUEST, "SESSION_URI_MISSING", "올바르지 않은 경매 접속 주소입니다"),
    MESSAGE_SEND_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "MESSAGE_SEND_FAILED", "클라이언트에게 메시지 전송에 실패했습니다"),
    SESSION_CLOSED(HttpStatus.NOT_FOUND, "SESSION_CLOSED", "세션이 닫혀있어 메세지 전송이 불가능합니다");

    private final HttpStatus httpStatus;
    private final String code;
    private final String defaultMessage;

    InternalErrorCode(HttpStatus httpStatus, String code, String defaultMessage) {
        this.httpStatus = httpStatus;
        this.code = code;
        this.defaultMessage = defaultMessage;
    }

    @Override
    public HttpStatus getHttpStatus() {
        return this.httpStatus;
    }

    @Override
    public String getCode() {
        return this.code;
    }

    @Override
    public String getDefaultMessage() {
        return this.defaultMessage;
    }
}
