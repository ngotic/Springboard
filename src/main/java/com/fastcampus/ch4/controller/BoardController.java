package com.fastcampus.ch4.controller;


import com.fastcampus.ch4.domain.BoardDto;
import com.fastcampus.ch4.domain.PageHandler;
import com.fastcampus.ch4.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/board")
public class BoardController {
    // 주석을 입력 해보자 이게 git 인가?
    @Autowired
    BoardService boardService;
    @PostMapping("/remove")
    public String remove(Integer bno, Integer page, Integer pageSize, Model m, HttpSession session, RedirectAttributes rattr ) {
        String writer = (String)session.getAttribute("id");
        try {
            m.addAttribute("page", page);
            m.addAttribute("pageSize", pageSize);
            int rowCnt = boardService.remove(bno, writer); // remove는 bno와 writer가 필요하다. writer는 session에서 가져온다.
            if(rowCnt != 1)
                throw new Exception("board remove error");
            // m.addAttribute("msg", "DEL_OK");
            rattr.addFlashAttribute("msg", "DEL_OK");
        } catch (Exception e) {
            e.printStackTrace();
            // m.addAttribute("msg", "DEL_ERR");
            rattr.addFlashAttribute("msg", "DEL_ERR");
        }
        // 이렇게 모델에 담으면 redirect할 때 뒤에 ?page= & pageSize= 이렇게 붙는다.
        return "redirect:/board/list";
    }

    @GetMapping("/read")
    public String read(Integer bno, Integer page, Integer pageSize, Model m){
        // ?bno=숫자 이렇게 오고 이것을 받아서 
        try {
            BoardDto boardDto = boardService.read(bno); // 읽어온 걸 받아야 한다.
                                                // 함수 호출하고 얻어온 객체를 
            m.addAttribute("boardDto", boardDto); // 이것과 아래문장이 동일 > 이게 board.jsp로 전달
                           // 여기다가 저장
            // m.addAttribute(boardDto);
            m.addAttribute("page", page);
            m.addAttribute("pageSize", pageSize);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return "board"; // 그리고 이걸 board.jsp에 전송
    }

    @GetMapping("/list")
    public String list(Integer page, Integer pageSize, Model m, HttpServletRequest request) {
        if(!loginCheck(request)) {
            return "redirect:/login/login?toURL="+request.getRequestURL();
        }

        if(page==null) page=1;
        if(pageSize==null) pageSize=10;

        try {
            //
            int totalCnt = boardService.getCount();
            PageHandler pageHandler = new PageHandler(totalCnt, page, pageSize);

            Map map = new HashMap();
            map.put("offset", (page-1)*pageSize);
            map.put("pageSize", pageSize);

            List<BoardDto> list = boardService.getPage(map); // 리스트를 모델에 담은
            m.addAttribute("list", list);
            m.addAttribute("ph", pageHandler);
//          m.addAttribute("page", page);
//          m.addAttribute("pageSize", pageSize);


        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return "boardList";
    }

    private boolean loginCheck(HttpServletRequest request) {
        // 1. 세션을 얻고
        HttpSession session = request.getSession();
        // 2. 세션에 id가 있는지 확인 있으면 true를 반환
        return session.getAttribute("id")!=null;
    }
}
