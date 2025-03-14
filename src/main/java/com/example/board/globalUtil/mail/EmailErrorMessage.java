package com.example.board.globalUtil.mail;

public enum EmailErrorMessage {
	SEND_FAILED("이메일 전송 실패: 잘못된 수신자 주소: "),
    MAIL_PARSE_FAILED("이메일 파싱또는 헤더 오류 발생: "),
    AUTHENTICATION_FAILED("인증 실패: 잘못된 이메일 계정 정보"),
    MAIL_SERVER_CONNECTION_FAILED("메일 서버 연결 실패"),
    INVALID_EMAIL_ADDRESS("잘못된 이메일 주소 형식"),
    MESSAGING_FAILED("인증 실패 또는 메일 서버와 연결할 수 없습니다: "),
    GENERAL_MAIL_ERROR("기타 메일 전송 오류 발생");

    private final String message;

    EmailErrorMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
