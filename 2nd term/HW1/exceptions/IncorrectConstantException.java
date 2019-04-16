package expression.exceptions;

public class IncorrectConstantException extends ParsingException {
    public IncorrectConstantException(String message, int pos) {
        super(message + ". Mistake is in " + pos + " symbol.");
    }
}
