package com.example.board.globalException;

public class DatabaseConnectionException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;

	public DatabaseConnectionException() {
        super("연결에 문제가 발생했습니다.");
    }

    public DatabaseConnectionException(String message) {
        super(message);
    }

    public DatabaseConnectionException(String message, Throwable cause) {
        super(message, cause);
    }

    public DatabaseConnectionException(Throwable cause) {
        super(cause);
    }
}
