package club.c1sec.c1ctfplatform.vo;

import lombok.Data;

@Data
public class Response<T> {
    private Integer code;
    private String message;
    private T payload;

    public void success(String message) {
        this.code = 200;
        this.message = message;
        try {
            this.payload = (T) message;
        } catch (Exception e) {
            this.payload = null;
        }
    }

    public void success(String message, T data) {
        this.code = 200;
        this.message = message;
        this.payload = data;
    }

    public void fail(String message) {
        this.code = 500;
        this.message = message;
        this.payload = null;
    }

    public void fail(String message, T data) {
        this.code = 500;
        this.message = message;
        this.payload = data;
    }

    public void invalid(String message) {
        this.code = 400;
        this.message = message;
        this.payload = null;
    }

    public void invalid(String msg, T data) {
        this.code = 400;
        this.message = message;
        this.payload = data;
    }
}
