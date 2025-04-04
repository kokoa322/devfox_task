package com.example.board.board.daoImpl;

import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import com.example.board.board.dao.BoardsDao;
import com.example.board.board.domain.Boards;
import com.example.board.board.domain.Comments;
import com.example.board.board.dto.res.BoardsDetailResDto;
import com.example.board.board.dto.res.BoardsListResDto;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class BoardsDaoImpl implements BoardsDao{
	
	private final SqlSessionTemplate sqlSession;

	/**
	 * ボードを作成します。
	 * 
	 * @param boards ボードの作成する情報
	 * @return 挿入された行数
	 */
	@Override
	public int write(Boards boards) {
	    return sqlSession.insert("Boards.save", boards);
	}

	/**
	 * 最後に挿入されたボードのIDを取得します。
	 * 
	 * @return 最後の挿入ID
	 */
	@Override
	public long findBylastInsertedId() {
	    return sqlSession.selectOne("Boards.findBylastInsertedId");
	}

	/**
	 * ボードIDで投稿を探して詳細情報を取得します。
	 * 
	 * @param id ボードID
	 * @return ボードの詳細情報を含むレスポンスDTO
	 */
	@Override
	public BoardsDetailResDto findById(long id) {
	    return sqlSession.selectOne("Boards.findById", id);
	}

	/**
	 * 複数の条件でボード詳細情報を取得します。
	 * 
	 * @param map 条件を含むマップ
	 * @return ボードの詳細情報を含むレスポンスDTO
	 */
	@Override
	public BoardsDetailResDto findById(Map<String, Object> map) {
	    return sqlSession.selectOne("Boards.findByIdComments", map);
	}

	/**
	 * 指定された範囲のボードリストを取得します。
	 * 
	 * @param map ページング情報
	 * @return ボードリスト
	 */
	@Override
	public List<BoardsListResDto> findAllBoards(Map<String, Integer> map) {
	    return sqlSession.selectList("Boards.findAll", map);
	}

	/**
	 * 全てのボードの件数を取得します。
	 * 
	 * @return ボードの総件数
	 */
	@Override
	public int findBoardsCount() {
	    return sqlSession.selectOne("Boards.findBoardsCount");
	}

	/**
	 * 指定されたボードを削除します（関連コメントも削除）。
	 * 
	 * @param id ボードID
	 * @return 削除された行数
	 */
	@Override
	@Transactional
	public int deleteBoards(long id) {
	    sqlSession.delete("Comments.deleteCommentsByboardId", id);
	    return sqlSession.delete("Boards.deleteBoardsById", id);
	}

	/**
	 * ボードを更新します。
	 * 
	 * @param boards 更新するボードの情報
	 * @return 更新された行数
	 */
	@Override
	public int boardUpdate(Boards boards) {
	    return sqlSession.update("Boards.updateBoard", boards);
	}

	/**
	 * 検索条件に基づいてボードを取得します。
	 * 
	 * @param map 検索条件を含むマップ
	 * @return 検索結果のボードリスト
	 */
	@Override
	public List<BoardsListResDto> searchFindAllBoards(Map<String, String> map) {
	    return sqlSession.selectList("Boards.searchFindAll", map);
	}

	/**
	 * ボードとユーザーのテストデータを挿入します。
	 */
	@Override
	public void testBoards() {
	    sqlSession.insert("Users.saveTest");
	    
	    for (int id = 1; id < 364; id++) {
	        sqlSession.insert("Boards.saveTest", id);
	    }
	}

	/**
	 * 指定されたボードのコメント件数を取得します。
	 * 
	 * @param id ボードID
	 * @return コメントの総件数
	 */
	@Override
	public int findAllCommnetsCount(long id) {
	    return sqlSession.selectOne("Boards.findByBoardsIdCommnetsCount", id);
	}

	/**
	 * コメントを作成します。
	 * 
	 * @param map コメント作成情報を含むマップ（例：board_id、user_id、content）
	 * @return 作成された行数
	 */
	@Override
	public int writeComment(Map<String, Object> map) {
	    return sqlSession.insert("Boards.saveComment", map);
	}

	/**
	 * 指定されたボードのIDでコメントを削除します。
	 * 
	 * @param id コメントID
	 * @return 削除された行数
	 */
	@Override
	public int deleteComment(long id) {
	    return sqlSession.delete("Comments.deleteById", id);
	}

	/**
	 * 指定された範囲のコメントリストを取得します。
	 * 
	 * @param map ページング情報
	 * @return コメントリスト
	 */
	@Override
	public List<Comments> findAllComments(Map<String, Integer> map) {
	    return sqlSession.selectList("Comments.findAllComments", map);
	}

	/**
	 * 指定されたボードのコメント数を取得します。
	 * 
	 * @param id ボードID
	 * @return コメントの総件数
	 */
	@Override
	public int findAllCommentsCount(int id) {
	    return sqlSession.selectOne("Boards.findByBoardsIdCommnetsCount", id);
	}

	@Override
	public int findBoardsSearchCount(Map<String, String> map) {
		return sqlSession.selectOne("Boards.findBoardsSearchCount", map);
	}

	@Override
	public Long findUserIdByboardId(Long board_id) {
		return sqlSession.selectOne("Boards.findUserIdByBoardId", board_id);
	}

	@Override
	public void hits(long board_id) {
		sqlSession.insert("Boards.saveHits", board_id);
	}

}
