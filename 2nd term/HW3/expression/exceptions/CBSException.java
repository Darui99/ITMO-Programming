package expression.exceptions;

public class CBSException extends ParsingException {
    public CBSException(String message, int pos) {
        super(message + ". Mistake is in " + pos + " symbol.");
    }
}
