package pl.memleak.panel.presentation.security;

import com.google.gson.JsonObject;
import org.springframework.security.core.AuthenticationException;

/**
 * Created by wigdis on 18.12.16.
 */
public class ApplicationAuthenticationException extends AuthenticationException {

    private ExceptionType exceptionType;

    public ApplicationAuthenticationException(String msg, ExceptionType exceptionType, Throwable t) {
        super(msg, t);
        this.exceptionType = exceptionType;
    }

    public ApplicationAuthenticationException(String msg, ExceptionType exceptionType) {
        super(msg);
        this.exceptionType = exceptionType;
    }

    public ExceptionType getExceptionType() {
        return exceptionType;
    }

    public String toJSONFormat() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("type", exceptionType.toString());
        jsonObject.addProperty("msg", getMessage());

        return jsonObject.toString();
    }

}
