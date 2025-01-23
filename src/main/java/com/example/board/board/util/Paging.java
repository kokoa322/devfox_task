package com.example.board.board.util;
import lombok.Data;

@Data
public class Paging {

    private int pgn;
    private int page;
    private int amount = 10;
    private int pageNums = 10;
    private int total_page;
    private int tpgn;
    private int start;
    private int last;
    private int totArticles;


    public Paging (int totArticles , int pNum, int pGroup) {
        this.totArticles = totArticles;
        this.total_page = (int) Math.ceil(totArticles/(double)this.amount);
        this.pgn = pGroup;
        this.page = pNum;
        this.tpgn = (int) Math.ceil(this.total_page/(double)this.amount);
        this.start = (pNum-1) * this.pageNums;
    }
}
