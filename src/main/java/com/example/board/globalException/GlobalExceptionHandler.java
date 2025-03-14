package com.example.board.globalException;

import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.board.globalUtil.mail.EmailService;
import com.mysql.cj.exceptions.CJCommunicationsException;

import lombok.RequiredArgsConstructor;

import org.apache.ibatis.exceptions.PersistenceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

	private final EmailService emailService;
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    private void sendAdminAlert(String subject, String message) {
        emailService.sendEmail(subject, message);
        logger.info("관리자에게 알림 전송: {} - {}", subject, message);
    }

	
	@ExceptionHandler(CJCommunicationsException.class)
    public ResponseEntity<String> handleNetworkError(CJCommunicationsException e) {
        logger.error(DatabaseErrorMessage.NETWORK_OR_DRIVER_ERROR.getMessage() + "{}", e.getMessage(), e);
        sendAdminAlert(DatabaseErrorMessage.NETWORK_OR_DRIVER_ERROR.getMessage(), e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(DatabaseErrorMessage.NETWORK_OR_DRIVER_ERROR.getMessage() + e.getMessage());
    }

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<String> handleDataAccessException(DataAccessException e) {
        logger.error(DatabaseErrorMessage.DATABASE_ACCESS_ERROR.getMessage() + "{}", e.getMessage(), e);
        sendAdminAlert(DatabaseErrorMessage.DATABASE_ACCESS_ERROR.getMessage(), e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(DatabaseErrorMessage.DATABASE_ACCESS_ERROR.getMessage() + e.getMessage());
    }

    @ExceptionHandler(PersistenceException.class)
    public ResponseEntity<String> handlePersistenceException(PersistenceException e) {
        logger.error(DatabaseErrorMessage.MYBATIS_SQL_EXECUTION_ERROR.getMessage() + "{}", e.getMessage(), e);
        sendAdminAlert(DatabaseErrorMessage.MYBATIS_SQL_EXECUTION_ERROR.getMessage(), e.getMessage());
        throw new DatabaseConnectionException(DatabaseErrorMessage.MYBATIS_SQL_EXECUTION_ERROR.getMessage(), e);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleUnknownException(Exception e) {
        logger.error(DatabaseErrorMessage.UNKNOWN_ERROR.getMessage() + "{}", e.getMessage(), e);
        sendAdminAlert(DatabaseErrorMessage.UNKNOWN_ERROR.getMessage(), e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(DatabaseErrorMessage.UNKNOWN_ERROR.getMessage() + e.getMessage());
    }
    
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException e) {
    	logger.error(DatabaseErrorMessage.UNKNOWN_ERROR.getMessage() + "{}", e.getMessage(), e);
    	sendAdminAlert(DatabaseErrorMessage.UNKNOWN_ERROR.getMessage(), e.getMessage());
        return ResponseEntity.badRequest().body("잘못된 요청: " + e.getMessage());
    }
    
//    @ExceptionHandler(DatabaseConnectionException.class)
//    public ResponseEntity<String> handleDatabaseConnectionException(DatabaseConnectionException e) {
//        logger.error("데이터베이스 연결 오류: {}", e.getMessage(), e);
//        sendAdminAlert("데이터베이스 연결 오류", e.getMessage());
//        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                .body("데이터베이스 연결 오류: " + e.getMessage());
//    }
   
}
