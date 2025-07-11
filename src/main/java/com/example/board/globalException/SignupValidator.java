package com.example.board.globalException;

import java.util.Optional;

import org.springframework.stereotype.Component;

import com.example.board.users.dao.UsersDao;
import com.example.board.users.dto.req.SignupReqDto;
import com.example.board.users.util.Validator;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SignupValidator {

    private final UsersDao usersDao;
    
    /**
     * ユーザー名が存在するかどうかを確認するメソッドです。
     * 
     * @param username チェックするユーザー名
     * @return ユーザー名が存在する場合はtrue、それ以外はfalse
     * 
     * 指定されたユーザー名がデータベースに存在するかを確認し、その結果を返します。
     * １。ユーザー名を中腹検査するメソッド呼び出します。
     */
    public boolean checkUsername(String username) {
    	// １。
        Optional<Boolean> result = Optional.ofNullable((Boolean) usersDao.existsUsername(username)); // メソッドを呼び出して、与えられたユーザー名が既に存在するかを確認し、成功場合はTURE、失敗場合はFALSE
        return result.orElse(false);
    }

    /**
     * メールアドレスが存在するかどうかを確認するメソッドです。
     * 
     * @param email チェックするメールアドレス
     * @return メールアドレスが存在する場合はtrue、それ以外はfalse
     * 
     * 指定されたメールアドレスがデータベースに存在するかを確認し、その結果を返します。
     * １。メールアドレスを中腹検査するメソッド呼び出します。
     */
    public boolean checkEmail(String email) {
        // １。
        Optional<Boolean> result = Optional.ofNullable((Boolean) usersDao.existsEmail(email)); // メソッドを呼び出して、与えられたメールアドレスが既に存在するかを確認し、成功場合はTRUE、失敗場合はFALSE
        return result.orElse(false);
    }

    public void validate(SignupReqDto signupReqDto) {

        if (!Validator.isValidEmail(signupReqDto.getEmail())) {
            throw new SignupValidationException("이메일 형식이 잘못되었습니다。");
        }

        if (!Validator.isValidPassword(signupReqDto.getPassword())) {
            throw new SignupValidationException("비밀번호는 최소 8자 이상 특수문자 포함되어야 합니다。");
        }

        if (checkUsername(signupReqDto.getUsername())) {
            throw new SignupValidationException("이미 사용 중인 사용자 이름입니다。");
        }

        if (checkEmail(signupReqDto.getEmail())) {
            throw new SignupValidationException("해당 이메일로 이미 가입된 회원이 존재합니다。");
        }
        
        if (!Validator.isPasswordMatch(signupReqDto.getPassword(), signupReqDto.getConfirmPassword())) {
            throw new SignupValidationException("비밀번호가 일치하지 않습니다。");
        }
    }
}
