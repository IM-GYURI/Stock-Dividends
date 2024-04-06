package zerobase.stockdividends.exception.impl;

import org.springframework.http.HttpStatus;
import zerobase.stockdividends.exception.AbstractException;

public class UnexpectedMonthException extends AbstractException {
    @Override
    public int getStatusCode() {
        return HttpStatus.BAD_REQUEST.value();
    }

    @Override
    public String getMessage() {
        return "1~12월을 벗어난 달입니다.";
    }
}
