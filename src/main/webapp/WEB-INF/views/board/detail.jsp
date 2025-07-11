<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%@ page import="java.time.LocalDateTime" %>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import="java.util.Date"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="com.example.board.board.util.Paging"%>

<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>게시판 디테일</title>
    <style>
        /* 페이지네이션 스타일 */
        .pagination {
            display: flex;
            justify-content: center;
            align-items: center;
            margin-top: 20px;
        }

        .pagination a {
            display: inline-block;
            padding: 8px 15px;
            margin: 0 5px;
            border: 1px solid #007aff;
            border-radius: 5px;
            color: #007aff;
            font-size: 14px;
            text-decoration: none;
            background-color: white;
            transition: background-color 0.3s, transform 0.2s ease;
        }

        .pagination a:hover {
            background-color: #007aff;
            color: white;
            transform: scale(1.1); /* Hover 시 버튼이 약간 커지도록 */
        }

        .pagination a.active {
            background-color: #007aff;
            color: white;
            pointer-events: none; /* 현재 페이지는 클릭할 수 없도록 설정 */
            font-weight: bold;
        }

        .pagination a:first-child,
        .pagination a:last-child {
            font-weight: bold;
        }

        .pagination a:disabled {
            background-color: #f0f0f0;
            color: #ccc;
            cursor: not-allowed;
        }

        /* 기존 스타일 유지 */
        body {
            font-family: Arial, sans-serif;
            background-color: #f5f5f7;
            color: #333;
            margin: 0;
            padding: 0;
        }

        h1, h2, h3 {
            color: #333;
            text-align: center;
        }

        .board-detail {
            background-color: white;
            width: 80%;
            margin: 20px auto;
            padding: 20px;
            border-radius: 8px;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
        }

        .board-detail h2 {
            font-size: 24px;
            margin-bottom: 10px;
        }

        .content {
            margin: 15px 0;
            font-size: 16px;
            line-height: 1.6;
        }

        .info {
            font-size: 14px;
            color: #666;
        }

        .info div {
            margin-bottom: 8px;
        }

        .comments-section {
            background-color: white;
            width: 80%;
            margin: 20px auto;
            padding: 20px;
            border-radius: 8px;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
        }

        .comment {
            background-color: #f9f9f9;
            margin-bottom: 15px;
            padding: 10px;
            border-radius: 8px;
            box-shadow: 0 0 5px rgba(0, 0, 0, 0.1);
        }

        .comment .username {
            font-weight: bold;
            color: #007aff;
        }

        .comment .timestamp {
            font-size: 12px;
            color: #999;
            margin-bottom: 5px;
        }

        .comment .content {
            font-size: 14px;
            line-height: 1.5;
        }

        .comment form {
            display: inline-block;
            margin-right: 10px;
        }

        .comment-form textarea {
            width: 100%;
            padding: 10px;
            border-radius: 5px;
            border: 1px solid #ccc;
            margin-bottom: 10px;
            font-size: 14px;
        }

        .comment-form button {
            background-color: #007aff;
            color: white;
            padding: 8px 15px;
            border: none;
            border-radius: 5px;
            font-size: 14px;
            cursor: pointer;
        }

        .comment-form button:hover {
            background-color: #005bb5;
        }

        .delete-button {
            background-color: #ff4d4d;
            color: white;
            padding: 5px 10px;
            border: none;
            border-radius: 5px;
            cursor: pointer;
        }

        .delete-button:hover {
            background-color: #e60000;
        }

        .edit-button {
            background-color: #4caf50;
            color: white;
            padding: 5px 10px;
            border: none;
            border-radius: 5px;
            cursor: pointer;
        }

        .edit-button:hover {
            background-color: #45a049;
        }

    </style>
    <!-- jQuery CDN 추가 -->
    <script src="https://code.jquery.com/jquery-3.6.4.min.js"></script>
   
</head>
<body>
<%@ include file="/WEB-INF/views/home.jsp" %>
<script>
$(document).ready(function() {
	fetchComments(${response.board_id}); 
});
 </script>
<!-- 게시글 세부 사항 -->
<div class="board-detail">
    <h2>${response.title}</h2>
    <div class="content">${response.content}</div>
    <div class="info">
        <div>작성자: ${response.username}</div>
        <div>작성일: ${response.created_at.format(DateTimeFormatter.ofPattern('yyyy-MM-dd HH:mm:ss'))}</div>
        <div>수정일: ${response.updated_at.format(DateTimeFormatter.ofPattern('yyyy-MM-dd HH:mm:ss'))}</div>
    </div>
    
    <c:if test="${not empty sessionScope.user and response.username == sessionScope.user}">
        <div style="display: flex; justify-content: flex-start; gap: 10px;">
            <form action="/boards/edit" method="get">
                <input type="hidden" name="id" value="${response.board_id}" />
                <input type="hidden" name="username" value="${response.username}" />
                <input type="hidden" name="title" value="${response.title}" />
                <input type="hidden" name="content" value="${response.content}" />
                <button type="submit" class="edit-button">수정</button>
            </form>
            <!-- AJAX 삭제 버튼 -->
            <button class="delete-button delete-board-button">삭제</button>
            
        </div>
            
    </c:if>
    <button 
				    type="button" 
				    id="likeButton" 
				    class="like-button" 
				    data-board-id="${response.board_id}"
				    onclick="toggleLike(${response.board_id}, ${sessionScope.user_id})">
				    좋아요
				</button>
    <span>${response.liked_count}</span>
</div>
<!-- 댓글 목록 -->
<div class="comments-section">

<!-- 댓글 추가 폼 -->
    <div class="comment-form">
        <form id="commentForm">
            <textarea name="content" placeholder="댓글을 입력하세요..." required></textarea><br>
            <input type="hidden" name="board_id" value="${response.board_id}" />
            <c:set var="user_id" value="${sessionScope.user_id}" />
            <input type="hidden" name="user_id" value="${user_id}" />
            <button type="button" onclick="submitComment()">댓글 작성</button>
        </form>
    </div>

    <h3>댓글 목록</h3>

    <div id="commentsList">
    </div>
    
   <!-- 페이지네이션 -->
   <div class="pagination">
        
          
</div>
 
<script>

function toggleLike(board_id, user_id) {
	
    $.ajax({
        type: "POST",
        url: '/likes?board_id=' + board_id + '&user_id=' + user_id,
        beforeSend: function() {
            if (!user_id) {
                alert("로그인 후 좋아요를 눌러주세요.");
                return false; // 요청을 중단합니다.
            }
        },
        success: function (result) {
            if (result) {
                location.reload();
            } else {
                location.reload();
            }
        },
        error: function (xhr) {
            alert("요청 실패: " + xhr.responseText);
        }
    });
}

//コメントを投稿する関数
function submitComment() {
    var formData = $('#commentForm').serialize();// フォームデータをシリアライズ

    $.ajax({
        url: '/boards/comments',　// コメントを送信するURL
        type: 'POST',
        data: formData,				// フォームデータを送信
        success: function(response) {
            if (response) {
                // $('textarea[name="content"]').val('');
                location.reload();// ページをリロードしてコメントを表示
            } else {
                alert('댓글 추가에 실패했습니다.');
            }
        },
        error: function() {
            window.location.href = '/users/signin';
        }
    });
}
            
//コメントを削除する関数です。
            function deleteComment(event, comment_id) {
                event.preventDefault();

                $.ajax({
                    url: '/boards/comments/' + comment_id,　// 削除するコメントのURLです。
                    type: 'DELETE',
                    success: function(response) {
                        if (response) {
                            location.reload();		// コメント削除後にページをリロード
                        } else {
                            alert('댓글 삭제에 실패했습니다.');
                        }
                    },
                    error: function() {
                        alert('댓글 삭제에 실패했습니다.');
                    }
                });
            } 
</script>

<script>
			//削除ボタンがクリックされた時の処理です。
            $(".delete-board-button").on("click", function (e) {
                e.preventDefault();											// デフォルトの動作をキャンセルです。
                const board_id = "<c:out value='${response.board_id}'/>";	// 投稿IDを取得です。
                
             // ユーザーに確認メッセージを表示です。
                if (confirm("정말로 삭제하시겠습니까?")) {
                    $.ajax({
                        url: "/boards/delete/" + board_id, 					// 削除する投稿のURLです。
                        type: "DELETE",
                        //cache: false,
                        success: function (response) {
                            if(response){
                                alert("게시글이 삭제되었습니다.");
                                window.location.href = "/boards"; 			// 一覧ページにリダイレクトです。 
                            } else {
                                alert("게시글이 삭제 실패");
                            }
                        },
                        error: function (xhr, status, error) {
                            alert("삭제 실패: " + xhr.responseText);
                        }
                    });
                }
            });
    </script>
<script>

//ページロード時に非同期リクエストでコメントとユーザーデータを取得
function fetchComments(board_id, pageGroup, pageNum) {
$.ajax({
    url: '/boards/comments', 
    type: 'GET',  
    dataType: 'json', 
    data: {
    	board_id: board_id,  	//　投稿ID
    	pageGroup: pageGroup,	//　ページグルプ
    	pageNum: pageNum 		//　ページ番号
    },
    success: function(response) {
        renderComments(response.comments);	//　コメントHTMLをrendering
        renderPagination(response.paging);	//　ページネーションHTMLをrendering
    },
    error: function(error) {
        console.error('댓글 및 사용자 데이터를 가져오는 데 실패했습니다.', error);
    }
});
}

//　コメントをRenderingするFunctionです。
function renderComments(comments) {
let commentsList = '';
if (comments.length > 0) {							// コメントデータがある場合はRenderingします。
    comments.forEach(function(comment) {			//　コメント長さだけ繰り返し処理を行います。
    	var currentUser = '${sessionScope.user}';   // 現在のユーザー情報
    	
    	commentsList += 							//　Renderingする変数です。
            '<div class="comment" data-comment-id="' + comment.id + '">' +
                '<div class="username">' + comment.users.username + '</div>' +
                '<div class="timestamp">' + comment.formattedCreated_at + '</div>' +
                '<div class="content">' + comment.content + '</div>' +
                '<input type="hidden" name="comment_id" value="' + comment.id + '" />';

        // 現在のユーザーがコメントの作成者と同じ場合のみ、削除ボタンを見えるようにします。
        if (currentUser && currentUser === comment.users.username) {
            commentsList += '<button class="delete-button" onclick="deleteComment(event, ' + comment.id + ')">삭제</button>';
        }

        commentsList += '</div>';
    });
    $('#commentsList').html(commentsList); // コメントを挿入するコンテナ追加します。
}
}
//RenderingページネイションするFunctionです。
function renderPagination(paging) {
let pagination = '';				//　Renderingする変数です。


if (paging.page > 1) {				//　最初のページへ戻るリンクです。
    pagination += '<a href="javascript:;" onclick="pageGrouping(1, 1, 0, 0)" class="button">처음으로</a>';
}

if (paging.pgn > 1) {				// 前のページリンク
    pagination += '<a href="javascript:;" onclick="pageGrouping(' + (paging.pgn - 1) + ', 1, ' + paging.tpgn + ')" class="button">이전</a>';
}

//　ページ番号の範囲を計算してリンクを表示します。
let startPage = (paging.pgn - 1) * paging.pageNums + 1;
let endPage = (paging.pgn * paging.pageNums < paging.total_page) ? paging.pgn * paging.pageNums : paging.total_page;

for (let i = startPage; i <= endPage; i++) {
	//現在のページ番号が選択されている場合、アクティブクラスを追加します。
    if (paging.page === i) {
        pagination += '<a href="javascript:;" class="active">' + i + '</a>';
    } else {
        pagination += '<a href="javascript:;" onclick="allBoardList(' + paging.pgn + ', ' + i + ')">' + i + '</a>';
    }
}
//　次のページリンクです。
if (paging.pgn < paging.tpgn) {
    pagination += '<a href="javascript:;" onclick="pageGrouping(' + (paging.pgn + 1) + ', 2, ' + paging.tpgn + ', ' + paging.total_page + ')" class="button">다음</a>';
}
//　最後のページリンクです。
if (paging.page < paging.total_page) {
    pagination += '<a href="javascript:;" onclick="pageGrouping(' + paging.tpgn + ', 2, ' + paging.tpgn + ', ' + paging.total_page + ')" class="button">끝으로</a>';
}

$('.pagination').html(pagination); // ページネーションを挿入するコンテナに追加します。
}


function pageGrouping(pGroup, direct, total_page_group_num, total_page) {
    let pNum = 1;
    // ページグループが最初の場合
    if (direct == 1 && pGroup == 1) {
        pNum = 1;
    } 
    // ページグループが1より大きい場合
    else if (direct == 1 && pGroup > 1) {
        pGroup = pGroup - 1;
        pNum = pGroup * 10 - 9;
    } 
    // 次のページグループに移動する場合
    else if (direct == 2 && pGroup < total_page_group_num) {
        pGroup = pGroup + 1;
        pNum = pGroup * 10 - 9;
    } 
    // 最後のページグループに移動する場合
    else if (direct == 2 && pGroup == total_page_group_num) {
        pNum = total_page;
    }
    
    fetchComments(${response.board_id}, pGroup, pNum); // コメントとページ情報を再取得
}

function allBoardList(pageGroup, pageNum) {
    fetchComments(${response.board_id}, pageGroup, pageNum); // すべての投稿リストを取得
}
</script>
</body>
</html>
