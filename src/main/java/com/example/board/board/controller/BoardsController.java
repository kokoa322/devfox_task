package com.example.board.board.controller;

import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.board.aspect.CheckSession;
import com.example.board.aspect.RequireSession;
import com.example.board.board.domain.Comments;
import com.example.board.board.dto.req.BoardsEditReqDto;
import com.example.board.board.dto.req.BoardsWriteReqDto;
import com.example.board.board.dto.res.BoardsDetailResDto;
import com.example.board.board.dto.res.BoardsListResDto;
import com.example.board.board.service.BoardsService;
import com.example.board.board.service.SseEmitterService;
import com.example.board.board.util.Paging;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Controller
@RequestMapping("/boards")
@RequiredArgsConstructor
public class BoardsController {
	
	private final SseEmitterService sseEmitterService;
	private final BoardsService boardsService;
	
	/**
	 * 指定された検索条件で掲示板のリストを取得し、ページネーションを行います。
	 * 
	 * @param model        ビューにデータを渡すためのModelオブジェクト
	 * @param session      ユーザーセッション情報を保持するHttpSessionオブジェクト
	 * @param pageGroup    現在のページグループ (nullの場合、デフォルト値は1)
	 * @param pageNum      現在のページ番号 (nullの場合、デフォルト値は1)
	 * @param searchKeyword 検索キーワード (nullの場合、すべての投稿を検索)
	 * @param searchType   検索タイプ (タイトル、ユーザーの名、または内容)
	 * @return 検索した投稿の掲示板のリストページのデータを渡します。
	 * 
	 * 検索ページネーション過程です。
	 * １。ページング変数宣言
	 * ２。投稿リスト取得するメソッド指出します。
	 * ３。全ての投稿の総数を取得するメソッド指出します。
	 * ４。ページングオブジェクトを初期化します。
	 * ５。モデルオブジェクトにデータを追加します。
	 */
	
	@CheckSession
	@GetMapping("/search")
	public String Searchlist(Model model, HttpSession session,
			@RequestParam(value ="pageGroup", required=false) String pageGroup, 
			@RequestParam(value="pageNum", required=false) String pageNum,
			@RequestParam(value="searchKeyword", required=false) String searchKeyword,
			@RequestParam(value="searchType", required=false) String searchType) {
		
		//　１。
		int pGroup = pageGroup != null ? Integer.parseInt(pageGroup) : 1;	//　ページグルプ　例　１〜１０、１１〜２０、２１〜３０
        int pNum = pageNum != null ? Integer.parseInt(pageNum) : 1;			//　現在のページ番号を示す変数です。
		int amount = 10;													// 1ページあたりの表示件数 を示す変数です。
		
		// ２。
		List<BoardsListResDto> boardsListResDto = 
				boardsService.searchFindAllBoards((pNum-1) * amount, amount, searchKeyword, searchType); // ページ番号（pNum）に基づいて検索条件（searchKeyword, searchType）で投稿リストを取得します。
		//　3。
		int totArticles = boardsService.findBoardsSearchCount(searchKeyword, searchType);	// 件削条件に基づいて全ての投稿の総数を取得するメソッド指出します。
		//　４。
		Paging paging = new Paging(totArticles, pNum, pGroup);	// ページネーション情報を計算します。
		//　５。モデルに検索ページングに必要なデータを追加します。
		model.addAttribute("items", boardsListResDto);
		model.addAttribute("paging", paging);
		model.addAttribute("searchKeyword", searchKeyword);
		model.addAttribute("searchType", searchType);
		return "board/list";
	}
	
	/**
	 * 掲示板のリストページを表示するメソッドです。
	 * 
	 * @CheckSession セッションを確認するアノテーション。
	 * @param model データをビューに渡すためのオブジェクト。
	 * @param session 現在のユーザーセッション情報。
	 * @param pageGroup ページングのページグループ（オプション）。
	 * @param pageNum ページングのページ番号（オプション）。
	 * @return 掲示板のリストページのデータを("board/list"に) を返します。
	 * 
	 * ページネーション過程です。
	 * １。変数宣言
	 * ２。投稿リスト取得するメソッド指出します。
	 * ３。全ての投稿の総数を取得するメソッド指出します。
	 * ４。ページングオブジェクトを初期化します。
	 * ５。モデルオブジェクトにデータを追加します。
	 */
	@CheckSession
	@GetMapping("")
	public String list(Model model, HttpSession session,
			@RequestParam(value = "pageGroup", required=false) String pageGroup, 
			@RequestParam(value="pageNum", required=false) String pageNum) {
		// 페이지와 사이즈 변수 초기화
		//　１。
        int pGroup = pageGroup != null ? Integer.parseInt(pageGroup) : 1;	//　ページグルプ　例　１〜１０、１１〜２０、２１〜３０
        int pNum = pageNum != null ? Integer.parseInt(pageNum) : 1;			//　現在のページ番号を示す変数です。
		int amount = 10;													// 1ページあたりの表示件数 を示す変数です。
				
		
		//　２。
		List<BoardsListResDto> boardsListResDto = 
				boardsService.findAllBoards((pNum-1) * amount, amount);	// ページ番号（pNum）に基づいて投稿リストを取得します。
		//　３。
		int totArticles = boardsService.findAllboardsCount();	// 全ての投稿の総数を取得するメソッド指出します。
		//　４。
		Paging paging = new Paging(totArticles, pNum, pGroup);	// ページネーション情報を計算します。
		//　５。モデルに検索ページングに必要なデータを追加します。
		model.addAttribute("items", boardsListResDto);
		model.addAttribute("paging", paging);
		return "board/list";
	}
	
	/**
	 * 指定された掲示板IDの詳細情報を取得し、詳細画面を表示します。
	 * 
	 * @param board_id 掲示板のID
	 * @param username ユーザー名
	 * @param model モデルオブジェクト、ビューにデータを渡すために使用します
	 * @param session セッションオブジェクト、セッション情報を管理します
	 * @return 掲示板詳細ページを表示するためのテンプレート
	 * 
	 *　詳細情報ページを表示する過程です。
	 *　１。詳細情報を取得するメソッド呼び出します。
	 *　２。boardsDetailResDtoにユーザー名を追加します。
	 *　３。詳細ページに必要なデータを追加します。
	 */
	@CheckSession
	@GetMapping("/{board_id}")
	public String detailBoard(
			@PathVariable("board_id") long board_id, 
			@RequestParam("username") String username,
			Model model, HttpSession session,
			HttpServletRequest request,
	        HttpServletResponse response) {
		
		// 1. 조회수 증가 로직 수행
	    String cookieName = "viewed_board_ids";
	    boolean isViewed = false;

	    // 쿠키 확인
	    Cookie[] cookies = request.getCookies();
	    if (cookies != null) {
	        for (Cookie cookie : cookies) {
	            if (cookieName.equals(cookie.getName())) {
	                String value = cookie.getValue(); // 예: "1_5_10"
	                List<String> viewedIds = new ArrayList<>(Arrays.asList(value.split("_")));
	                if (viewedIds.contains(String.valueOf(board_id))) {
	                    isViewed = true;
	                } else {
	                    viewedIds.add(String.valueOf(board_id));
	                    String newValue = String.join("_", viewedIds);
	                    Cookie newCookie = new Cookie(cookieName, newValue);
	                    newCookie.setPath("/");
	                    newCookie.setMaxAge(60 * 30); // 30분 유지
	                    response.addCookie(newCookie);
	                }
	                break;
	            }
	        }
	    }

	    // 쿠키가 처음이거나 board_id가 없는 경우 → 새 쿠키 생성
	    if (cookies == null || !isViewed) {
	        Cookie newCookie = new Cookie(cookieName, String.valueOf(board_id));
	        newCookie.setPath("/");
	        newCookie.setMaxAge(60 * 30); // 30분 유지
	        response.addCookie(newCookie);

	        // 조회수 증가
	        boardsService.increaseHits(board_id);
	    }
		
		
		//　１。
		BoardsDetailResDto boardsDetailResDto = boardsService.detailBoard(board_id);	//投稿ID基づいて詳細情報を取得します。
		//　２。
		boardsDetailResDto.setUsername(username);										// ユーザー名を不必要なクエリー使用しないためにClientから受け取った投稿の作成者名をboardsDetailResDtoに追加します。
		//　３。
		model.addAttribute("response", boardsDetailResDto);								//　詳細ページに必要なデータをモデルオブジェクトに追加します。
	    return "board/detail";
	}
	
	/**
	 * 新しい掲示板作成ページを表示します。
	 * 
	 * @param model モデルオブジェクト、ビューにデータを渡すために使用します
	 * @param session セッションオブジェクト、セッション情報を管理します
	 * @return "board/write" 新しい掲示板作成ページのテンプレート
	 */
	@RequireSession
	@GetMapping("/write")
	public String writeBoard(Model moel, HttpSession session) {
		
		return "board/write";
	}
	
	/**
	 * 新しい掲示板を作成し、その詳細ページへリダイレクトします。
	 * 
	 * @param boardsWriteReqDto 新しい掲示板の作成に必要な情報を含むリクエストDTO
	 * @param redirectAttributes リダイレクト時にパラメータを渡すためのオブジェクト
	 * @param session セッションオブジェクト、セッション情報を管理します
	 * @return 新しく作成した掲示板の詳細ページにリダイレクトします
	 * 
	 * 作成過程です。
	 * １。作成した後、その投稿の詳細情報を取得します。
	 * ２。RedirectAttributesオブジェクトに詳細ページを表示するためのデータ追加します。
	 * ３。リダイレクトする時にパラメーターを追加します。
	 */
	@RequireSession
	@PostMapping("/write")
	public String writeBoard(
			@ModelAttribute BoardsWriteReqDto boardsWriteReqDto, 
			RedirectAttributes redirectAttributes, 
			HttpSession session) {
		//　１。
		BoardsDetailResDto boardsDetailResDto = boardsService.writer(boardsWriteReqDto);	//　作成した後、その投稿の詳細情報を取得します。
		//　２。　																				
		redirectAttributes.addAttribute("username", boardsDetailResDto.getUsername());		//　RedirectAttributesオブジェクトに詳細ページを表示するためのデータ追加します。
		//　３。
		return "redirect:/boards/"+boardsDetailResDto.getBoard_id();						//　リダイレクトする時にパラメーターを追加します。
	}
	
	/**
	 * 投稿内容を編集します。
	 * 編集するユーザーがセッションのユーザーと一致する場合、投稿内容を更新し、詳細ページを表示します。
	 * 
	 * @param session セッションオブジェクト、現在のユーザーセッション情報を管理します
	 * @param model モデルオブジェクト、ビューにデータを渡すために使用します
	 * @param boardsEditReqDto 編集するための掲示板情報を含むリクエストDTO
	 * @return 更新後の掲示板詳細ページ（"board/detail"）を表示します
	 * 
	 * 修整過程です。
	 * １。修整するメソッドを呼び出します。
	 * ２。RedirectAttributesオブジェクトに詳細ページを表示するためのデータ追加します。
	 * ３。リダイレクトする時にパラメーターを追加します
	 */
	@RequireSession
	@PostMapping("/edit")
	public String editBoard(
			HttpSession session, 
			Model model, 
			@ModelAttribute BoardsEditReqDto boardsEditReqDto,
			RedirectAttributes redirectAttributes) {
		
		if(session.getAttribute("user").equals(boardsEditReqDto.getUsername())) {
			//１。
			BoardsDetailResDto boardsDetailResDto =  boardsService.boardsEdit(boardsEditReqDto);//　修整するメソッドを呼び出し。修整した後、その投稿の詳細情報を取得します。
			//２。
			redirectAttributes.addAttribute("username", boardsDetailResDto.getUsername());		//　RedirectAttributesオブジェクトに詳細ページを表示するためのデータ追加します。
			//３。
			return "redirect:/boards/"+boardsDetailResDto.getBoard_id();						//　リダイレクトする時にパラメーターを追加します。
		} 
		return "board/detail";
		
	}
	
	/**
	 * 編集ページを表示します。指定された掲示板の情報をフォームに設定します。
	 * 
	 * @param session セッションオブジェクト、現在のユーザーセッション情報を管理します
	 * @param model モデルオブジェクト、ビューにデータを渡すために使用します
	 * @param boardsEditReqDto 編集用の掲示板情報
	 * @return 編集ページのテンプレート（"board/edit"）を表示します
	 * 
	 * 修整ページを表示します。
	 * １。受け取ってモデルに追加します。
	 */
	@RequireSession
	@GetMapping("/edit")
	public String editBoardFrom(HttpSession session, Model model, @ModelAttribute BoardsEditReqDto boardsEditReqDto) {
		//１。
		model.addAttribute("response", boardsEditReqDto); //　boardsEditReqDtoで詳細ページの情報を受け取ってモデルに追加します。
		return "board/edit";
	}
	
	/**
	 * 指定された投稿を削除します。
	 * 
	 * @param board_id 削除する掲示板のID
	 * @param model モデルオブジェクト、ビューにデータを渡すために使用します
	 * @param session セッションオブジェクト、現在のユーザーセッション情報を管理します
	 * @return 削除結果のレスポンスとして掲示板一覧ページ（"board/list"）を表示します
	 * 
	 * 指定された投稿IDを基づいて投稿を削除する過程です。。
	 * １。投稿を削除するメソッド呼び出します。
	 * ２。モデルにデータを追加します。
	 */
	@RequireSession
	@DeleteMapping("/delete/{board_id}")
	@ResponseBody
	public String deleteBoard(@PathVariable("board_id") String board_id, Model model,HttpSession session) {
		
		//１。
		boolean result = boardsService.deleteBoard(Long.parseLong(board_id));	//　投稿を削除するメソッド呼び出し、bolleanタイプでデータを取得します。成功する場合はTRUE、失敗はFLASEです。
		//２。
		model.addAttribute("response", result);									//　bolleanタイプでデータをモデルオブジェクトに追加します。
		return "board/list";
	}
	
	/**
	 * 新しいコメントを投稿します。
	 * 
	 * @param board_id コメントを追加する掲示板のID
	 * @param user_id コメントを投稿するユーザーのID
	 * @param content コメントの内容
	 * @param session セッションオブジェクト、現在のユーザーセッション情報を管理します
	 * @return コメント投稿後のレスポンス
	 * 
	 * コメント作成過程です。
	 * １。作成するメソッドを呼びだします。
	 */
	@RequireSession
	@PostMapping("/comments")
	@ResponseBody
	public ResponseEntity<?> writeComment(
			@RequestParam("board_id") Long board_id,
			@RequestParam("user_id") Long user_id,
	        @RequestParam("content") String content,
	        HttpSession session) {
		
			//１。　作成するメソッドを呼びだし、作成が成功する場合はTRUE、失敗はFLASEです。そしてその結果をClientに渡します。
			boolean result = boardsService.writeComment(board_id, user_id, content);
	        	
	        
	        // 댓글이 달린 게시글의 작성자를 찾는다.
	        Long userId = boardsService.findUserId(board_id);

	        // 작성자에게 SSE 알림 전송
	        String message = "댓글 등록되었습니다!";
	        sseEmitterService.sendAlarm(userId, message);
			
			return ResponseEntity.ok(result);

	}

	/**
	 * 指定されたコメントを削除します。
	 * 
	 * @param comment_id 削除するコメントのID
	 * @param session セッションオブジェクト、現在のユーザーセッション情報を管理します
	 * @return 削除が成功はtrue失敗はfalse、データを渡す
	 * 
	 * 指定されたコメントIDに基づいてコメントを削除します。
	 * １。削除するメソッドを呼び出します。
	 */
	@RequireSession
	@DeleteMapping("/comments/{comment_id}")
	@ResponseBody
	public ResponseEntity<?> deleteComment(@PathVariable("comment_id") long comment_id, HttpSession session) {
		
		//　１。　指定されたコメントIDに基づいて削除するメソッドを呼び出し、bolleanタイプでデータを取得します。成功する場合はTRUE、失敗はFLASEです。そしてその結果をClientに渡します。
		return ResponseEntity.ok(boardsService.deleteComment(comment_id));
	}
	
	/**
	 * コメントのリストを取得し、ページネーションを行います。
	 * 
	 * @param pageGroup ページグループ番号（オプション）
	 * @param pageNum ページ番号（オプション）
	 * @param board_id コメントリストを取得する掲示板のID
	 * @return コメントリストとページネーション情報を含むデータを渡す
	 * 
	 * コメントページング過程です。
	 * １。ページング変数宣言
	 * ２。コメントリスト取得するメソッド指出します。
	 * ３。全てのコメントの総数を取得するメソッド指出します。
	 * ４。ページングオブジェクトを初期化します。
	 * ５。モデルオブジェクトにデータを追加します。
	 */
	@GetMapping("/comments")
	@ResponseBody
	public Map<String, Object> commentList(
			@RequestParam(value = "pageGroup", required=false) String pageGroup, 
			@RequestParam(value = "pageNum", required=false) String pageNum,
			@RequestParam(value = "board_id") int board_id) {
		
		//　１。
		int pGroup;														//　ページグルプ　例　１〜１０、１１〜２０、２１〜３０
		int pNum;														//　現在のページ番号を示す変数です。
		int amount = 10;												// 1ページあたりの表示件数 を示す変数です。
				
		if(pageGroup == null && pageNum == null) {						//　最初に値がNULLな場合、1で初期化します。
			pGroup = 1; pNum = 1;
					
		} else {														//　最近はStringタイプなのでCasting変換します。
			pGroup = Integer.parseInt(pageGroup);		
			pNum = Integer.parseInt(pageNum);
		}
		//２。
		List<Comments> comments = boardsService.findAllComments((pNum-1) * amount, amount, board_id); // ページ番号（pNum）に基づいてコメントリストを取得します。
		//３。
		int totArticles = boardsService.findAllCommentsCount(board_id);	// 指定されたボードID基づいて全てのコメントの総数を取得するメソッド指出します。
		//４。
		Paging paging = new Paging(totArticles, pNum, pGroup);			// ページネーションコメントを計算します。
		
		Map<String, Object> response = new HashMap<String, Object>();
		
		//　５。マップにコメントページングに必要なデータを追加します。
		response.put("comments", comments);
		response.put("paging", paging);
		
		return response;
	}
	


    @GetMapping("/sse/subscribe/{userId}")
    public SseEmitter subscribe(@PathVariable Long userId) {
        return sseEmitterService.subscribe(userId);
    }
}
