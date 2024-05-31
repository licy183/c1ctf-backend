package club.c1sec.c1ctfplatform.exception;

/**
 * 不捕获堆栈跟踪的 {@link Exception}
 *
 * @see java.lang.Exception
 * @since 1.0.1
 */
public class NoStackTraceException extends Exception {
    public NoStackTraceException() {
        super(null, null, false, false);
    }

    public NoStackTraceException(String message) {
        super(message, null, false, false);
    }

}
