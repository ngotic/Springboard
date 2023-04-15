<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <title>Title</title>
  <script src="https://code.jquery.com/jquery-1.11.3.js"></script>
</head>
<body>
  <h2>{name:"abc", age:10}</h2>
  <button id="sendBtn" type="button"> SEND </button>
  <h2>Data From Server : </h2>
  <div id="data"></div>
  <script>
    $(document).ready(function (){
        let person = {name:"abc", age:10};
        let person2 = {};
        $("#sendBtn").click(function(){
          $.ajax({
            type: 'POST',       // 요청 메서드
            url: '/ch4/send',   // 요청 URI
            headers : { "content-type":"application/json" }, // 요청헤더
            dataType: 'text',   // 전송받을 데이터의 타입
            data : JSON.stringify(person), // 전송할 때
            success : function(result) {
              // 반환 성공하였을 때 데이터 받음
              // post, /ch4/send로 데이터 쏨, 성공시 받음
              person2 = JSON.parse(result);
              alert("received="+result); // json 객체를 출력
              $("#data").html("name="+person2.name+", age="+person2.age);
            },
            error : function(){ alert("error"); }
          });
          alert("the request is sent");
        });
    });
  </script>
</body>
</html>
