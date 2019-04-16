package expression.exceptions;

public class IncorrectTokenException extends ParsingException {
    public IncorrectTokenException(String message, int pos) {
        super(message + ". Mistake is in " + pos + " symbol.");
    }
}
