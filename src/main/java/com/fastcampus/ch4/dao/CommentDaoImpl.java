package com.fastcampus.ch4.dao;

import com.fastcampus.ch4.domain.CommentDto;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository // > test는 나중에 하기
public class CommentDaoImpl implements CommentDao{

    // SqlSession 객체 만들고 namespace 지정해서 주입하는 부분이 Dao이다.
    // SqlSession을 Autowired로 주입하고
    @Autowired
    private SqlSession session;
    private static String namespace = "com.fastcampus.ch4.dao.CommentMapper.";

    @Override
    public int count(Integer bno) throws Exception {
        return session.selectOne(namespace+"count", bno);
    }

    // cno, bno > 로
    @Override
    public int deleteAll(Integer bno) {
        return session.delete(namespace+"deleteAll", bno);
    }

    @Override
    public int delete(Integer cno, String commenter) throws Exception {
        // type은 Integer, String으로 받는데 Map으로 쿼리를 넘겨준다.
        Map map = new HashMap();
        map.put("cno", cno);
        map.put("commenter", commenter);
        return session.delete(namespace+"delete", map);
    }

    @Override
    public int insert(CommentDto dto) throws Exception {
        return session.insert(namespace+"insert", dto);
    }

    @Override //selectList 사용
    public List<CommentDto> selectAll(Integer bno) throws Exception {
        return session.selectList(namespace+"selectAll", bno);
    }

    @Override //selectOne
    public CommentDto select(Integer cno) throws Exception {
        return session.selectOne(namespace+"select", cno);
    }

    @Override
    public int update(CommentDto dto) throws Exception {
        return session.update(namespace+"update", dto);
    }
}
