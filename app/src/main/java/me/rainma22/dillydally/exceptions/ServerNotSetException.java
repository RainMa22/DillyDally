package me.rainma22.dillydally.exceptions;

/**
 * ServerNotSetException
 */
public class ServerNotSetException extends DillyDallyException{

    public ServerNotSetException() {
    }

    public ServerNotSetException(String message) {
        super(message);
    }

    public ServerNotSetException(Throwable cause) {
        super(cause);
    }

    public ServerNotSetException(String message, Throwable cause) {
        super(message, cause);
    }

}
