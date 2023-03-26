package com.fastcampus.ch4.dao;

import com.fastcampus.ch4.domain.BoardDto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/spring/**/root-context.xml"})
public class BoardDaoImplTest {
    @Autowired
    BoardDao boardDao;

    @Test
    public void insertData() throws Exception {
        boardDao.deleteAll();
        for(int i=1; i<=220;i++) {
            BoardDto boardDto = new BoardDto("title"+i, "no content", "asdf");
            boardDao.insert(boardDto);
        }
    }

    @Test
    public void select() throws Exception {
        assertTrue( boardDao != null);
        System.out.println("boardDao = "+ boardDao);
        BoardDto boardDto = boardDao.select(1);
        System.out.println("boardDto = "+ boardDto);
        assertTrue(boardDto.getBno().equals(1));
    }

//    @Test
//    public void delete() throws Exception {
//        boardDao.deleteAll();
//        assertTrue(boardDao.count() == 0);
//    }

    @Test
    public void insert() {
    }

    @Test
    public void update() {
    }

    @Test
    public void increaseViewCnt() {
    }

    @Test
    public void selectPage() throws Exception {
        Map map = new HashMap();
        map.put("offset", 0);
        map.put("pageSize", 10);
        List<BoardDto> list = boardDao.selectPage(map);

        for( BoardDto b : list){
            System.out.println(b);
        }

    }

    @Test
    public void selectAll() {
    }

    @Test
    public void deleteAll() {
    }

    @Test
    public void count() {
    }
}