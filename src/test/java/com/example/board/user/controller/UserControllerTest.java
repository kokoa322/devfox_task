package com.example.board.user.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;

import com.example.board.globalException.SignupValidationException;
import com.example.board.globalException.SignupValidator;
// SignupValidator는 이 테스트에서 직접 사용하지 않으므로, 주석 처리하거나 제거할 수 있습니다.
// import com.example.board.globalException.SignupValidator;
import com.example.board.users.controller.UserController;
import com.example.board.users.dao.UsersDao;
import com.example.board.users.dto.req.SigninReqDto;
// UsersDao도 이 테스트에서는 직접 사용하지 않습니다.
// import com.example.board.users.dao.UsersDao;
import com.example.board.users.dto.req.SignupReqDto;
import com.example.board.users.dto.res.SigninResDto;
import com.example.board.users.service.UsersService; // UsersService는 Mock으로 필요합니다.

// @Transactional // 컨트롤러 단위 테스트에는 보통 필요하지 않습니다. 제거 권장.
public class UserControllerTest {

    // UsersDao, SignupValidator는 이 테스트에서 직접 Mocking할 필요가 없습니다.
    // usersService만 Mocking하여 UserController에 주입하면 됩니다.
     @Mock
     private UsersDao usersDao;

    // @Mock
    // private SignupValidator signupValidator;
	
	private SignupValidator signupValidator; // 실제 SignupValidator 인스턴스
	
    @Mock
    private UsersService usersService; // UserController의 의존성인 UsersService를 Mocking합니다.

    @InjectMocks
    private UserController userController; // 실제 UserController에 Mock UsersService를 주입합니다.
    
    @Mock
    private HttpSession session;

    @Mock
    private Model model;
    
    @Mock
    private HttpServletResponse response;

    @Captor
    ArgumentCaptor<Cookie> cookieCaptor;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Mock 객체 초기화

        // 실제 SignupValidator 인스턴스를 생성하고, Mocking한 UsersDao를 주입합니다.
        // 이렇게 해야 signupValidator.validate() 메소드 내에서 usersDao를 호출하는 부분이 NullPointerException을 발생시키지 않습니다.
        this.signupValidator = new SignupValidator(usersDao);

        // signupValidator의 validate 메소드 내에서 checkUsername, checkEmail 메소드가 호출될 수 있으므로
        // 이들이 예상치 못한 예외를 던지거나 테스트 흐름을 방해하지 않도록 기본 동작을 설정합니다.
        // 이 테스트는 비밀번호 유효성 검증에 집중하므로, 중복 검사는 실패하지 않도록 설정합니다.
        when(usersDao.existsUsername(anyString())).thenReturn(false);
        when(usersDao.existsEmail(anyString())).thenReturn(false);
    }

    @Test
    @DisplayName("회원가입 - 정상")
    void 회원가입_성공_테스트() {
        SignupReqDto req = new SignupReqDto();
        req.setUsername("kokoa223");
        req.setPassword("Skills12##");
        req.setConfirmPassword("Skills12##");
        req.setEmail("tjsgus223@naver.com");

        // usersService.signup()이 호출되면 true를 반환하도록 Mock 설정
        when(usersService.signup(req)).thenReturn(true);

        ResponseEntity<?> response = userController.signup(req);

        // 컨트롤러의 응답 확인
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(true, response.getBody());

        // usersService.signup() 메소드가 정확히 1번 호출되었는지 검증
        verify(usersService, times(1)).signup(req);
    }

    @Test
    @DisplayName("비밀번호 최소 길이 미달 시 SignupValidationException 발생 및 정확한 메시지 반환")
    void validate_passwordMinLength_throwsException() {
        // 1. 
        SignupReqDto req = new SignupReqDto();
        req.setUsername("validUser");
        req.setPassword("short1!"); 
        req.setConfirmPassword("short1!");
        req.setEmail("valid@example.com");

        // 2. 예상 결과: 발생할 예외 메시지
        String expectedErrorMessage = "비밀번호는 최소 8자 이상 특수문자 포함되어야 합니다。";

        // 3. 실제 SignupValidator의 validate 메소드를 호출하여 예외 발생 여부 및 메시지를 검증
        // assertThrows를 사용하여 람다식 내에서 메소드를 호출하고, 특정 예외가 던져지는지 확인
        SignupValidationException exception = assertThrows(SignupValidationException.class, () -> {
            signupValidator.validate(req); // <--- 실제 validate 메소드 실행
        });

        // 4. 예외 메시지가 예상과 일치하는지 확인
        assertEquals(expectedErrorMessage, exception.getMessage());
    }
    
 // 추가적으로 비밀번호가 8자리 이상이고 유효한 경우의 성공 테스트도 추가할 수 있습니다.
    @Test
    @DisplayName("비밀번호가 유효한 경우 예외가 발생하지 않음")
    void validate_passwordValid_noException() {
        SignupReqDto req = new SignupReqDto();
        req.setUsername("validUser");
        req.setPassword("ValidPa!"); // 8자리 이상, 유효한 비밀번호
        req.setConfirmPassword("ValidPa!");
        req.setEmail("valid@example.com");

        // 예외가 발생하지 않는 것을 확인합니다.
        // assertDoesNotThrow는 특정 코드가 예외를 던지지 않는지 검증할 때 사용합니다.
        assertDoesNotThrow(() -> {
            signupValidator.validate(req);
        });
    }
    

    @Test
    void signup_비밀번호불일치_예외발생() {
        // given
        SignupReqDto req = new SignupReqDto();
        req.setUsername("testUser");
        req.setPassword("Password!1");
        req.setConfirmPassword("DifferentPassword!2"); // 일치하지 않도록 설정
        req.setEmail("test@example.com");

        doThrow(new SignupValidationException("비밀번호가 일치하지 않습니다。"))
            .when(usersService).signup(any(SignupReqDto.class));

        // when
        ResponseEntity<?> response = userController.signup(req);

        // then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("비밀번호가 일치하지 않습니다。", response.getBody());
    }
    

    @Test
    void signup_비밀번호에_특수문자_없을때_예외발생() {
        // given
        SignupReqDto req = new SignupReqDto();
        req.setUsername("testUser");
        req.setPassword("Password123"); // 특수문자 없음
        req.setConfirmPassword("Password123");
        req.setEmail("test@example.com");

        doThrow(new SignupValidationException("비밀번호는 최소 8자 이상 특수문자 포함되어야 합니다。"))
            .when(usersService).signup(any(SignupReqDto.class));

        // when
        ResponseEntity<?> response = userController.signup(req);

        // then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("비밀번호는 최소 8자 이상 특수문자 포함되어야 합니다。", response.getBody());
    }
    

    @Test
    void signup_이미존재하는이메일_예외발생() {
        // given
        SignupReqDto req = new SignupReqDto();
        req.setUsername("newUser");
        req.setPassword("Password!1");
        req.setConfirmPassword("Password!1");
        req.setEmail("duplicate@example.com"); // 이미 존재하는 이메일

        doThrow(new SignupValidationException("해당 이메일로 이미 가입된 회원이 존재합니다。"))
            .when(usersService).signup(any(SignupReqDto.class));

        // when
        ResponseEntity<?> response = userController.signup(req);

        // then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("해당 이메일로 이미 가입된 회원이 존재합니다。", response.getBody());
    }
    

    @Test
    void signup_잘못된이메일형식_예외발생() {
        // given
        SignupReqDto req = new SignupReqDto();
        req.setUsername("testUser");
        req.setPassword("Password!1");
        req.setConfirmPassword("Password!1");
        req.setEmail("invalid-email-format"); // '@' 없음

        doThrow(new SignupValidationException("이메일 형식이 잘못되었습니다。"))
            .when(usersService).signup(any(SignupReqDto.class));

        // when
        ResponseEntity<?> response = userController.signup(req);

        // then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("이메일 형식이 잘못되었습니다。", response.getBody());
    }
    
    @Test
    void signin_정상로그인_성공리다이렉트() {
        // given
        SigninReqDto req = new SigninReqDto();
        req.setUsername("testUser");
        req.setPassword("Password!1");

        SigninResDto res = new SigninResDto();
        res.setId(1L);
        res.setUsername("testUser");

        when(usersService.signin(any(SigninReqDto.class))).thenReturn(res);

        // when
        String viewName = userController.signiUser(req, session, model);

        // then
        assertEquals("redirect:/boards", viewName);
        verify(session).setAttribute("user", "testUser");
        verify(session).setAttribute("user_id", 1L);
        verify(session).setMaxInactiveInterval(3600);
    }
    
    @Test
    void signin_잘못된비밀번호_로그인실패() {
        // given
        SigninReqDto req = new SigninReqDto();
        req.setUsername("testUser");
        req.setPassword("wrongPassword");

        when(usersService.signin(any(SigninReqDto.class))).thenReturn(null); // 로그인 실패

        // when
        String viewName = userController.signiUser(req, session, model);

        // then
        assertEquals("users/signin", viewName);
        verify(model).addAttribute("errorMessage", "아이디 또는 비밀번호가 일치하지 않습니다.");
    }

    
    @Test
    void signin_존재하지않는아이디_로그인실패() {
        // given
        SigninReqDto req = new SigninReqDto();
        req.setUsername("nonExistentUser");
        req.setPassword("Password!1");

        when(usersService.signin(any(SigninReqDto.class))).thenReturn(null); // 사용자 없음

        // when
        String viewName = userController.signiUser(req, session, model);

        // then
        assertEquals("users/signin", viewName);
        verify(model).addAttribute("errorMessage", "아이디 또는 비밀번호가 일치하지 않습니다.");
    }

    
    @Test
    void signin_아이디공백_로그인실패() {
        // given
        SigninReqDto req = new SigninReqDto();
        req.setUsername("  "); // 공백
        req.setPassword("Password!1");

        when(usersService.signin(any(SigninReqDto.class))).thenReturn(null); // 공백도 실패

        // when
        String viewName = userController.signiUser(req, session, model);

        // then
        assertEquals("users/signin", viewName);
        verify(model).addAttribute("errorMessage", "아이디 또는 비밀번호가 일치하지 않습니다.");
    }

    
    @Test
    void signin_비밀번호공백_로그인실패() {
        // given
        SigninReqDto req = new SigninReqDto();
        req.setUsername("testUser");
        req.setPassword(" "); // 공백

        when(usersService.signin(any(SigninReqDto.class))).thenReturn(null);

        // when
        String viewName = userController.signiUser(req, session, model);

        // then
        assertEquals("users/signin", viewName);
        verify(model).addAttribute("errorMessage", "아이디 또는 비밀번호가 일치하지 않습니다.");
    }
    
    @Test
    void logout_세션무효화와쿠키삭제_정상리다이렉트() {
        // when
        String viewName = userController.logout(session, response);

        // then
        // 세션 무효화 호출 확인
        verify(session).invalidate();

        // 쿠키 삭제 확인
        verify(response).addCookie(cookieCaptor.capture());
        Cookie deletedCookie = cookieCaptor.getValue();

        assertEquals("JSESSIONID", deletedCookie.getName());
        assertNull(deletedCookie.getValue());
        assertEquals(0, deletedCookie.getMaxAge());
        assertEquals("/", deletedCookie.getPath());

        // 리다이렉트 확인
        assertEquals("redirect:/boards", viewName);
    }
}