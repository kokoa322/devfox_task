package com.example.board.aspect;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@Aspect
public class CheckSessionAspect {
	
	/**
	 * セッションが存在し、ユーザーがログインしているかを確認するメソッドです。
	 * 
	 * セッションにユーザー情報が存在しない場合、ログインページにリダイレクトします。
	 * ログインしている場合は、モデルにユーザー名を追加し、元のメソッドを実行します。
	 *
	 * @param joinPoint メソッド実行のコンテキスト
	 * @return セッションが有効であれば元のメソッドを実行し、セッションが無効であればリダイレクトを返します。
	 */
	 @Around("@annotation(RequireSession)")
	    public Object RequireSession(ProceedingJoinPoint joinPoint) throws Throwable {
	        Object[] args = joinPoint.getArgs();//　伝達されたすべてのデータをObject[]配列で返します。
	        HttpSession session = null;
	        HttpServletResponse response = null;

	        //　HttpSessionと HttpServletResponseを探して保持します。
	        for (Object arg : args) {
	            if (arg instanceof HttpSession) {	//　argがHttpSession タイプのオブジェクトであることを確認しながら探します。
	                session = (HttpSession) arg;
	            } else if (arg instanceof HttpServletResponse) {
	                response = (HttpServletResponse) arg;
	            }
	        }

	        // セッションがNullまたはユーザーにセッションがない場合です。
	        if (session == null || session.getAttribute("user") == null) {
	            if (response != null) {
	                response.sendRedirect("/users/signin"); // リダイレクト指定します。
	                return null; // リダイレクト後、メソッドの実行を止めるためにnullを返します。
	            }
	            return "redirect:/users/signin"; // リダイレクト指定します。
	        }

	       //  ユーザーがセッションにある場合、モデルにユーザー名を追加します。
	        String username = (String) session.getAttribute("user");
	        for (Object arg : args) {
	            if (arg instanceof Model) {
	            	((Model) arg).addAttribute("username", username);
	            	((Model) arg).addAttribute("user", username);
	            }
	        }

	        // 元のメソッド実行します。
	        return joinPoint.proceed();
	    }
	

	 /**
	  * セッションにユーザー名を追加するメソッドです。
	  * 
	  * メソッドの引数にHttpSessionが含まれている場合、セッションからユーザー名を取得し、
	  * モデルに追加します。ユーザー名がnullでない場合のみ追加されます。
	  *
	  * @param joinPoint メソッド実行のコンテキスト
	  * @return メソッドをそのまま実行します。
	  */
    @Around("@annotation(CheckSession)")
    public Object CheckSessionModel(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();	//　伝達されたすべてのデータをObject[]配列で返します。
        HttpSession session = null;

     //　HttpSessionと HttpServletResponseを探して保持します。
        for (Object arg : args) {
            if (arg instanceof HttpSession) {	//　argがHttpSession タイプのオブジェクトであることを確認しながら探します。
                session = (HttpSession) arg;
                break;
            }
        }

        if (session != null) {	//ユーザーがセッションにある場合は。ログを残します。
        	String methodName = joinPoint.getSignature().getName();
        	String username = (String) session.getAttribute("user");
        	if (username != null) { log.info("{} -> {}", username, methodName); }
        	
            // モデルにユーザー名を追加します。
            for (Object arg : args) {
                if (arg instanceof Model && username != null) {
                	((Model) arg).addAttribute("username", username);
                	((Model) arg).addAttribute("user", username);
                }
            }
        }

        // 원래의 메소드 실행
        return joinPoint.proceed();
    }

}

