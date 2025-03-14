<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Login</title>
    
    <style>
    /* 기본적인 페이지 스타일 */
body {
    font-family: Arial, sans-serif;
    background-color: #f0f4f8;
    margin: 0;
    padding: 0;
    display: flex;
    justify-content: center;
    align-items: center;
    height: 100vh;
}

.login-container {
    background-color: #fff;
    padding: 40px;
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
    border-radius: 12px;
    width: 100%;
    max-width: 420px;
}

h2 {
    text-align: center;
    margin-bottom: 30px;
    font-size: 28px;
    color: #333;
}

/* 폼 컨테이너 */
.login-form {
    display: flex;
    flex-direction: column;
}

/* 입력 필드 스타일 */
.input-group {
    margin-bottom: 20px;
}

.input-group label {
    font-size: 14px;
    color: #555;
    margin-bottom: 8px;
    display: block;
}

.input-group input {
    width: 100%;
    padding: 15px;
    font-size: 16px;
    border: 1px solid #ddd;
    border-radius: 30px; /* 둥근 모서리 */
    background-color: #f8f9fa;
    transition: border-color 0.3s, box-shadow 0.3s;
}

.input-group input:focus {
    border-color: #6c63ff;
    box-shadow: 0 0 8px rgba(108, 99, 255, 0.2);
    outline: none;
}

/* 버튼 스타일 */
button {
    padding: 14px;
    background: linear-gradient(45deg, #6c63ff, #5048e5); /* 그라데이션 버튼 */
    color: #fff;
    font-size: 16px;
    border: none;
    border-radius: 30px; /* 둥근 모서리 */
    cursor: pointer;
    transition: background 0.3s ease, transform 0.2s ease;
}

button:hover {
    background: linear-gradient(45deg, #5048e5, #6c63ff); /* 그라데이션 색상 반전 */
    transform: scale(1.05); /* 클릭 시 버튼 살짝 커지기 */
}

/* 아이디 중복 확인 버튼 스타일 */
.check-btn {
    background-color: #28a745;
    margin-top: 10px;
    padding: 12px;
    font-size: 16px;
    border-radius: 30px;
    color: white;
}

.check-btn:hover {
    background-color: #218838;
}

/* 아이디 중복 확인 메시지 */
#usernameMessage {
    font-size: 14px;
    font-weight: bold;
    margin-top: 5px;
}

/* 로그인 버튼 */
.login-btn {
    background: linear-gradient(45deg, #6c63ff, #5048e5);
}

.login-btn:hover {
    background: linear-gradient(45deg, #5048e5, #6c63ff);
}

/* 회원가입 버튼 */
.signup-btn {
    padding: 14px;
    background: linear-gradient(45deg, #28a745, #218838); /* 그린 그라데이션 버튼 */
    color: #fff;
    font-size: 16px;
    border: none;
    border-radius: 30px; /* 둥근 모서리 */
    cursor: pointer;
    transition: background 0.3s ease, transform 0.2s ease;
    margin-top: 10px; /* 로그인 버튼과 간격 */
    width: 100%; /* 로그인 버튼과 동일한 너비로 맞추기 */
}

.signup-btn:hover {
    background: linear-gradient(45deg, #218838, #28a745); /* 그라데이션 색상 반전 */
    transform: scale(1.05); /* 클릭 시 버튼 살짝 커지기 */
}

/* 폼 오류 메시지 스타일 */
.alert {
    color: red;
    font-size: 14px;
    text-align: center;
    margin-top: 10px;
}

/* 작은 화면에 대응하기 위한 미디어 쿼리 */
@media (max-width: 480px) {
    .login-container {
        padding: 20px;
        width: 90%;
    }

    h2 {
        font-size: 22px;
    }

    .input-group input {
        font-size: 14px;
    }

    button {
        font-size: 14px;
        padding: 10px;
    }
}
    
    </style>
</head>
<body>

    <div class="login-container">
        <h2>Login</h2>
        <!-- ログインフォーム -->
        <form action="/users/signin" method="POST" class="login-form" id="loginForm">
            <div class="input-group">
                <label for="username">ID(닉네임)</label>
                <input type="text" id="username" name="username" required>
            </div>
            <div class="input-group">
                <label for="password">비밀번호</label>
                <input type="password" id="password" name="password" required>
            </div>
            <button type="submit" class="login-btn">로그인</button>
            <!-- エラーメッセージを表示 -->
            <c:if test="${not empty errorMessage}">
                <div style="color: red;">
                    <p>아이디 또는 비밀번호가 잘못되었습니다。</p>
                </div>
            </c:if>
        </form>

        <!-- 회원가입 버튼 -->
        <form action="/users/signup" method="GET">
            <button type="submit" class="signup-btn">회원가입</button>
        </form>
    </div>

</body>
</html>
