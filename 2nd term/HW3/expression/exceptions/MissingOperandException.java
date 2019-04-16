package expression.exceptions;

public class MissingOperandException extends ParsingException {
    public MissingOperandException(String message, int pos) {
        super(message + ". Mistake is in " + pos + " symbol.");
    }
}
