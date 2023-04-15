package com.fastcampus.ch4.service;

import com.fastcampus.ch4.domain.CommentDto;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface CommentService {

    public int getCount(Integer bno) throws Exception ;

    public int remove(Integer cno, Integer bno, String commenter) throws Exception ;

    public int write(CommentDto commentDto) throws Exception;

    public List<CommentDto> getList(Integer bno) throws Exception;

    public CommentDto read(Integer cno) throws Exception ;

    public int modify(CommentDto commentDto) throws Exception;
}
