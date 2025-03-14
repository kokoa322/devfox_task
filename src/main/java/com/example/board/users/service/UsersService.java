package com.example.board.users.service;

import com.example.board.users.dto.req.DeleteUserReqDto;
import com.example.board.users.dto.req.SigninReqDto;
import com.example.board.users.dto.req.SignupReqDto;
import com.example.board.users.dto.res.SigninResDto;

public interface UsersService {

    /**
     * ユーザー名の重複を検査します。
     * 
     * @param username 確認したいユーザー名
     * @return 重複がある場合はtrue
     */
    boolean checkUsername(String username);

    /**
     * メールアドレスの重複を検査します。
     * 
     * @param email チェックしたいメールアドレス
     * @return 重複がある場合はtrue
     */
    boolean checkEmail(String email);

    /**
     * 新規ユーザーを登録します。
     * @param signupReqDto 新規登録用の情報
     * @return 登録成功はtrue
     */
    boolean signup(SignupReqDto signupReqDto);

    /**
     * ログイン処理を行います。
     * 
     * @param signinReqDto ログイン情報
     * @return ログイン成功の情報
     */
    SigninResDto signin(SigninReqDto signinReqDto);

    /**
     * アカウントを削除します。
     * 
     * @param deleteUserReqDto 削除するアカウントの情報
     * @return 削除成功はtrue
     */
    boolean deleteAccaunt(DeleteUserReqDto deleteUserReqDto);

}
