package com.example.board.users.util;

import java.util.regex.Pattern;

public class Validator {

    // 이메일 유효성 검사 정규 표현식
	// 이메일 유효성 검사 정규 표현식
	private static final String EMAIL_REGEX ="^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
    private static final String PASSWORD_REGEX = "^(?=.*[!@#$%^&*(),.?\":{}|<>])[\\S]{8,}$"; // 특수문자 포함 + 공백 없이 8자 이상


    /**
     * 이메일 유효성 검사
     *
     * @param email 이메일 주소
     * @return 유효한 이메일이면 true, 그렇지 않으면 false
     */
    public static boolean isValidEmail(String email) {
        if (email == null) return false;
        Pattern pattern = Pattern.compile(EMAIL_REGEX);
        return pattern.matcher(email).matches();
    }
    
    /**
     * 비밀번호 유효성 검사 (최소 8자 이상, 특수문자 포함)
     *
     * @param password 비밀번호
     * @return 유효한 비밀번호이면 true, 그렇지 않으면 false
     */
    public static boolean isValidPassword(String password) {
        if (password == null) return false;
        return Pattern.matches(PASSWORD_REGEX, password);
    }
    /**
     * 비밀번호 일치 여부 검사
     *
     * @param password 원본 비밀번호
     * @param confirmPassword 확인용 비밀번호
     * @return 두 비밀번호가 같으면 true, 다르면 false
     */
    public static boolean isPasswordMatch(String password, String confirmPassword) {
        if (password == null || confirmPassword == null) {
            return false;
        }
        return password.equals(confirmPassword);
    }
}
