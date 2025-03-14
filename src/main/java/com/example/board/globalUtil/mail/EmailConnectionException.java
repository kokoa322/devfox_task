package com.example.board.globalUtil.mail;

public class EmailConnectionException extends RuntimeException {

	public EmailConnectionException() {
        super("연결에 문제가 발생했습니다.");
    }

    public EmailConnectionException(String message) {
        super(message);
    }

    public EmailConnectionException(String message, Throwable cause) {
        super(message, cause);
    }

    public EmailConnectionException(Throwable cause) {
        super(cause);
    }
}
