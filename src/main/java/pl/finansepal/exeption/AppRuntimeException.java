package pl.finansepal.exeption;

public class AppRuntimeException extends RuntimeException {
    public AppRuntimeException(String invalid_token) {
        super(invalid_token);
    }
}
