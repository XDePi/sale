package exception;

public class InvalidFormatFileException extends AbstractValidationFileException {
    public InvalidFormatFileException(Exception e) {
        super(e);
    }
}
