package priv.lee90.learn.exception;

/**
 * 限流异常
 * @author lee90
 */
public class RateLimitException extends RuntimeException {

    public RateLimitException(String message) {
        super(message);
    }
}
