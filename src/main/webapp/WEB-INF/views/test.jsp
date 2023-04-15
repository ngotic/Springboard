<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
    <script src="https://code.jquery.com/jquery-1.11.3.js"></script>
</head>
<body>
<h2>commentTest </h2>

comment: <input type="text" name="comment"><br>
<button id="sendBtn" type="button">SEND</button>
<!-- send버튼을 누르면 input 태그의 내용이 저장이 되도록 하려는 것  -->
<button id="modBtn" type="button">수정</button>


<div id="commentList"></div>
<script>
    let bno = 3;
    let showList = function(bno){
        $.ajax({
            type: 'GET',       // 요청 메서드
            url: '/ch4/comments?bno='+bno,   // 요청 URI
            // headers : { "content-type":"application/json" }, > 보낼 데이터가 없으면 요청헤더는 지운다.
            // dataType: 'text',   // 전송받을 데이터의 타입> 이걸 json으로 쓰면 json이다. 근데 생략해도 기본이 json임
            // data : JSON.stringify(person), // 전송할 때 > 문자열로 x
            success : function(result) { // 반환 성공하였을 때 데이터 받음
                $("#commentList").html(toHtml(result));
            },
            error : function(){ alert("error"); }
        });

    }
    $(document).ready(function (){
        showList(bno); // 일단 document가 준비되면 데이터 보여준다.
        // modBtn이 눌리면 본격적으로 전송한다.
        $("#modBtn").click(function(){

            let cno = $(this).attr("data-cno");
            let comment = $("input[name=comment]").val();

            if(comment.trim()==''){
                alert("댓글을 입력하세요.");
                $("input[name=comment]").focus();
                return ;
            }

            $.ajax({
                type:'PATCH',
                url:'/ch4/comments/'+cno,
                headers: {"content-type":"application/json"},
                data: JSON.stringify({ cno: cno, comment:comment}),
                success: function(result){
                    alert(result);
                    showList(bno);
                }
            });
        });

        $("#sendBtn").click(function(){
            let comment = $("input[name=comment]").val();

            if(comment.trim()==''){
                alert("댓글을 입력해주세요.")
                $("input[name=comment]").focus();
                return ;
            }

            $.ajax({  ///ch4/comments?bno=1085
                type: 'POST',
                url: '/ch4/comments?bno='+bno,
                headers: {"content-type" : "application/json"}, // 보내는 데이터
                data : JSON.stringify({bno:bno, comment:comment}), // 자바스크립트 객체를, > 보낼때는 문자열화
                // dataType이 생략되어서  전송받는것은 json으로 받는다.
                success : function(result) {
                    alert(result);
                    showList(bno); // 등록이 되었으면 갱신이 되야지
                } ,
                error : function() { alert("error") }
            });
        });


                                       // 각 comment마다 붙은 수정버튼
        $("#commentList").on("click", ".modBtn", function(){
            let cno = $(this).parent().attr("data-cno");
            // comment를 가져와야 한다.
            let comment =$("span.comment", $(this).parent()).text(); //
            // 클릭된 수정부턴의 부모에 있는 span.comment만 가져온다.

            //1. comment의 내용을 input에 뿌려주기
            $("input[name=comment]").val(comment);
            //2. cno 전달하기
            $("#modBtn").attr("data-cno", cno); // 여기다가 cno를 저장한다.

            // id 의 modBtn은 맨위의것
            // 이런 두가지 정보를 가지면 컨트롤러로 전달해서 update를 한다.
        });

        // jQuery의 on메서드
        // 이부분의 함수가 동작이 안됨 .. .delBtn 자체가 아직 생기지 않음
        // $(".delBtn").click(function(){
        $("#commentList").on("click", ".delBtn", function(){
            let cno = $(this).parent().attr("data-cno");
            let bno = $(this).parent().attr("data-bno");
            alert("delBtn clicked");
            $.ajax({
                type:'DELETE',      // 이거 타입이 진짜 중요
                url:'/ch4/comments/'+cno+'?bno='+bno,
                success: function(result){
                    alert(result); //DEL_OK가 뜬다.
                    showList(bno); // 다시 보여주는거 호출
                }
            }); // $.ajax()
        }); // 이전에 SEND 버튼을 눌러야 delBtn 버튼이 만들어지고 거기에 이벤트가 등록된다.
    });

    let toHtml = function(comments) {
        let tmp = "<ul>"; // 리스트를 구성
        comments.forEach(function(comment){
            tmp += '<li data-cno='+comment.cno;
            tmp += ' data-pcno=' + comment.pcno;
            tmp += ' data-bno=' + comment.bno + '>';
            tmp += ' commenter=<span class="commenter">' + comment.commenter + '</span>'
            tmp += ' comment=<span class="comment">' + comment.comment + '</span>'
            tmp += ' up_data='+comment.up_date
            tmp += '<button class="delBtn">삭제</button>'
            tmp += '<button class="modBtn">수정</button>'
            tmp += '</li>'
        }) // 리스트로 하나씩 for문 돈다.
        return tmp + "</ul>";
    }

</script>
</body>
</html>
