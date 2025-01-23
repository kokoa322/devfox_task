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
	        Object[] args = joinPoint.getArgs();
	        HttpSession session = null;
	        HttpServletResponse response = null;

	        // HttpSession과 HttpServletResponse를 찾아 저장
	        for (Object arg : args) {
	            if (arg instanceof HttpSession) {
	                session = (HttpSession) arg;
	            } else if (arg instanceof HttpServletResponse) {
	                response = (HttpServletResponse) arg;
	            }
	        }

	        // session이 null이거나 user가 세션에 없는 경우
	        if (session == null || session.getAttribute("user") == null) {
	            if (response != null) {
	                response.sendRedirect("/users/signin"); // 리다이렉트 처리
	                return null; // 리다이렉트 후 메소드 실행을 멈추기 위해 null 반환
	            }
	            return "redirect:/users/signin"; // 리다이렉트 문자열 반환
	        }

	       //  "user"가 세션에 있으면 모델에 username 추가
	        String username = (String) session.getAttribute("user");
	        for (Object arg : args) {
	            if (arg instanceof Model) {
	                ((Model) arg).addAttribute("username", username);
	            }
	        }

	        // 원래의 메소드 실행
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
    @Around("@annotation(CheckSession)") // AddUserToModel 어노테이션이 붙은 메소드에만 적용
    public Object CheckSessionModel(ProceedingJoinPoint joinPoint) throws Throwable {
        // joinPoint에서 HttpSession을 직접 받아옴
        Object[] args = joinPoint.getArgs();
        HttpSession session = null;

        // HttpSession을 메소드 인자에서 찾기
        for (Object arg : args) {
            if (arg instanceof HttpSession) {
                session = (HttpSession) arg;
                break;
            }
        }

        if (session != null) {
        	
        	String methodName = joinPoint.getSignature().getName();
        	String username = (String) session.getAttribute("user");
        	if (username != null) { log.info("{} -> {}", username, methodName); }
        	
            // 모델에 username 추가 (첫 번째 인자가 Model 타입일 때)
            for (Object arg : args) {
                if (arg instanceof Model && username != null) {
                    ((Model) arg).addAttribute("username", username);
                }
            }
        }

        // 원래의 메소드 실행
        return joinPoint.proceed();
    }
    
   



}

