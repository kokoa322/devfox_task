package com.example.board.users.controller;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.board.aspect.RequireSession;
import com.example.board.globalException.SignupValidationException;
import com.example.board.globalException.SignupValidator;
import com.example.board.globalUtil.DateTimeFormatterUtil;
import com.example.board.users.dto.req.DeleteUserReqDto;
import com.example.board.users.dto.req.SigninReqDto;
import com.example.board.users.dto.req.SignupReqDto;
import com.example.board.users.dto.res.SigninResDto;
import com.example.board.users.service.UsersService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
	private final UsersService usersService;
	private final DateTimeFormatterUtil dateTimer;
	private final SignupValidator signupValidator;

	/**
	 * ログインページを表示するメソッドです。
	 * 
	 * @return ログインページのビュー名
	 */
	@GetMapping("/signin")
	public String signin(){
		return "users/signin";
	}
	
	/**
	 * ログアウト処理を行うメソッドです。
	 * 
	 * @param session HTTPセッション
	 * @param response HTTPレスポンス
	 * @return ログアウト後、掲示板ページにリダイレクトする
	 * 
	 * セッションを無効化し、JSESSIONIDクッキーを削除します。
	 * 
	 * ログアウト過程です。
	 * １。セッション無効化
	 * ２。クッきー初期化
	 */
	@GetMapping("/signout")
	public String logout(HttpSession session, HttpServletResponse response) {
		
	    //　１。
	    session.invalidate(); 							//　セッション無効化します。
	    // ２。
	    Cookie cookie = new Cookie("JSESSIONID", null);	//　Nullで設定します。
	    cookie.setMaxAge(0); 							// クッキーの有効期限を0に設定し、即座に期限を満了させます。
	    cookie.setPath("/"); 							// クッキーの経路設定します。
	    response.addCookie(cookie);						//　クッキー初期化します。
	    
	    return "redirect:/boards";  
	}
	
	/**
	 * ホームページを表示するメソッドです。
	 * 
	 * @return　ホームページにリダイレクトする
	 */
	@PatchMapping("/")
	public String editUser() {
		return "home";
	}
	
	/**
	 *　ユーザーアカウントを削除するメソッドです。
	 * 
	 * @param session HTTPセッション
	 * @param deleteUserReqDto ユーザー削除リクエストのデータ
	 * @return ユーザー削除の結果をBooleanタイプを渡す
	 * 
	 *　リクエストボディから削除するユーザー情報を受け取り、アカウント削除を実行します。
	 *　削除が成功した場合、セッションを無効化してログアウトします。
	 * 
	 *　ユーザー削除過程です。
	 *　１。削除するメソッド行います。
	 *　２。セッション無効化
	 *　３。クッきー初期化
	 */
	@RequireSession
	@DeleteMapping("/")
	@ResponseBody
	public ResponseEntity<?> deleteUser(HttpSession session, @RequestBody DeleteUserReqDto deleteUserReqDto, HttpServletResponse response) {
		
		// １。
		boolean result = usersService.deleteAccaunt(deleteUserReqDto);	// ユーザーアカウントを削除するためにサービスメソッドを呼び出し、その結果はbolleanタイプで返されます。
		if(result) {
			// ２。
		    session.invalidate();										//　セッション無効化します。
		    // ３。
		    Cookie cookie = new Cookie("JSESSIONID", null);				//　Nullで設定します。
		    cookie.setMaxAge(0); 										// クッキーの有効期限を0に設定し、即座に期限を満了させます。
		    cookie.setPath("/"); 										// クッキーの経路設定します。
		    response.addCookie(cookie);									//　クッキー初期化します。
			}
		return ResponseEntity.ok(result);
	}
	
	/**
	 * ユーザーがログインするメソッドです。
	 * 
	 * @param signinReqDto サインインリクエストのデータ
	 * @param session HTTPセッション
	 * @param model モデル
	 * @return ログインの結果に基づいてリダイレクトまたはログインページを返す
	 * 
	 * ログインリクエストを処理し、認証が成功した場合はセッションにユーザー情報を保存し、掲示板ページにリダイレクトします。
	 * 認証に失敗した場合はエラーメッセージを表示します。
	 * 
	 * ログイン過程です。
	 * １。ログインするメソッド行います。
	 * ２。セッション設定します
	 * 
	 */
	@PostMapping("/signin")
	public String signiUser(@ModelAttribute SigninReqDto signinReqDto, HttpSession session, Model model) {
	    
		// １。
		SigninResDto signinResDto = usersService.signin(signinReqDto);			//　ユーザーログインするためにサービスメソッド呼び出し、ユーザーの城ホでリスポンスオブジェっくとDTOで返されます。
	    
	    if (signinResDto != null) {
	    	// ２。
	        log.info("{}様ログイン : {}", signinResDto.getUsername(), dateTimer);	//　ログインしたユーザーのログを残します。
	        session.setAttribute("user", signinResDto.getUsername());			//　ユーザー情報を保存します。
	        session.setAttribute("user_id", signinResDto.getId());				//　ユーザー情報を保存します。
	        session.setMaxInactiveInterval(30 * 120);							//　セッションの有効時間を１時間で設定します。
	        
	        return "redirect:/boards";
	    }
	    
	    model.addAttribute("errorMessage", "아이디 또는 비밀번호가 일치하지 않습니다.");	// 失敗した場合、メッセージをモデルに追加してログインページを再表示します。
	    return "users/signin"; 
	}
	
	/**
	 * ユーザージョインを行うメソッドです。
	 * 
	 * @param signupReqDto サインアップリクエストのデータ
	 * @return サインアップの結果を含むレスポンスエンティティ
	 * 
	 * ジョインリクエストを処理し、新規ユーザーの作成結果を返します。
	 * 
	 * ジョイン過程です。
	 * １。ジョインするメソッド呼び出します。
	 */
//	@PostMapping("/signup")
//	@ResponseBody
//	public ResponseEntity<?> signup(@RequestBody SignupReqDto signupReqDto) {
//		// １。
//		return ResponseEntity.ok(usersService.signup(signupReqDto)); //　ジョインするメソッドを呼び出してbollean型でデータを返されてジョイン成功場合はTURE、失敗場合はFALSEです。
//	}
	@PostMapping("/signup")
	@ResponseBody
	public ResponseEntity<?> signup(@RequestBody SignupReqDto signupReqDto) {
	    try {
	        boolean result = usersService.signup(signupReqDto);
	        return ResponseEntity.ok(result); // true
	    } catch (SignupValidationException e) {
	    	return ResponseEntity
	    		    .status(HttpStatus.BAD_REQUEST)
	    		    .contentType(MediaType.valueOf("text/plain;charset=UTF-8"))
	    		    .body(e.getMessage());
	    }
	}
	
	/**
	 * ユーザー名の重複チェックを行うメソッドです。
	 * ジョイン時にユーザー名が既に使用されているかどうかを確認します。
	 * 
	 * @param username チェックするユーザー名
	 * @return ユーザー名が既に存在するかどうかのBooleanタイプで結果渡す
	 */
	@GetMapping("/signup/{username}")
	@ResponseBody
	public ResponseEntity<?> existsUsername(@PathVariable String username) {
	    return ResponseEntity.ok(usersService.checkUsername(username));
	}
	
	/**
	 * ジョインページを表示するメソッドです。
	 * ジョインページのビューを表示します。
	 * 
	 * @param model モデル
	 * @return サインアップページのビュー名
	 */
	@GetMapping("/signup")
	public String signup(Model model) {
		return "users/signup";
	}
	
}
