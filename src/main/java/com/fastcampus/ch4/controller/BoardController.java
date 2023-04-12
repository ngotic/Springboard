package com.fastcampus.ch4.controller;


import com.fastcampus.ch4.domain.BoardDto;
import com.fastcampus.ch4.domain.PageHandler;
import com.fastcampus.ch4.domain.SearchCondition;
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
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/board")
public class BoardController {

    @Autowired
    BoardService boardService;
    @PostMapping("/modify")
    //public String remove(Integer bno, Integer page, Integer pageSize, Model m, HttpSession session, RedirectAttributes rattr )
    
    public String modify(BoardDto boardDto,  SearchCondition sc, Model m,  HttpSession session, RedirectAttributes rattr){
        String writer = (String)session.getAttribute("id");
        boardDto.setWriter(writer);

        System.out.println();
        try {
//            m.addAttribute("page", page); // 왜 이걸로 담으면 안대지?
//            m.addAttribute("pageSize", pageSize);
            int rowCnt = boardService.modify(boardDto);
            if (rowCnt !=1)  throw new Exception("Modify Failed");
            //RedirectAttributes 이게 있으면 다 이걸로 담아서 보내라
//            rattr.addAttribute("page", page);
//            rattr.addAttribute("pageSize", pageSize);
            rattr.addFlashAttribute("msg", "MOD_OK");
            return "redirect:/board/list"+sc.getQueryString();
        } catch (Exception e) {
            e.printStackTrace();
            m.addAttribute(boardDto);
            m.addAttribute("msg", "MOD_ERR");
            return "board";
        }
    }

    @PostMapping("/write")
    public String write(BoardDto boardDto, Model m, HttpSession session, RedirectAttributes rattr) {
        String writer = (String)session.getAttribute("id");
        boardDto.setWriter(writer);
        try {
            int rowCnt = boardService.write(boardDto); //insert
            if (rowCnt!=1 ) // 글이 안써지면 에러를 보내준다.
                throw new Exception("Write failed");
            rattr.addFlashAttribute("msg", "WRT_OK"); // session을 이용한 1회성 저장
            return "redirect:/board/list";
            // m.addAttribute("msg", "WRT_OK"); 잘 처리되었으면 이렇게 보내주면 좋다. 근데 이게 계쏙 주소창에 남아서
            // 결국에는 redirect Attribute를 쓴다.
        } catch (Exception e) {
            e.printStackTrace();
            m.addAttribute("boardDto", boardDto);
            //m.addAttribute(boardDto); // 앞에 안써도 된다.
            m.addAttribute("mode", "new");
            m.addAttribute("msg", "WRT_ERR");
            return "board";
        }
    }
    @GetMapping("/write")
    public String write(Model m) {
        m.addAttribute("mode", "new");
        return "board"; // 읽기와 쓰기에 쓰는데, 쓰기에 사용할 떄는 mode-new로 준다.
    }

    @PostMapping("/remove")
    public String remove(Integer bno, SearchCondition sc, Model m, HttpSession session, RedirectAttributes rattr ) {
        String writer = (String)session.getAttribute("id");
        try {
//            m.addAttribute("page", page);
//            m.addAttribute("pageSize", pageSize); // 리다이렉트로 보내는건데 뒤에 쿼리스트링으로 보내려면 이렇게 보내면 안된다.
            int rowCnt = boardService.remove(bno, writer); // remove는 bno와 writer가 필요하다. writer는 session에서 가져온다.
            if(rowCnt != 1)
                throw new Exception("board remove error");
            // m.addAttribute("msg", "DEL_OK");
            // Session의 Flash영역에 데이터를 저장해놓고 Flash영역에서 데이터를 가지고 와서 사용할수도 있지요.
//            rattr.addAttribute("page", page);
//            rattr.addAttribute("pageSize", pageSize);
            rattr.addFlashAttribute("msg", "DEL_OK");

        } catch (Exception e) {

            e.printStackTrace();
            // m.addAttribute("msg", "DEL_ERR");
            rattr.addFlashAttribute("msg", "DEL_ERR");
            // addFlashAttribute는 model에 addAttribute랑 같이 담아도 전송된다.
        }
        // 이렇게 모델에 담으면 redirect할 때 뒤에 ?page= & pageSize= 이렇게 붙는다.
        return "redirect:/board/list"+sc.getQueryString(); // 이렇게 아예 뒤에 붙여버림
    }

    @GetMapping("/read")
    public String read(Integer bno,  SearchCondition sc,RedirectAttributes rattr, Model m){
        // ?bno=숫자 이렇게 오고 이것을 받아서 
        try {
            BoardDto boardDto = boardService.read(bno); // 읽어온 걸 받아야 한다.
                                                // 함수 호출하고 얻어온 객체를 
            m.addAttribute("boardDto", boardDto); // 이것과 아래문장이 동일 > 이게 board.jsp로 전달
            // 여기다가 저장
            // m.addAttribute(boardDto);
//            m.addAttribute("page", page);
//            m.addAttribute("pageSize", pageSize);

        } catch (Exception e) {
            e.printStackTrace();
            rattr.addFlashAttribute("msg", "READ_ERR");
            return "redirect:/board/list"+sc.getQueryString();
        }
        return "board"; // 그리고 이걸 board.jsp에 전송
    }

    @GetMapping("/list")
    public String list(SearchCondition sc,  Model m, HttpServletRequest request) {
        if(!loginCheck(request)) {
            return "redirect:/login/login?toURL="+request.getRequestURL();
        }
//        이렇게 지우고 다 default로 할당
//        if(page==null) page=1;
//        if(pageSize==null) pageSize=10;
        try {
            int totalCnt = boardService.getSearchResultCnt(sc);
            m.addAttribute("totalCnt", totalCnt);
            PageHandler pageHandler = new PageHandler(totalCnt, sc);

            List<BoardDto> list = boardService.getSearchResultPage(sc); // 리스트를 모델에 담은
            m.addAttribute("list", list);
            m.addAttribute("ph", pageHandler);

            Instant startOfToday = LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant();
            m.addAttribute("startOfToday", startOfToday.toEpochMilli());

        } catch (Exception e) {
            e.printStackTrace();
            m.addAttribute("msg", "LIST_ERR");
            m.addAttribute("totalCnt", 0);
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
