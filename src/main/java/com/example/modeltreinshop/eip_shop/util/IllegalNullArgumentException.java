package com.example.modeltreinshop.eip_shop.util;

public class IllegalNullArgumentException extends IllegalArgumentException {
    public IllegalNullArgumentException() {
    }

    public IllegalNullArgumentException(String s) {
        super(s);
    }

    public IllegalNullArgumentException(String message,
                                        Throwable cause) {
        super(message, cause);
    }

    public IllegalNullArgumentException(Throwable cause) {
        super(cause);
    }
}
