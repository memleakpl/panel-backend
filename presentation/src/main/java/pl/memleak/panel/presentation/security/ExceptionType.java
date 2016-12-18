package pl.memleak.panel.presentation.security;

/**
 * Created by wigdis on 18.12.16.
 */
public enum ExceptionType {
    BAD_CREDENTIALS(401),
    BAD_REQUEST(400);

    private int code;

    ExceptionType(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

}
