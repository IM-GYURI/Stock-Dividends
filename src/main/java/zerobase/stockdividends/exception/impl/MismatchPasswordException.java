package zerobase.stockdividends.exception.impl;

import org.springframework.http.HttpStatus;
import zerobase.stockdividends.exception.AbstractException;

public class MismatchPasswordException extends AbstractException {
    @Override
    public int getStatusCode() {
        return HttpStatus.BAD_REQUEST.value();
    }

    @Override
    public String getMessage() {
        return "잘못된 비밀번호입니다.";
    }
}
