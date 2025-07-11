<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Sign Up</title>
    <link rel="stylesheet" href="/resources/static/css/signup.css">
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
</head>
<body>

<!-- ジョインページのコンテナ -->
<div class="signup-container">
    <h2>Sign Up</h2>
    <!--ジョインフォーム -->
    <form class="signup-form" id="signupForm">
        <div class="input-group">
            <label for="username">ID(닉네임)</label>
            <!-- ユーザー名入力フィールド -->
            <input type="text" id="username" name="username" required>
            <button type="button" id="checkUsernameBtn" class="check-btn">아이디 중복 확인</button>
            <span id="usernameMessage"></span>
        </div>
        <div class="input-group">
        	<!-- パスワード入力フィールド -->
            <label for="password">비밀번호</label>
            <input type="password" id="password" name="password" required>
        </div>
        <div class="input-group">
        	<!-- パスワード確認入力フィールド -->
            <label for="confirmPassword">비밀번호 확인</label>
            <input type="password" id="confirmPassword" name="confirmPassword" required>
        </div>
        <div class="input-group">
        	<!-- メールアドレス入力フィールド -->
            <label for="email">이메일</label>
            <input type="email" id="email" name="email" required>
        </div>
        <!-- サインアップ送信ボタン -->
        <button type="submit" class="signup-btn">회원가입</button>
    </form>
</div>

<script>
    $(document).ready(function () {
        // フォーム送信時の処理
        $('#signupForm').submit(function (event) {
            event.preventDefault(); // デフォルトのフォーム送信を防止

            // 入力された値を取得
            const username = $('#username').val();
            const password = $('#password').val();
            const confirmPassword = $('#confirmPassword').val();
            const email = $('#email').val();

            // パスワード確認が一致しない場合
            if (password !== confirmPassword) {
                alert("비밀번호가 일치하지 않습니다!"); 
                return;
            }

            // メールアドレス形式を検証
            let reg_email = /^([\w-]+(?:\.[\w-]+)*)@((?:[\w-]+\.)*\w[\w-]{0,66})\.([a-z]{2,6}(?:\.[a-z]{2})?)$/;
            if (!reg_email.test(email)) {
                alert("이메일 형식이 맞지 않습니다!"); 
                $('#email').focus();
                return;
            }

            // サーバーへサインアップデータを送信
            $.ajax({
                url: '/users/signup',
                type: 'POST',
                contentType: 'application/json',
                data: JSON.stringify({
                    username: username,
                    password: password,
                    confirmPassword: confirmPassword,
                    email: email
                }),
                success: function (response) {
                    if (response === true) {
                        // 회원가입 성공 시 게시판 페이지로 이동
                        window.location.href = '/boards';
                    } else {
                        // 서버에서 true가 아닌 응답을 줬을 경우 (예외 처리 미사용 로직일 때)
                        alert('회원가입에 실패했습니다. 다시 시도해주세요。');
                    }
                },
                error: function (xhr, error) {
                    // 서버에서 예외 메시지 응답을 준 경우 (커스텀 예외 처리)
                    if (xhr.status === 400) {
                        alert(xhr.responseText);  // 서버에서 보내준 에러 메시지 그대로 표시
                    } else {
                        alert('회원가입에 실패했습니다. 다시 시도해주세요。');
                    }
                }
            });
        });
    });
    
</script>

<script>
    $(document).ready(function() {
        // ユーザー名の重複確認ボタンのクリックイベント
        $('#checkUsernameBtn').click(function() {
            var username = $('#username').val();
            
            // ユーザー名が空の場合の処理
            if (username.trim() === "") {
                $('#usernameMessage').text('아이디를 입력하세요。').css('color', 'red'); 
                return;
            }
            
            // サーバーへユーザー名の確認リクエスト
            $.ajax({
                url: '/users/signup/' + username, // PathVariable 形式
                type: 'GET',
                success: function(response) {
                    if (!response) {
                        $('#usernameMessage').text('사용 가능한 아이디입니다。').css('color', 'green'); 
                    } else {
                        $('#usernameMessage').text('이미 사용 중인 아이디입니다。').css('color', 'red'); 
                    }
                },
                error: function(xhr, error) {
                    if (xhr.status === 500) {
                        alert('서버 오류: ' + xhr.responseText); 
                    } else {
                        alert('중복확인에 실패했습니다. 다시 시도해주세요。'); 
                    }
                }
            });
        });
    });
</script>

</body>
</html>
