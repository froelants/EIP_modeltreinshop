package be.vdab.modeltreinshop.eip_shop.util;

public class IllegalBlankArgumentException extends IllegalNullArgumentException{
    public IllegalBlankArgumentException() {
    }

    public IllegalBlankArgumentException(String s) {
        super(s);
    }

    public IllegalBlankArgumentException(String message, Throwable cause) {
        super(message, cause);
    }

    public IllegalBlankArgumentException(Throwable cause) {
        super(cause);
    }
}
