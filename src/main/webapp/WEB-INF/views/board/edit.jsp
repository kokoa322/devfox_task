<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>게시글 수정</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f5f5f7;
            color: #333;
            margin: 0;
            padding: 0;
        }

        .edit-container {
            background-color: white;
            width: 60%;
            margin: 50px auto;
            padding: 20px;
            border-radius: 8px;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
        }

        .edit-container h2 {
            text-align: center;
            color: #333;
        }

        .edit-form label {
            display: block;
            font-weight: bold;
            margin-top: 15px;
        }

        .edit-form input[type="text"],
        .edit-form textarea {
            width: 100%;
            padding: 10px;
            border-radius: 5px;
            border: 1px solid #ccc;
            margin-bottom: 20px;
            font-size: 14px;
        }

        .edit-form button {
            background-color: #007aff;
            color: white;
            padding: 10px 20px;
            border: none;
            border-radius: 5px;
            font-size: 16px;
            cursor: pointer;
            display: block;
            margin: 0 auto;
        }

        .edit-form button:hover {
            background-color: #005bb5;
        }
    </style>
</head>
<body>
<!-- 編集コンテナ -->
<div class="edit-container">
    <!-- 編集ページのタイトル -->
    <h2>게시글 수정</h2> <!-- 投稿の編集 -->

    <!-- 編集フォーム -->
    <form class="edit-form" action="/boards/edit" method="POST">
        <!-- タイトルのラベルと入力フィールド -->
        <label for="title">제목</label>
        <input type="text" id="title" name="title" value="${response.title}" />

        <!-- 内容のラベルとテキストエリア -->
        <label for="content">내용</label>
        <textarea id="content" name="content" rows="10">${response.content}</textarea>

        <!-- 隠しフィールド（投稿IDとユーザー名） -->
        <input type="hidden" name="id" value="${response.id}"/>
        <input type="hidden" name="username" value="${response.username}"/>

        <!-- 編集完了ボタン -->
        <button type="submit">수정 완료</button>
    </form>
</div>
</body>

</html>
