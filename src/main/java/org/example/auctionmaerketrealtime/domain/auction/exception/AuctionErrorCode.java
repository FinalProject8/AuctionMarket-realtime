package org.example.auctionmaerketrealtime.domain.auction.exception;

import org.example.auctionmaerketrealtime.common.exception.ErrorCode;
import org.springframework.http.HttpStatus;

public enum AuctionErrorCode implements ErrorCode {

    AUCTION_NOT_FOUND(HttpStatus.NOT_FOUND, "AUCTION_NOT_FOUND", "경매를 찾을 수 없습니다"),
    AUCTION_NOT_STARTED(HttpStatus.BAD_REQUEST, "AUCTION_NOT_STARTED", "경매가 아직 시작하지 않았습니다"),
    AUCTION_IS_ENDED(HttpStatus.BAD_REQUEST, "AUCTION_IS_ENDED", "경매가 이미 종료되었습니다"),
    AUCTION_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "AUCTION_ALREADY_EXISTS", "이미 존재하는 경매입니다");

    private final HttpStatus httpStatus;
    private final String code;
    private final String defaultMessage;

    AuctionErrorCode(HttpStatus httpStatus, String code, String defaultMessage) {
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
