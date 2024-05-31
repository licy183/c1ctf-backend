package club.c1sec.c1ctfplatform.exception;

public class CheckFailException extends NoStackTraceException {
    public CheckFailException(String message) {
        super(message);
    }
}
