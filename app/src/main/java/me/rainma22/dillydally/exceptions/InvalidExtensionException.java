package me.rainma22.dillydally.exceptions;

public class InvalidExtensionException extends DillyDallyException{

    public InvalidExtensionException() {
    }

    public InvalidExtensionException(String message) {
        super(message);
    }

    public InvalidExtensionException(Throwable cause) {
        super(cause);
    }

    public InvalidExtensionException(String message, Throwable cause) {
        super(message, cause);
    }
    
}
