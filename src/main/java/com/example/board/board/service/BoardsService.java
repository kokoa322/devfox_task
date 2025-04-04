package com.example.board.board.service;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;

import com.example.board.board.domain.Comments;
import com.example.board.board.dto.req.BoardsEditReqDto;
import com.example.board.board.dto.req.BoardsWriteReqDto;
import com.example.board.board.dto.res.BoardsDetailResDto;
import com.example.board.board.dto.res.BoardsListResDto;

public interface BoardsService {
	
	/**
	 * ボードを作成します。
	 * 
	 * @param boardsReqDto ボード作成のためのRequestDTO
	 * @return 作成されたボードの詳細情報を含むResponseDTO
	 */
	BoardsDetailResDto writer(BoardsWriteReqDto boardsReqDto);

	/**
	 * 指定された範囲のボードを全て取得します。
	 * 
	 * @param i 開始位置
	 * @param amount 取得するボードの数
	 * @return ボードのリスト
	 */
	List<BoardsListResDto> findAllBoards(int i, int amount);

	/**
	 * 全てのボードの数を取得します。
	 * 
	 * @return ボードの総数
	 */
	int findAllboardsCount();

	/**
	 * ボードの詳細情報を取得します。
	 * 
	 * @param i 開始位置
	 * @param amount 取得するデータの数
	 * @param board_id 詳細を取得するボードのID
	 * @return ボードの詳細情報を含むレスポンスDTO
	 */
	BoardsDetailResDto detailBoard(int i, int amount, long board_id);

	/**
	 * 指定されたボードを削除します。
	 * 
	 * @param board_id 削除するボードのID
	 * @return ボードの削除が成功したかどうか
	 */
	boolean deleteBoard(long board_id);

	/**
	 * ボードを編集します。
	 * 
	 * @param boardsEditReqDto ボード編集のためのリクエストDTO
	 * @return 編集されたボードの詳細情報を含むレスポンスDTO
	 */
	BoardsDetailResDto boardsEdit(BoardsEditReqDto boardsEditReqDto);

	/**
	 * 指定されたキーワードとタイプに基づいてボードを検索ページングします。
	 * 
	 * @param i 開始位置
	 * @param amount 取得するボードの数
	 * @param searchKeyword 検索キーワード
	 * @param searchType 検索タイプ（例：タイトル、ユーザー名、内容）
	 * @return 検索結果のボードリスト
	 */
	List<BoardsListResDto> searchFindAllBoards(int i, int amount, String searchKeyword, String searchType);

	/**
	 * 指定されたボードのコメント数を取得します。
	 * 
	 * @param board_id ボードID
	 * @return ボードについたコメントの総数
	 */
	int findAllCommnetsCount(long board_id);

	/**
	 * 指定されたボードの詳細情報を取得します。
	 * 
	 * @param board_id ボードID
	 * @return ボードの詳細情報を含むレスポンスDTO
	 */
	BoardsDetailResDto detailBoard(long board_id);

	/**
	 * 指定されたボードにコメントを追加します。
	 * 
	 * @param board_id コメントを追加するボードのID
	 * @param user_id コメントを作成したユーザーのID
	 * @param content コメントの内容
	 * @return コメント作成が成功したかどうか
	 */
	boolean writeComment(Long board_id, Long user_id, String content);

	/**
	 * 指定されたコメントを削除します。
	 * 
	 * @param comment_id 削除するコメントのID
	 * @return 削除されたコメントの情報（オブジェクト）
	 */
	Object deleteComment(long comment_id);

	/**
	 * 指定されたボードの全てのコメントを取得します。
	 * 
	 * @param i 開始位置
	 * @param amount 取得するコメントの数
	 * @param board_id ボードID
	 * @return コメントのリスト
	 */
	List<Comments> findAllComments(int i, int amount, int board_id);

	/**
	 * 指定されたボードのコメント数を取得します。
	 * 
	 * @param board_id ボードID
	 * @return コメントの総数
	 */
	int findAllCommentsCount(int board_id);

	int findBoardsSearchCount(String searchKeyword, String searchType);

	Long findUserId(Long board_id);

	void increaseHits(long board_id);

	//Page<BoardsListResDto> getBoards(int page, int size);
}
