package com.example.board.users.dao;

import org.apache.ibatis.annotations.Mapper;

import com.example.board.users.domain.Users;

@Mapper
public interface UsersDao {

	/**
	 * ユーザー名が既に存在するか確認するメソッドです。
	 * 
	 * @param username チェックするユーザー名
	 * @return ユーザー名が存在する場合はtrue、それ以外はfalseを返します。
	 */
	boolean existsUsername(String username);

	/**
	 * メールアドレスが既に存在するか確認するメソッドです。
	 * 
	 * @param email チェックするメールアドレス
	 * @return メールアドレスが存在する場合はtrue、それ以外はfalseを返します。
	 */
	boolean existsEmail(String email);

	/**
	 * 新しいユーザーをデータベースに登録するメソッドです。
	 * 
	 * @param users 登録するユーザー情報を含むUsersオブジェクト
	 * @return ユーザー登録が成功した場合は1、それ以外の場合は0を返します。
	 */
	int signup(Users users);

	/**
	 * ユーザー名に基づいてユーザー情報を検索するメソッドです。
	 * 
	 * @param username 検索するユーザー名
	 * @return 指定されたユーザー名に一致するユーザー情報があればUsersオブジェクトを返し、見つからなければnullを返します。
	 */
	Users findByUsername(String username);

	/**
	 * ユーザーIDに基づいてユーザーを削除するメソッドです。
	 * 
	 * @param id 削除するユーザーのID
	 * @return ユーザー削除が成功した場合は削除された行数を返し、失敗した場合は0を返します。
	 */
	int deleteById(long id);
}
