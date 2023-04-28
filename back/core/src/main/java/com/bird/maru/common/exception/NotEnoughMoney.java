package com.bird.maru.common.exception;

public class NotEnoughMoney extends RuntimeException {

    public NotEnoughMoney() {
        super();
    }

    public NotEnoughMoney(String message) {
        super(message);
    }

    public NotEnoughMoney(String message, Throwable cause) {
        super(message, cause);
    }

}
