<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>글 작성하기</title>
    <link rel="stylesheet" href="/resources/static/css/boardWrite.css">
</head>
<body>
    <h1>글 작성하기</h1>
    <!-- フォームの開始 -->
    <form action="/boards/write" method="post">
        <!-- 작성자 (作成者) -->
        <div>
            <label for="username">작성자:</label>
            <!-- セッションからユーザー名を取得し、読み取り専用フィールドに表示 -->
            <input type="text" id="username" name="username" placeholder="작성자 이름을 입력하세요" value="${sessionScope.user}" required readonly>
        </div>
        
        <!-- 제목 (タイトル) -->
        <div>
            <label for="title">제목:</label>
            <!-- タイトル入力フィールド -->
            <input type="text" id="title" name="title" maxlength="255" placeholder="제목을 입력하세요" required>
        </div>
        
        <!-- 내용 (内容) -->
        <div>
            <label for="content">내용:</label>
            <!-- 内容入力フィールド -->
            <textarea id="content" name="content" rows="10" cols="50" placeholder="내용을 입력하세요" required></textarea>
        </div>
        
        <!-- 버튼 (ボタン) -->
        <div>
            <!-- 送信ボタン -->
            <button type="submit">작성하기</button>
            <!-- リセットボタン -->
            <button type="reset">초기화</button>
        </div>
    </form>
</body>
</html>
