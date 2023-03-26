package com.fastcampus.ch4.service;

import com.fastcampus.ch4.dao.BoardDao;
import com.fastcampus.ch4.domain.BoardDto;

import java.util.List;
import java.util.Map;

public interface BoardService {
    public int getCount() throws Exception;
    public int remove(Integer bno, String writer) throws Exception ;
    public int write(BoardDto boardDto) throws Exception;
    public List<BoardDto> getList() throws Exception;
    public BoardDto read(Integer bno) throws Exception;
    public List<BoardDto> getPage(Map map) throws Exception;
    public int modify(BoardDto boardDto) throws Exception;

}
