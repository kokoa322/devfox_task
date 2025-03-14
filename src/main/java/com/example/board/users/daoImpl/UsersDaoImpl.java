package com.example.board.users.daoImpl;

import javax.transaction.Transactional;

import org.apache.ibatis.exceptions.PersistenceException;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;
import com.example.board.users.dao.UsersDao;
import com.example.board.users.domain.Users;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class UsersDaoImpl implements UsersDao {

    private final SqlSessionTemplate sqlSession;

    /**
     * ユーザー名が既に存在するかを確認するメソッドです。
     * 
     * @param username チェックするユーザー名
     * @return ユーザー名が存在する場合はtrue、それ以外はfalseを返します。
     */
    @Override
    public boolean existsUsername(String username) {
        return sqlSession.selectOne("Users.existsByUsername", username);
    }

    /**
     * メールアドレスが既に存在するかを確認するメソッドです。
     * 
     * @param email チェックするメールアドレス
     * @return メールアドレスが存在する場合はtrue、それ以外はfalseを返します。
     */
    @Override
    public boolean existsEmail(String email) {
        return sqlSession.selectOne("Users.existsByEmail", email);
    }

    /**
     * 新しいユーザーをデータベースに登録するメソッドです。
     *
     * @param users データベースに登録するユーザー情報を含むUsersオブジェクト
     * @return データベースに挿入された行数を返します。成功した場合は通常1が返されます。
     */
    @Override
    public int signup(Users users) {
        
        return sqlSession.insert("Users.save", users);
    }

    /**
     * 指定されたユーザー名に一致するユーザー情報を検索するメソッドです。
     *
     * @param username 検索対象のユーザー名
     * @return ユーザー名が既に存在する場合はtrue, 存在しない場合はfalseを返します。
     */
	@Override
	public Users findByUsername(String username) {
		
		return sqlSession.selectOne("Users.findByUsername", username);
	}
	
	/**
	 * 指定されたユーザーIDに関連するデータを削除するメソッドです。
	 *
	 * @param id 削除対象のユーザーID
	 * @return ユーザー情報の削除によって影響を受けた行数を返します。
	 *         成功した場合は1以上、失敗した場合は0が返されます。
	 */
	@Transactional
	@Override
	public int deleteById(long id) {
		
		sqlSession.delete("Comments.deleteCommentsByUserId", id);
	    sqlSession.delete("Boards.deleteBoardsByUserId",id);
		return sqlSession.delete("Users.deleteById", id);
	}
}
