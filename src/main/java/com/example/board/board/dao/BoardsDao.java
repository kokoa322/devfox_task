package com.example.board.board.dao;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.ibatis.annotations.Mapper;

import com.example.board.board.domain.Boards;
import com.example.board.board.domain.Comments;
import com.example.board.board.dto.res.BoardsDetailResDto;
import com.example.board.board.dto.res.BoardsListResDto;

@Mapper
public interface BoardsDao {

	/**
	 * 投稿を作成します。
	 *
	 * @param boards 作成する投稿情報
	 * @return 作成された投稿の行数
	 */
	int write(Boards boards);

	/**
	 * 最後に挿入されたIDを取得します。
	 *
	 * @return 最後に挿入された投稿のID
	 */
	long findBylastInsertedId();

	/**
	 * 投稿詳細情報を取得します
	 *
	 * @param map 検索条件
	 * @return 投稿詳細情報
	 */
	BoardsDetailResDto findById(Map<String, Object> map);

	/**
	 * 投稿詳細情報を取得します。（投稿ID指定）
	 *
	 * @param id 投稿のID
	 * @return 投稿詳細情報
	 */
	BoardsDetailResDto findById(long id);

	/**
	 * すべての投稿をページネーション
	 *
	 * @param map ページネーション情報
	 * @return 投稿リスト
	 */
	List<BoardsListResDto> findAllBoards(Map<String, Integer> map);

	/**
	 * 投稿の総数を取得します。
	 *
	 * @return 投稿の総件数
	 */
	int findBoardsCount();

	/**
	 * 指定したIDの投稿を削除します。
	 *
	 * @param board_id 削除する投稿のボードのID
	 * @return 削除された行数
	 */
	int deleteBoards(long board_id);

	/**
	 * 投稿情報を更新します。
	 *
	 * @param boards 更新する投稿情報
	 * @return 更新された行数
	 */
	int boardUpdate(Boards boards);

	/**
	 * キーワード検索で投稿を取得します。
	 *
	 * @param map 検索条件
	 * @return 検索結果の投稿リスト
	 */
	List<BoardsListResDto> searchFindAllBoards(Map<String, String> map);

	/**
	 * テスト用の投稿データを作成します。
	 */
	void testBoards();

	/**
	 * 特定の投稿に関連するコメントの総数を取得します。
	 *
	 * @param board_id 投稿のID
	 * @return コメントの総数
	 */
	int findAllCommnetsCount(long board_id);

	/**
	 * コメントを新規作成します。
	 *
	 * @param map コメントの情報（例：投稿ID、ユーザーID、内容）
	 * @return 作成されたコメントの行数
	 */
	int writeComment(Map<String, Object> map);

	/**
	 * 指定したIDのコメントを削除します。
	 *
	 * @param id 削除するコメントのID
	 * @return 削除された行数
	 */
	int deleteComment(long id);

	/**
	 * 特定の投稿に関連するすべてのコメントを取得します。
	 *
	 * @param map ページネーション情報
	 * @return コメントリスト
	 */
	List<Comments> findAllComments(Map<String, Integer> map);

	/**
	 * 特定の投稿に関連するコメントの総数を取得します。
	 *
	 * @param id 投稿のID
	 * @return コメントの総数
	 */
	int findAllCommentsCount(int id);

	int findBoardsSearchCount(Map<String, String> map);

	Long findUserIdByboardId(Long board_id);


}
