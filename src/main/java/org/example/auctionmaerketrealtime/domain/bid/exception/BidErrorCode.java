package org.example.auctionmaerketrealtime.domain.bid.exception;

import org.example.auctionmaerketrealtime.common.exception.ErrorCode;
import org.springframework.http.HttpStatus;

public enum BidErrorCode implements ErrorCode {

    LOWER_THAN_CURRENT_BID(HttpStatus.BAD_REQUEST, "LOWER_THAN_CURRENT_BID", "현재 최고가보다 낮아 입찰할 수 없습니다");

    private final HttpStatus httpStatus;
    private final String code;
    private final String defaultMessage;

    BidErrorCode(HttpStatus httpStatus, String code, String defaultMessage) {
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
