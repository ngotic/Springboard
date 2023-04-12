package com.fastcampus.ch4.service;

import com.fastcampus.ch4.dao.BoardDao;
import com.fastcampus.ch4.domain.BoardDto;
import com.fastcampus.ch4.domain.SearchCondition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

// 여기서는 트랜잭션을 적용할게 없어서 예외를 컨트롤러에게 던진다.
@Repository
public class BoardServiceImpl implements BoardService {
    @Autowired
    BoardDao boardDao;


    @Override
    public List<BoardDto> getSearchResultPage(SearchCondition sc) throws Exception {
        return boardDao.searchSelectPage(sc);
    }

    @Override
    public int getSearchResultCnt(SearchCondition sc) throws  Exception {
        return boardDao.searchResultCnt(sc);
    }

    @Override
    public int getCount() throws Exception {
        return boardDao.count();
    }

    @Override
    public int remove(Integer bno, String writer) throws Exception {
        return boardDao.delete(bno, writer);
    }

    @Override
    public int write(BoardDto boardDto) throws Exception {
        //throw new Exception("test"); // 고의로 실패를 내는 문장
        return boardDao.insert(boardDto);
    }

    @Override
    public List<BoardDto> getList() throws Exception {
        return boardDao.selectAll();
    }

    @Override
    public BoardDto read(Integer bno) throws Exception {
        BoardDto boardDto = boardDao.select(bno);
        boardDao.increaseViewCnt(bno);

        return boardDto;
    }

    @Override
    public List<BoardDto> getPage(Map map) throws Exception {
        return boardDao.selectPage(map);
    }

    @Override
    public int modify(BoardDto boardDto) throws Exception {
        return boardDao.update(boardDto);
    }
}
