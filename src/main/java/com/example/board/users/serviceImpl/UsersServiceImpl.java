package com.example.board.users.serviceImpl;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.board.globalUtil.DateTimeFormatterUtil;
import com.example.board.users.dao.UsersDao;
import com.example.board.users.domain.Users;
import com.example.board.users.dto.req.DeleteUserReqDto;
import com.example.board.users.dto.req.SigninReqDto;
import com.example.board.users.dto.req.SignupReqDto;
import com.example.board.users.dto.res.SigninResDto;
import com.example.board.users.service.UsersService;
import com.example.board.users.util.EmailValidator;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class UsersServiceImpl implements UsersService {

    // UsersDao 의존성 주입
    private final UsersDao usersDao;
    private final PasswordEncoder passwordEncoder;
    private final DateTimeFormatterUtil dateTimer;

    /**
     * ユーザー名が存在するかどうかを確認するメソッドです。
     * 
     * @param username チェックするユーザー名
     * @return ユーザー名が存在する場合はtrue、それ以外はfalse
     * 
     * 指定されたユーザー名がデータベースに存在するかを確認し、その結果を返します。
     * １。ユーザー名を中腹検査するメソッド呼び出します。
     */
    @Override
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
    @Override
    public boolean checkEmail(String email) {
        // １。
        Optional<Boolean> result = Optional.ofNullable((Boolean) usersDao.existsEmail(email)); // メソッドを呼び出して、与えられたメールアドレスが既に存在するかを確認し、成功場合はTRUE、失敗場合はFALSE
        return result.orElse(false);
    }

    /**
     * ユーザーのジョイン処理を行うメソッドです。
	 * 
	 * @param signupReqDto サインアップリクエスト情報
	 * @return サインアップ処理が成功した場合はtrue、失敗した場合はfalse
     * 
     * ユーザー名、メールアドレス、パスワードを受け取り、必要なバリデーションとユーザー作成処理を行います。
     * １。パスワードを暗号化します。
     * ２。メール形式を検証するメソッドを呼び出します。
 	 * ３。ユーザー名とメールを中腹検査するメソッド呼び出します。
 	 * ４。すべての条件を満たした場合、データベースにユーザーを登録します。
     */
    @Override
    @Transactional
    public boolean signup(SignupReqDto signupReqDto) {
        
        boolean userNameAvailable;
        boolean emailAvailable;

        // １。
        String encodedPassword = passwordEncoder.encode(signupReqDto.getPassword()); // パスワードを暗号化し, セキュアな形式で保存します。
        Users users = Users.builder()												 //　Usersオブジェクト生成します。
                .username(signupReqDto.getUsername())
                .password(encodedPassword)
                .email(signupReqDto.getEmail())
                .build();
        // ２。
        emailAvailable = EmailValidator.isValidEmail(users.getEmail());				 //　メール形式を検証するメソッドを呼び出しメールアドレスの形式が正しいか検証します。成功場合はTRUE、失敗場合はFALSE	 
        if (emailAvailable) { return false; } 										
        // 3.
        userNameAvailable = checkUsername(users.getUsername());						 // メソッドを呼び出して、与えられたユーザー名が既に存在するかを確認し、成功場合はTURE、失敗場合はFALSE
        emailAvailable = checkEmail(users.getEmail());								 //　ユーザーメールアドレス中腹検査するメソッドを呼び出し、成功場合はTRUE、失敗場合はFALSE

        // 4.
        if (!userNameAvailable && !emailAvailable) {								 // すべての条件を満たした場合、データベースにユーザーを登録します。
            Optional<Boolean> result = Optional.ofNullable(usersDao.signup(users) > 0 ? Boolean.TRUE : Boolean.FALSE); //　ユーザーを登録するメソッドを呼び出し、成功場合はTRUE、失敗場合はFALSE
            log.info("{}様 会員加入 : {}", users.getUsername(), dateTimer.getCurrentTime()); //　ユーザー登録したログ残します。
            return result.orElse(false);
        } else {
            // 4.
            return false;
        }
    }

    /**
     * ユーザーのログインを処理するメソッドです。
     * 
     * @param signinReqDto ログインに必要な情報を含むリクエストDTO
     * @return ログイン処理が成功した場合は、ユーザー名とIDを含むレスポンスDTOを返します。  
     *         ログインに失敗した場合はnullを返します。
     * 
     * ユーザー名とパスワードを受け取り、データベースに保存されているユーザー情報と照合します。
     * １。ユーザーがデータベースに存在するのか確認するメソッド呼びだします。
     * ２。ペスワードを復号化検証するメソッド呼び出します。
     */
    @Override
    @Transactional
    public SigninResDto signin(SigninReqDto signinReqDto) {
    	//１。
        Optional<Users> users = Optional.ofNullable(usersDao.findByUsername(signinReqDto.getUsername())); //　ユーザー名を基にユーザー情報をデータベースから検索し、Usersオブジェクト生成。
        
        //２。
        SigninResDto signinResDto = users.filter(user -> passwordEncoder.matches(signinReqDto.getPassword(), user.getPassword())) //　ペスワードを復号化検証してsigninResDtoオブジェクト生成、
                .map(user -> SigninResDto.builder() //mapメソッドを使ってOptionalにあるデータに接近してビルダーでオブジェクト生成します。
                .id(user.getId())
                .username(user.getUsername())
                .build())
                .orElse(null);						// passwordEncoder.matchesの返り血がFALSE場合はNULLで初期化します。
        return signinResDto;
    }

	/**
	 * ユーザーアカウントを削除するメソッドです。
	 * 
	 * @param deleteUserReqDto アカウント削除に必要な情報を含むリクエストDTO
	 * @return アカウント削除が成功した場合はtrue、失敗した場合はfalseを返します。
	 * 
	 * ユーザーアカウントを削除するためにユーザーIDが存在するのか確認し、
	 * 暗号化して比較します。その後にユーザーのアカウントを削除します。
	 * 
	 */
	@Override
	@Transactional
	public boolean deleteAccaunt(DeleteUserReqDto deleteUserReqDto) {
		//１。
		Users users = usersDao.findByUsername(deleteUserReqDto.getUsername());							//　ユーザー名を基にユーザー情報をデータベースから検索し、Usersオブジェクト生成。
		//２。
		boolean result = passwordEncoder.matches(deleteUserReqDto.getPassword(), users.getPassword()); //　ペスワードを復号化検証し、成功場合はTURE、失敗場合はFALSE
		
		if(result) {
			log.info("{}様アカウント削除", users.getUsername());
			//3。
			return Optional.ofNullable(usersDao.deleteById(users.getId()) > 0 ? (Boolean) true : (Boolean) false).orElse(false); //　データベースにユーザーのアカウントを削除するメソッド呼びだし、成功場合はTURE、失敗場合はFALSE
		}
		return result;
	}
}
