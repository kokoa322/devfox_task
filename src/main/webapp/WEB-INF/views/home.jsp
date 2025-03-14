<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <script src="https://code.jquery.com/jquery-3.6.4.min.js"></script>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>세션 연장</title>
    
    <!-- CSS 스타일 -->
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 0;
            padding: 0;
            background-color: #f4f4f4;
        }
        nav {
            background-color: #333;
            color: #fff;
            padding: 10px 20px;
            display: flex;
            justify-content: space-between;
            align-items: center;
        }
        nav a {
            color: white;
            text-decoration: none;
            margin: 0 15px;
        }
        nav a:hover {
            text-decoration: underline;
        }
        .header-content {
            display: flex;
            justify-content: space-between;
            align-items: center;
            width: 100%;
        }
        .header-content p {
            color: white;
            margin: 0;
        }
        #timer {
            font-size: 16px;
            color: #fff;
        }
        #extend-session-btn {
            padding: 10px 20px;
            background-color: #4CAF50;
            color: white;
            border: none;
            cursor: pointer;
            display: none;
        }
        #extend-session-btn:hover {
            background-color: #45a049;
        }
    </style>
    
    <!-- JS 스크립트 -->
    <script>
    $(document).ready(function() {
        $('#delete-account-btn').click(function() {
            const username = $('#username').val(); // 현재 사용자 이름
            const password = $('#password').val(); // 입력한 비밀번호

            if (!password) {
                alert("비밀번호를 입력하세요.");
                return; // 비밀번호가 없으면 요청 중단
            }

            if (confirm("정말 계정을 삭제하시겠습니까?")) {
                $.ajax({
                    url: '/users/', // 백엔드 URL
                    type: 'DELETE', // DELETE 요청
                    contentType: 'application/json', // JSON 형식으로 전송
                    data: JSON.stringify({ 
                        username: username, 
                        password: password 
                    }), // JSON 데이터로 전송
                    success: function(response) {
                        alert("계정이 삭제되었습니다.");
                        window.location.href = "/"; // 삭제 성공 후 홈으로 이동
                    },
                    error: function(xhr, status, error) {
                        alert("계정 삭제에 실패했습니다.");
                        console.error("삭제 실패:", error);
                    }
                });
            }
        });
    });

    let sessionTimer;
    let timeRemaining = 0;
    let timerDisplay = document.getElementById("timer");
    const extendSessionButton = document.getElementById("extend-session-btn");

    function checkSessionTime() {
        fetch("/sessionTimer")
            .then(response => response.json())  
            .then(data => {
                timeRemaining = data; 
                updateTimerDisplay(timeRemaining);

                if (timeRemaining <= 60 && timeRemaining > 0) {
                    extendSessionButton.style.display = "inline";
                }

                if (timeRemaining <= 0) {
                    clearInterval(sessionTimer);
                    alert("세션이 만료되었습니다.");
                    window.location.href = "/users/signout"; 
                }
            })
            .catch(error => {
                console.error("세션 시간 조회 실패:", error);
                clearInterval(sessionTimer); 
            });
    }

    function updateTimerDisplay(time) {
        const minutes = Math.floor(time / 60);
        const seconds = time % 60;

        if (timerDisplay) {
            timerDisplay.textContent = `남은 시간: ` + minutes + `분 ` + seconds + `초`;
        }
    }

    function extendSession() {
        fetch("/extendSession", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            }
        })
        .then(response => {
            if (response.ok) {
                alert("세션이 연장되었습니다.");
                extendSessionButton.style.display = "none";
                timeRemaining = 60 * 60;  
                updateTimerDisplay(timeRemaining); 
            } else {
                alert("세션 연장에 실패했습니다.");
            }
        })
        .catch(error => {
            console.error("연장 요청 실패:", error);
        });
    }

    window.onload = function() {
        timerDisplay = document.getElementById("timer");
        if (timerDisplay) {
            checkSessionTime(); 
            sessionTimer = setInterval(checkSessionTime, 1000); 
        } else {
            console.error("timer 요소를 찾을 수 없습니다.");
        }
    };
    </script>
</head>
<body>
    <nav>
        <div class="header-content">
            <c:if test="${empty username}">
                <a href="/users/signin">로그인</a>
            </c:if>
            <c:if test="${not empty username}">
                <a href="/users/signout">로그아웃</a>
                <div>
                    <input type="hidden" id="username" value="<c:out value='${username}' />" />
                    <label for="password">비밀번호 입력:</label>
                    <input type="password" id="password" placeholder="비밀번호를 입력하세요" />
                    <button id="delete-account-btn">계정 삭제</button>
                </div>
            </c:if>
            
            <span id="alarmCount">0</span>
            <button id="alarmButton">알림 확인</button>
            <c:if test="${not empty username}">
                <p>${username}</p>
                <div id="timer">남은 시간: 0분 0초</div>
                <button id="extend-session-btn" style="display: none;" onclick="extendSession()">세션 연장</button>
            </c:if>
            
    </nav>
    
   <!-- 알림 숫자 표시 -->

<script>



document.addEventListener("DOMContentLoaded", function() {
	
	document.getElementById("alarmButton").addEventListener("click", function() {
	    alarmCountElement.textContent = "0";
	    localStorage.setItem("alarmCount", "0"); // 저장된 값도 초기화
	});
	
    const userId = "${sessionScope.user_id}";

    if (!userId) {
        console.error("사용자 ID가 없습니다.");
        return;
    }

    const eventSource = new EventSource("/boards/sse/subscribe/" + userId);
    
    const alarmCountElement = document.getElementById("alarmCount");
    let alarmCount = localStorage.getItem("alarmCount");
    if (alarmCount === null) {
        alarmCount = 0;
    } else {
        alarmCount = parseInt(alarmCount, 10);
    }
    
    alarmCountElement.textContent = alarmCount;

    eventSource.onmessage = function(event) {
        try {
            
            alert("댓글이 등록 되었습니다");

            alarmCount++;
            alarmCountElement.textContent = alarmCount;
            localStorage.setItem("alarmCount", alarmCount);
        } catch (error) {
            console.error("SSE 메시지 파싱 오류:", error);
        }
    };

    eventSource.onerror = function(event) {
        console.error("SSE 연결이 종료되었습니다.");
    };
});

</script>

    
</body>
</html>
