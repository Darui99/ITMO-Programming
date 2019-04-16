package expression.exceptions;

public class MissingOperationException extends ParsingException {
    public MissingOperationException(String message) {
        super(message);
    }
}
