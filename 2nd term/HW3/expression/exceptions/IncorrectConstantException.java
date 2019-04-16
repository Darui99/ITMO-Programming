package expression.exceptions;

public class IncorrectConstantException extends EvaluateException {
    public IncorrectConstantException(String message, int pos) {
        super(message + ". Mistake is in " + pos + " symbol.");
    }
}
