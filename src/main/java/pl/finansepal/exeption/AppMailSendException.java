package pl.finansepal.exeption;

public class AppMailSendException extends RuntimeException {
    public AppMailSendException(String exMessage){
        super(exMessage);
    }
}
