package com.fastcampus.ch4.controller;


import com.fastcampus.ch4.domain.CommentDto;
import com.fastcampus.ch4.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;


//@Controller
//@ResponseBody
@RestController
public class CommentController{
    @Autowired
    CommentService service;
//    {
//        "pcno":0,
//            "comment":"hi"
//    }


// ★ 1. ResponseBody를 클래스 위에 붙일 수 있다.

    // 수정 정의 /ch4/comments/26
    @PatchMapping("/comments/{cno}")
    public ResponseEntity<String> modify(@PathVariable Integer cno,
                                         @RequestBody CommentDto dto,
                                         HttpSession session){
        //String commenter = (String)session.getAttribute("id");
        String commenter = "asdf";
        dto.setCommenter(commenter);
        dto.setCno(cno);
        System.out.println("dto = " + dto);
        try {
            if(service.modify(dto)!=1)
                throw new Exception("Write failed");
            return new ResponseEntity<>("MOD_OK", HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("MOD_ERR", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/comments") // /ch4/comments?bno=1085 POST
    // 댓글을 등록하는 메서드
    public ResponseEntity<String> write(@RequestBody CommentDto dto,
                                        Integer bno, HttpSession session) {
        String commenter = "asdf";
        dto.setCommenter(commenter);
        dto.setBno(bno);
        System.out.println("dto = " + dto);
        try {
            if(service.write(dto)!=1)
                throw new Exception("Write failed.");
            return new ResponseEntity<String>("WRT_OK", HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<String>("WRT_ERR",HttpStatus.BAD_REQUEST);
        }
    }

    // Mapping된 URL의 일부를 읽어올 떄는 PathVariaible을 붙어야 한다.
    // {cno} 이렇게 cno 부분은 PathVariable이라고 한다. @PathVariable로 지정해야 한다.
    // 쿼리스트링 있는 부분은 그냥 붙인다.
    // 삭제와 같은 그냥 뭔가 상태 메세지만 담아서 전달해도 될 때는 String하나 넣음
    @DeleteMapping("/comments/{cno}")  // http://localhost/ch4/comments/10?bno=3<- 삭제할 댓글번호,
     // 이거 어노테이션 순서는 상관없다.
    public ResponseEntity<String> remove(@PathVariable Integer cno, Integer bno, HttpSession session){
//        String commenter = (String)session.getAttribute("id");
        // 로그인 과정 따로 없이 테스트를 하기위해~
        String commenter = "asdf";
        try {
            int rowCnt = service.remove(cno, bno, commenter);
            if ( rowCnt !=1 )
                throw new Exception("Delete Failed");
            return new ResponseEntity<>("DEL_OK", HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("DEL_ERR", HttpStatus.BAD_REQUEST);
        }
    }


    @GetMapping("/comments") // localhost/ch4/comments?bno=3

    public ResponseEntity<List<CommentDto>> list(Integer bno){
        List<CommentDto> list = null;
        try {
            list = service.getList(bno);
            return new ResponseEntity<List<CommentDto>>(list, HttpStatus.OK);
        } catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<List<CommentDto>>(HttpStatus.BAD_REQUEST);
        }
    }
}





//CommentService, CommentDto
/*

@RestController
public class CommentController {
    // Controller에서는 서비스 단에 있는 클래스를 객체로 가져온다.
    @Autowired
    CommentService service;
//    {
//        "pcno":0,
//            "comment":"hihihi",
//            "commenter":"asdf"
//
//   }
    // 지정된 게시물의 모든 댓글을 가져오는 메서드
    @GetMapping("/comments") // comments?bno=1080 GET > 글번호 기준
    public ResponseEntity<List<CommentDto>> list(Integer bno) {
        List<CommentDto> list = null;
        try {
            list = service.getList(bno); // list와 상태메세지를 담는다.
            return new ResponseEntity<List<CommentDto>>(list, HttpStatus.OK); // 200
        } catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<List<CommentDto>>(HttpStatus.BAD_REQUEST); // 400
        }
        // ResponseEntity에 상태값 하나만 넣거나, 데이터, 상태값 이렇게 넣는다.
    }

    // 댓글을 수정하는 메서드
    @PatchMapping("/comment/{cno}") //      /ch4/comments/26  > PATCH
    public ResponseEntity<String> modify(@PathVariable Integer cno, @RequestBody CommentDto dto){
        String commenter = "asdf";
        dto.setCommenter(commenter);
        dto.setCno(cno);
        System.out.println("dto = " + dto);

        try{
            if(service.modify(dto)!=1)
                throw new Exception("Write failed");
            return new ResponseEntity<String>("MOD_OK", HttpStatus.OK);

        } catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<String>("MOD_ERR", HttpStatus.BAD_REQUEST);
        }
    }


    // {
    //    "pcno":0,
    //    "comment" : "hi"
    // }

    // 댓글을 등록하는 메서드
    @PostMapping("/contents") // /ch4/comments?bno=1085 POST
    public ResponseEntity<String> write(@RequestBody CommentDto dto, Integer bno, HttpSession session){
        // String commenter = (String)session.getAttribute("id");
        String commenter = "asdf";
        dto.setCommenter(commenter);
        dto.setBno(bno);
        System.out.println("dto = " + dto);

        try {
            if(service.write(dto)!=1)
                throw new Exception("Write failed");
            // Generic으로 뭘 넣을지 써주는 것이다.
            return new ResponseEntity<String>("WRT_OK", HttpStatus.OK);

        } catch(Exception e){
            e.printStackTrace();
            return new ResponseEntity<String>("WRT_ERR", HttpStatus.BAD_REQUEST);
        }
    }





    @DeleteMapping("/comments/{cno}") // comments/1?bno=1085  // bno는 쿼리 스트링, PathVariable은
    public ResponseEntity<String> remove(@PathVariable Integer cno, Integer bno, HttpSession session){
        // String commenter = (String)session.getAttribute("id");
        String commenter = "asdf";
        try {
            int rowCnt = service.remove(cno, bno, commenter);
            if(rowCnt != 1)
                throw new Exception("Delete Failed");
            return new ResponseEntity<String>("DEL_OK", HttpStatus.OK);
        } catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<String>("DEL_ERR", HttpStatus.BAD_REQUEST);
        }
    }

}
*/
