package com.example.board;

import java.util.Locale;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpSession;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.board.aspect.CheckSession;
import com.example.board.board.dao.BoardsDao;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class HomeController {
	
	private final BoardsDao boardsDao;
	
	@CheckSession
	@GetMapping("/")
	public String home(Locale locale, Model model, HttpSession session) {

	    return "redirect:/boards"; 
	}
	
	/**
	 * サーバーが起動する際に、掲示板のダミーデータを作成します。
	 * 
	 * このメソッドは、アプリケーションの初期化時に実行され、
	 * テスト用の掲示板データをデータベースに挿入します。
	 */
	@PostConstruct
	public void init() {
		boardsDao.testBoards();
	}
	
}
