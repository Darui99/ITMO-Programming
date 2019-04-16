package expression.exceptions;

public class MissingOperandException extends ParsingException {
    public MissingOperandException(String message) {
        super(message);
    }
}
