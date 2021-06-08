package exception;

public class AbstractValidationFileException extends RuntimeException {
    public AbstractValidationFileException(Exception ex) {
        super(ex);
    }

    public AbstractValidationFileException() {
        super();
    }
}
