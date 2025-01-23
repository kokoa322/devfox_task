package com.example.board.globalException;

public enum DatabaseErrorMessage {
    NETWORK_OR_DRIVER_ERROR("네트워크 연결 문제 또는 MySQL 드라이버 오류: "),
    DATABASE_ACCESS_ERROR("데이터베이스 접근 오류: "),
	MYBATIS_SQL_EXECUTION_ERROR("MyBatis SQL 실행 오류:"),
	UNKNOWN_ERROR("예상치 못한 오류 발생:"); 
	
    private final String message;

    DatabaseErrorMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
