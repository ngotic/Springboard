package com.fastcampus.ch4.controller;

import java.net.URLEncoder;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.fastcampus.ch4.dao.*;
import com.fastcampus.ch4.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/login")
public class LoginController {
    @Autowired
    UserDao userDao;

    @GetMapping("/login")
    public String loginForm() {
        return "loginForm";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        // 1. 세션을 종료
        session.invalidate();
        // 2. 홈으로 이동
        return "redirect:/";
    }

    @PostMapping("/login")
    public String login(String id, String pwd, String toURL, boolean rememberId,
                        HttpServletRequest request, HttpServletResponse response) throws Exception {

        // 1. id와 pwd를 확인
        if(!loginCheck(id, pwd)) {
            // 2-1   일치하지 않으면, loginForm으로 이동
            String msg = URLEncoder.encode("id 또는 pwd가 일치하지 않습니다.", "utf-8");
            return "redirect:/login/login?msg="+msg;
        }
        // 2-2. id와 pwd가 일치하면,
        //  세션 객체를 얻어오기 > 세션은 컨트롤러에서 만들어진다.
        HttpSession session = request.getSession();
//        HttpSession session = request.getSession(); <---- 여기서 세션을 얻어오는데요. 세션이 없으면 새로 생성합니다.
//        HttpSession session = request.getSession(true); // 위와 동일. 세션이 있으면 반환 없으면 새로 생성
//        HttpSession session = request.getSession(false); // 세션이 있으면 반환 없으면 생성 안함.

        //  세션 객체에 id를 저장
        session.setAttribute("id", id);

        if(rememberId) {
            //     1. 쿠키를 생성
            Cookie cookie = new Cookie("id", id); // ctrl+shift+o 자동 import
//		       2. 응답에 저장
            response.addCookie(cookie);
        } else {
            // 1. 쿠키를 삭제
            Cookie cookie = new Cookie("id", id); // ctrl+shift+o 자동 import
            cookie.setMaxAge(0); // 쿠키를 삭제
//		       2. 응답에 저장
            response.addCookie(cookie);
        }
//		3. 홈으로 이동
        toURL = toURL==null || toURL.equals("") ? "/" : toURL;

        return "redirect:"+toURL;
    }

    private boolean loginCheck(String id, String pwd) {
        User user = null;

        try {
            user = userDao.selectUser(id); // 원래
            //user = userDao.selectUser2(id, pwd); // 임시 sql injection test
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        //return user!=null; // 임시 sql injection test
        return user!=null && user.getPwd().equals(pwd); //원래
    }
}
//        return "asdf".equals(id) && "1234".equals(pwd);