package com.example.board.board.serviceImpl;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.board.board.dao.BoardsDao;
import com.example.board.board.domain.Boards;
import com.example.board.board.domain.Comments;
import com.example.board.board.dto.req.BoardsEditReqDto;
import com.example.board.board.dto.req.BoardsWriteReqDto;
import com.example.board.board.dto.res.BoardsDetailResDto;
import com.example.board.board.dto.res.BoardsListResDto;
import com.example.board.board.repository.BoardsRepository;
import com.example.board.board.service.BoardsService;
import com.example.board.users.dao.UsersDao;
import com.example.board.users.domain.Users;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class BoardsServiceImpl implements BoardsService {
	
	private final BoardsDao boardsDao;
	private final UsersDao usersDao;
	private final BoardsRepository boardsRepository;

	/**
	 * 新しい投稿を作成し、その詳細情報を返すメソッド。
	 * 
	 * @param boardsWriteReqDto 投稿作成に必要なデータ
	 * @return 作成された投稿の詳細情報を含むBoardsDetailResDtoオブジェクト
	 *         作成に失敗した場合はnullを返す
	 */
	@Override
	@Transactional
	public BoardsDetailResDto writer(BoardsWriteReqDto boardsWriteReqDto) {
		Users users = usersDao.findByUsername(boardsWriteReqDto.getUsername());
		Boards board = Boards.builder()
				.content(boardsWriteReqDto.getContent())
				.title(boardsWriteReqDto.getTitle())
				.users(users)
				.build();
		
		Optional<Boolean> result = Optional.ofNullable(boardsDao.write(board) > 0 ? Boolean.TRUE : Boolean.FALSE);
		
		if(result.orElse(false)) {
			
			long id = boardsDao.findBylastInsertedId();
			Optional<BoardsDetailResDto> optionalBoardDetail = Optional.ofNullable(boardsDao.findById(id));

			BoardsDetailResDto boardsDetailResDto = optionalBoardDetail
			    .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 게시판 입니다."));

			boardsDetailResDto.setUsername(users.getUsername());
			log.info("{}",boardsDetailResDto);
			return boardsDetailResDto;
			
		} else {
			return null;
		}
	}

	/**
	 * 投稿リストを取得するメソッド。
	 *
	 * @param i ページ番号 
	 * @param amount ページ内の項目数 
	 * @return 投稿リストBoardsListResDto
	 */
	@Override
	public List<BoardsListResDto> findAllBoards(int i, int amount) {
		
		Map<String, Integer> map = new HashMap<String, Integer>();
		map.put("pNum", i);
		map.put("scale", amount);
		 
		
		return boardsDao.findAllBoards(map).stream()
        .map(data -> {
            Long id = (Long) data.getId();
            String username = (String) data.getUsername();
            String title = (String) data.getTitle();
            LocalDateTime created_at = (LocalDateTime) data.getCreated_at();
            int comment_count = data.getComment_count();
            return new BoardsListResDto(id, username, title, created_at, comment_count);
        })
        .collect(Collectors.toList());
	}

	/**
	 * すべての掲示板の件数を取するメソッドです。
	 * 
	 * @return 掲示板の総件数
	 */
	@Override
	public int findAllboardsCount() {
		return boardsDao.findBoardsCount();
	}

	/**
	 * 指定された掲示板の詳細情報を取得するメソッドです。
	 * 
	 * @param i ページ番号
	 * @param amount ページごとのデータ量
	 * @param board_id 掲示板のID
	 * @return 掲示板の詳細情報を含むBoardsDetailResDtoオブジェクト
	 */
	@Override
	@Transactional
	public BoardsDetailResDto detailBoard(int i, int amount, long board_id) {
		
		Map<String, Object> map = new HashMap();
		map.put("pNum", i);
		map.put("scale", amount);
		map.put("board_id", String.valueOf(board_id));
		
		Optional<BoardsDetailResDto> optionalBoardDetail = Optional.ofNullable(boardsDao.findById(map));

		BoardsDetailResDto boardsDetailResDto = optionalBoardDetail
		    .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 게시판 입니다."));

		log.info("{}",boardsDetailResDto);
		return boardsDetailResDto;
		
	}
	/**
	 * 指定された掲示板を削除するメソッドです。
	 * 
	 * @param board_id 削除対象の投稿のID
	 * @return 掲示板の削除が成功した場合はtrue、失敗した場合はfalseを返します
	 */
	@Override
	@Transactional
	public boolean deleteBoard(long board_id) {
		try {
	        boardsRepository.deleteById(board_id);
	        return true; // 삭제가 정상적으로 완료된 경우
	    } catch (EmptyResultDataAccessException e) {
	        return false; // 해당 ID가 없어서 삭제할 수 없는 경우
	    }
	}
	
	/**
	 * 指定された掲示板の内容を編集するメソッドです。
	 * 
	 * @param boardsEditReqDto 編集内容を含む情報のBoardsEditReqDto
	 * @return 編集後の掲示板の詳細情報を含む情報BoardsDetailResDto
	 */
	@Override
	public BoardsDetailResDto boardsEdit(BoardsEditReqDto boardsEditReqDto) {
		
		Boards boards = Boards.builder()
				.id(boardsEditReqDto.getId())
				.title(boardsEditReqDto.getTitle())
				.content(boardsEditReqDto.getContent())
				.build();
		
		if(boardsDao.boardUpdate(boards) > 0) {
			
			Optional<BoardsDetailResDto> optionalBoardDetail = Optional.ofNullable(boardsDao.findById(boards.getId()));

			BoardsDetailResDto boardsDetailResDto = optionalBoardDetail
			    .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 게시판 입니다."));
			boardsDetailResDto.setUsername(boardsEditReqDto.getUsername());
			return boardsDetailResDto;
		} else {
			return null;
		}
	}

	/**
	 * 指定された検索条件で、掲示板のリストを検索するメソッドです。
	 * 
	 * @param i ページ番号
	 * @param amount ページごとのデータ量
	 * @param searchKeyword 検索キーワード
	 * @param searchType 検索タイプ（タイトル、内容など）
	 * @return 検索結果を含むの情報BoardsListResDto
	 */
	@Override
	public List<BoardsListResDto> searchFindAllBoards(int i, int amount, String searchKeyword, String searchType) {
		
		Map<String, String> map = new HashMap();
		map.put("pNum", String.valueOf(i));
		map.put("scale", String.valueOf(amount));
		map.put("searchKeyword", searchKeyword);
		map.put("searchType", searchType);
		 
		
		return boardsDao.searchFindAllBoards(map).stream()
        .map(data -> {
            Long id = (Long) data.getId();
            String username = (String) data.getUsername();
            String title = (String) data.getTitle();
            LocalDateTime created_at = (LocalDateTime) data.getCreated_at();
            int comment_count = data.getComment_count();
            return new BoardsListResDto(id, username, title, created_at, comment_count);
        })
        .collect(Collectors.toList());
	}

	/**
	 * 指定された掲示板に関連するコメントの総件数を取得するメソッドです。
	 * 
	 * @param board_id コメント数を取得する投稿のID
	 * @return 指定された投稿のコメントの総件数
	 */
	@Override
	public int findAllCommnetsCount(long board_id) {
	    return boardsDao.findAllCommnetsCount(board_id);
	}

	/**
	 * 指定された掲示板の詳細情報を取得するメソッドです。
	 * 
	 * @param board_id 詳細情報を取得する掲示板のID
	 * @return 指定された掲示板の詳細情報を含むBoardsDetailResDtoオブジェクト
	 */
	@Override
	public BoardsDetailResDto detailBoard(long board_id) {
	    return boardsDao.findById(board_id);
	}

	/**
	 * 指定された掲示板にコメントを追加するメソッドです。
	 * 
	 * @param board_id コメントを追加する掲示板のID
	 * @param user_id コメントを投稿するユーザーのID
	 * @param content コメントの内容
	 * @return コメントの投稿が成功した場合はtrue、失敗した場合はfalseを返します
	 */
	@Override
	public boolean writeComment(Long board_id, Long user_id, String content) {
		Map<String, Object> map = new HashMap<>();
		map.put("board_id", board_id);
		map.put("user_id", user_id);
		map.put("content", content);
		
		Optional<Boolean> result = Optional.ofNullable(boardsDao.writeComment(map) > 0 ? Boolean.TRUE : Boolean.FALSE);
		return result.orElse(false);
	}
	
	/**
	 * 指定されたコメントを削除するメソッドです。
	 * 
	 * @param comment_id 削除対象のコメントのID
	 * @return コメントの削除が成功した場合はtrue、失敗した場合はfalseを返します
	 */
	@Override
	public Object deleteComment(long comment_id) {
		Optional<Boolean> result = Optional.ofNullable(boardsDao.deleteComment(comment_id) > 0 ? Boolean.TRUE : Boolean.FALSE);
		return result.orElse(false);
	}

	/**
	 * 指定された掲示板のすべてのコメントを取得するメソッドです。
	 * 
	 * @param i ページ番号
	 * @param amount ページごとのデータ量
	 * @param board_id コメントを取得する掲示板のID
	 * @return コメントのリスト
	 */
	@Override
	public List<Comments> findAllComments(int i, int amount, int board_id) {
		Map<String, Integer> map = new HashMap<String, Integer>();
		map.put("pNum", i);
		map.put("scale", amount);
		map.put("board_id", board_id);
		 
		
		return boardsDao.findAllComments(map).stream()
			    .map(data -> {
			        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
			        String formattedCreatedAt = data.getCreated_at().format(formatter);
			        
			        return Comments.builder()
			            .id(data.getId())
			            .boards(data.getBoards())
			            .users(data.getUsers())
			            .content(data.getContent())
			            .created_at(data.getCreated_at())
			            .formattedCreated_at(formattedCreatedAt)
			            .build();
			    })
			    .collect(Collectors.toList());
	}
	
	/**
	 * 指定された掲示板のすべてのコメントを取得するメソッドです。
	 * 
	 * @param i ページ番号
	 * @param amount ページごとのデータ量
	 * @param board_id コメントを取得する投稿のID
	 * @return コメントのリスト
	 */
	@Override
	public int findAllCommentsCount(int board_id) {
		return boardsDao.findAllCommentsCount(board_id);
	}

	@Override
	public int findBoardsSearchCount(String searchKeyword, String searchType) {
		
		Map<String, String> map = new HashMap<String, String>();
		map.put("searchKeyword", searchKeyword);
		map.put("searchType", searchType);
		
		return boardsDao.findBoardsSearchCount(map);
	}

	@Override
	public Long findUserId(Long board_id) {
		
		return boardsDao.findUserIdByboardId(board_id);
	}
	
	
}
