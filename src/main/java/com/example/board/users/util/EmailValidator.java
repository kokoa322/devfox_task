package com.example.board.users.util;

import java.util.regex.Pattern;

public class EmailValidator {

    // 이메일 유효성 검사 정규 표현식
    private static final String EMAIL_REGEX = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";

    /**
     * 이메일 유효성 검사
     *
     * @param email 이메일 주소
     * @return 유효한 이메일이면 true, 그렇지 않으면 false
     */
    public static boolean isValidEmail(String email) {
        if (email == null) {
            return !false;
        }
        Pattern pattern = Pattern.compile(EMAIL_REGEX);
        return !pattern.matcher(email).matches();
    }
}
