<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import="java.util.Date"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="com.example.board.board.util.Paging"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>


<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Home Page</title>
    <style>
        body {
            font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, "Helvetica Neue", Arial, sans-serif;
            background-color: #f5f5f7;
            color: #333;
            margin: 0;
            padding: 0;
        }

        h1 {
            text-align: center;
            color: #333;
            margin-top: 30px;
        }

        .table-container {
            width: 80%;
            margin: 20px auto;
            background-color: white;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
            border-radius: 8px;
            overflow: hidden;
        }

        table {
            width: 100%;
            border-collapse: collapse;
        }

        th, td {
            padding: 20px;
            text-align: left;
        }

        th {
            background-color: #f1f1f1;
            font-weight: normal;
            color: #666;
        }

        tr:nth-child(even) {
            background-color: #f9f9f9;
        }

        tr:hover {
            background-color: #f1f1f1;
        }

        .pagination {
            display: flex;
            justify-content: center;
            margin-top: 20px;
        }

        .pagination a {
            text-decoration: none;
            color: #007aff;
            margin: 0 10px;
            font-size: 18px;
            padding: 12px 20px;
            border-radius: 5px;
            background-color: #f0f0f5;
            font-weight: bold;
        }

        .pagination a:hover {
            background-color: #007aff;
            color: white;
        }

        .pagination .disabled {
            color: #ccc;
            pointer-events: none;
        }

        .pagination .active {
            font-weight: bold;
            color: #333;
        }

        .button, .search-form button {
            padding: 11px 13px;
            border-radius: 5px;
            background: linear-gradient(45deg, #6c63ff, #5048e5);
            color: white;
            text-decoration: none;
            font-weight: bold;
            display: inline-block;
            transition: background 0.3s ease, transform 0.2s ease;
            font-size: 16px;
        }

        .button:hover, .search-form button:hover {
            background: linear-gradient(45deg, #5048e5, #6c63ff);
            transform: scale(1.05);
        }

        .write-button {
            display: flex;
            justify-content: flex-end;
            margin: 20px 10vw;
            border-radius: 5px;
        }

        .search-form {
            display: flex;
            justify-content: flex-end;
        }

        .search-form input[type="text"] {
            padding: 12px;
            border-radius: 5px;
            border: 1px solid #ccc;
            width: 150px;
            font-size: 16px;
        }

        .search-form button {
            padding: 12px 20px;
            border-radius: 5px;
            border: none;
            background-color: #007aff;
            color: white;
            font-weight: bold;
            font-size: 16px;
            margin-left: 10px;
            cursor: pointer;
            transition: background 0.3s ease, transform 0.2s ease;
        }

        .search-form button:hover {
            background-color: #005bb5;
            transform: scale(1.05);
        }

        .search-form button:focus {
            outline: none;
            box-shadow: 0 0 5px rgba(0, 123, 255, 0.5);
        }

        @media (max-width: 768px) {
            .table-container {
                width: 95%;
            }

            .pagination a {
                font-size: 14px;
                padding: 8px 14px;
            }

            .search-form input[type="text"] {
                width: 220px;
            }

            .button, .search-form button {
                padding: 14px;
                font-size: 16px;
            }
        }
    </style>
    <script src="https://code.jquery.com/jquery-3.6.4.min.js"></script>
</head>
<body>
<%@ include file="/WEB-INF/views/home.jsp" %>
    <div>
        <h1>게시판</h1>

        <!-- 글쓰기 버튼 및 검색 폼 -->
        <div class="write-button">
            <div class="search-form">
                <form action="/boards/search" method="GET">
		            <select name="searchType" style="padding: 12px; border-radius: 5px; border: 1px solid #ccc; width: 150px; font-size: 16px;">
		                <option value="title">제목</option>
		                <option value="username">작성자</option>
		                <option value="content">내용</option>
		            </select>
		            <input type="text" name="searchKeyword" placeholder="검색" />
		            <button type="submit">검색</button>
		        </form>
            </div>
            &nbsp;&nbsp;
            <a href="/boards/write" class="button">글쓰기</a> <!-- 글쓰기 버튼 스타일 변경 -->
        </div>

        <!-- 검색 결과 없을 때 메시지 출력 -->
        <c:if test="${empty items}">
            <p>검색 결과가 없습니다.</p>
        </c:if>

        <!-- 테이블 -->
        <div class="table-container">
            <table>
                <thead>
                    <tr>
                        <th style="text-align: center;">번호</th>
                        <th style="text-align: center;">제목</th>
                        <th style="text-align: center;">이름</th>
                        <th style="text-align: center;">작성일</th>
                        <th style="text-align: center;">좋아요</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="item" items="${items}">
                        <tr>
                            <td><a href="/boards/${item.id}?username=${item.username}" style="text-decoration: none; color: inherit; display: block; text-align: center;">${item.id}</a></td>
                            <td><a href="/boards/${item.id}?username=${item.username}" style="text-decoration: none; color: inherit; display: block; text-align: center;"">${item.title} (${item.comment_count})</a></td>
                            <td><a href="/boards/${item.id}?username=${item.username}" style="text-decoration: none; color: inherit; display: block; text-align: center;"">${item.username}</a></td>
                            <td><a href="/boards/${item.id}?username=${item.username}" style="text-decoration: none; color: inherit; display: block; text-align: center;"">${item.formattedCreated_at}</a></td>
                            <td><a href="/boards/${item.id}?username=${item.username}" style="text-decoration: none; color: inherit; display: block; text-align: center;"">${item.liked_count}</a></td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>

        <!-- 페이징 -->
        <div class="pagination">
        	<!-- 検索キーワードと検索タイプがある場合 -->
        	<c:if test="${not empty searchKeyword and not empty searchType}">
        	    <!-- 現在のページが1より大きい場合、「最初へ」ボタンを表示 -->
	            <c:if test="${paging.page > 1}">
	                <a href="javascript:;" onclick="searchPageGrouping(1, 1, 0, 0, '${searchKeyword}', '${searchType}')" class="button" style="color: white; text-decoration: none;">처음으로</a>
	            </c:if>
				<!-- 現在のページグループが1より大きい場合、「前へ」ボタンを表示 -->
	            <c:if test="${paging.pgn > 1}">
	                <a href="javascript:;" onclick="searchPageGrouping(${paging.pgn - 1}, 1, ${paging.tpgn}, ${paging.total_page}, '${searchKeyword}', '${searchType}')" class="button" style="color: white; text-decoration: none;">이전</a>
	            </c:if>
				<!-- ページ番号を表示 -->
	            <c:forEach var="i" begin="${(paging.pgn - 1) * paging.pageNums + 1}" end="${(paging.pgn * paging.pageNums < paging.total_page) ? paging.pgn * paging.pageNums : paging.total_page}">
	                <c:choose>
	                    <c:when test="${paging.page == i}">
	                        <a href="javascript:;" class="active">${i}</a>
	                    </c:when>
	                    <c:otherwise>
	                        <a href="javascript:;" onclick="searchAllBoardList(${paging.pgn}, ${i}, '${searchKeyword}', '${searchType}')">${i}</a>
	                    </c:otherwise>
	                </c:choose>
	            </c:forEach>
				<!-- 現在のページグループが最後のページグループより小さい場合、「次へ」ボタンを表示 -->
	            <c:if test="${paging.pgn < paging.tpgn}">
	                <a href="javascript:;" onclick="searchPageGrouping(${paging.pgn + 1}, 2, ${paging.tpgn}, ${paging.total_page}, '${searchKeyword}', '${searchType}')" class="button" style="color: white; text-decoration: none;">다음</a>
	            </c:if>
				<!-- 現在のページが最終ページより小さい場合、「最後へ」ボタンを表示 -->
	            <c:if test="${paging.page < paging.total_page}">
	                <a href="javascript:;" onclick="searchPageGrouping(${paging.tpgn}, 2, ${paging.tpgn}, ${paging.total_page}, '${searchKeyword}', '${searchType}')" class="button" style="color: white; text-decoration: none;">끝으로</a>
	            </c:if>
            </c:if>
            
            <!-- 検索キーワードまたは検索タイプが空の場合 -->
            <c:if test="${empty searchKeyword or empty searchType}">
            	<!-- 現在のページが1より大きい場合、「最初へ」ボタンを表示 -->
            	<c:if test="${paging.page > 1}">
	                <a href="javascript:;" onclick="pageGrouping(1, 1, 0, 0)" class="button" style="color: white; text-decoration: none;">처음으로</a>
	            </c:if>
				<!-- 現在のページグループが1より大きい場合、「前へ」ボタンを表示 -->
	            <c:if test="${paging.pgn > 1}">
	                <a href="javascript:;" onclick="pageGrouping(${paging.pgn - 1}, 1, ${paging.tpgn}, ${paging.total_page})" class="button" style="color: white; text-decoration: none;">이전</a>
	            </c:if>
				<!-- ページ番号を表示 -->
	            <c:forEach var="i" begin="${(paging.pgn - 1) * paging.pageNums + 1}" end="${(paging.pgn * paging.pageNums < paging.total_page) ? paging.pgn * paging.pageNums : paging.total_page}">
	                <c:choose>
	                    <c:when test="${paging.page == i}">
	                        <a href="javascript:;" class="active">${i}</a>
	                    </c:when>
	                    <c:otherwise>
	                        <a href="javascript:;" onclick="allBoardList(${paging.pgn}, ${i})">${i}</a>
	                    </c:otherwise>
	                </c:choose>
	            </c:forEach>
	
				<!-- 現在のページグループが最後のページグループより小さい場合、「次へ」ボタンを表示 -->
	            <c:if test="${paging.pgn < paging.tpgn}">
	                <a href="javascript:;" onclick="pageGrouping(${paging.pgn + 1}, 2, ${paging.tpgn}, ${paging.total_page})" class="button" style="color: white; text-decoration: none;">다음</a>
	            </c:if>
		
				<!-- 現在のページが最終ページより小さい場合、「最後へ」ボタンを表示 -->				
	            <c:if test="${paging.page < paging.total_page}">
	                <a href="javascript:;" onclick="pageGrouping(${paging.tpgn}, 2, ${paging.tpgn}, ${paging.total_page})" class="button" style="color: white; text-decoration: none;">끝으로</a>
	            </c:if>
            </c:if>
        </div>
    </div>

    <script>
        // ページグループを変更する関数
        function pageGrouping(pGroup, direct, total_page_group_num, total_page) {
            let pNum = 0;
            // 最初のページグループに移動する場合
            if (direct == 1 && pGroup == 1) {
                pNum = 1;
            } else if (direct == 1 && pGroup > 1) {
                // 前のページグループに移動する場合
                pGroup = pGroup - 1;
                pNum = pGroup * 10 - 9;
            } else if (direct == 2 && pGroup < total_page_group_num) {
                // 次のページグループに移動する場合
                pGroup = pGroup + 1;
                pNum = pGroup * 10 - 9;
            } else if (direct == 2 && pGroup == total_page_group_num) {
                // 最後のページに移動する場合
                pNum = total_page;
            }

            // ページのリダイレクト
            window.location.href = "/boards?pageNum=" + pNum + "&pageGroup=" + pGroup;
        }

        // すべてのボードリストを表示するための関数
        function allBoardList(pageGroup, pageNum) {
            // 指定されたページ番号にリダイレクト
            window.location.href = "/boards?pageNum=" + pageNum + "&pageGroup=" + pageGroup;
        }
        
        // 検索結果のページグループを変更する関数
        function searchPageGrouping(pGroup, direct, total_page_group_num, total_page, searchKeyword, searchType) {
            let pNum = 0;
            // 最初のページグループに移動する場合
            if (direct == 1 && pGroup == 1) {
                pNum = 1;
            } else if (direct == 1 && pGroup > 1) {
                // 前のページグループに移動する場合
                pGroup = pGroup - 1;
                pNum = pGroup * 10 - 9;
            } else if (direct == 2 && pGroup < total_page_group_num) {
                // 次のページグループに移動する場合
                pGroup = pGroup + 1;
                pNum = pGroup * 10 - 9;
            } else if (direct == 2 && pGroup == total_page_group_num) {
                // 最後のページに移動する場合
                pNum = total_page;
            }

            // 検索結果を含むページのリダイレクト
            window.location.href = "/boards/search?pageNum=" + pNum + "&pageGroup=" + pGroup + "&searchKeyword=" + searchKeyword + "&searchType=" + searchType;
        }

        // 検索結果に基づいてすべてのボードリストを表示するための関数
        function searchAllBoardList(pageGroup, pageNum, searchKeyword, searchType) {
            // 検索結果を含む指定されたページ番号にリダイレクト
            window.location.href = "/boards/search?pageNum=" + pageNum + "&pageGroup=" + pageGroup + "&searchKeyword=" + searchKeyword + "&searchType=" + searchType;
        }
        
</script>
</body>
</html>



