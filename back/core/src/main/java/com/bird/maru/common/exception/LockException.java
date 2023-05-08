package com.bird.maru.common.exception;

public class LockException extends RuntimeException {

    private static final String EXCEPTION_MESSAGE = "LOCK을 수행하는 중에 오류가 발생했습니다.";

    public LockException() {
        super(EXCEPTION_MESSAGE);
    }

}
