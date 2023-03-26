package com.fastcampus.ch4.dao;


import com.fastcampus.ch4.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;

@Repository
public class UserDaoImpl implements UserDao{
    @Autowired
    DataSource ds;

    @Override
    public int deleteUser(String id) throws Exception {
        String sql = "delete from user_info where id=?";

        try ( // try-with-resources (  )에 exception 걸릴만한 애들을 넣어준다.
              // 그리고 이부분엔 초기화하는 구문이 들어간다.
            Connection conn = ds.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
                ){
            pstmt.setString(1, id);
            return pstmt.executeUpdate();
        }
    }

    @Override
    public User selectUser(String id) throws Exception {
        User user=null;
        String sql = "select * from user_info where id=?";
        try (
                Connection conn = ds.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql);

                // 값을 받아올 때만 resultSet을 쓴다.
        ) {
            pstmt.setString(1, id);
            ResultSet rs  = pstmt.executeQuery(); // select만!! 나머지 insert, update, delete는 executeQuery다.
            if(rs.next()){ // 값을 잘 받아왔나??
                // rs.next()로 인덱스 옮기고 > rs.get타입(어느요소) > 이방식으로 꺼내온다.
                user = new User();
                user.setId(rs.getString(1));
                user.setPwd(rs.getString(2));
                user.setName(rs.getString(3));
                user.setEmail(rs.getString(4));
                user.setBirth( new Date(rs.getDate(5).getTime()) );
                // sql Date객체로 받아오고 이걸 getTime()으로 해서 시간을 얻어오고 util Date로 변환
                user.setSns(rs.getString(6));
                user.setReg_date(new Date(rs.getTimestamp(7).getTime()));
            }
        }
        return user;
    }

//    @Override // ' " " '안에 '' 안에 "" > 잠시 sql injection 확인 용도
//    public User selectUser2(String id, String pwd) throws Exception {
//        User user=null;
//        String sql = "select * from user_info where id='"+id+"' and pwd= '" + pwd+ "'";
//        System.out.println("sql : "+sql);
//        try (
//                Connection conn = ds.getConnection();
//                Statement stmt = conn.createStatement();
//                //preparedStatement를 일반 statement로 바꿈
//        ) {
//
//            ResultSet rs  = stmt.executeQuery(sql); // select만!! 나머지 insert, update, delete는 executeQuery다.
//            if(rs.next()){ // 값을 잘 받아왔나??
//                // rs.next()로 인덱스 옮기고 > rs.get타입(어느요소) > 이방식으로 꺼내온다.
//                user = new User();
//                user.setId(rs.getString(1));
//                user.setPwd(rs.getString(2));
//                user.setName(rs.getString(3));
//                user.setEmail(rs.getString(4));
//                user.setBirth( new Date(rs.getDate(5).getTime()) );
//                // sql Date객체로 받아오고 이걸 getTime()으로 해서 시간을 얻어오고 util Date로 변환
//                user.setSns(rs.getString(6));
//                user.setReg_date(new Date(rs.getTimestamp(7).getTime()));
//            }
//        }
//        return user;
//    }

    @Override
    public int insertUser(User user) throws Exception {
        String sql = "insert into user_info values (?, ?, ?, ?, ?, ?, now())";
        // insert, update 부류는 pstmt에 넣어서 실행
        try (
                Connection conn = ds.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql);
                ){
            pstmt.setString(1, user.getId());
            pstmt.setString(2, user.getPwd());
            pstmt.setString(3, user.getName());
            pstmt.setString(4,user.getEmail() );
            // java.sql.Date와 java.util.Date와 구분하기 위해서
            pstmt.setDate(5, new java.sql.Date(user.getBirth().getTime())); // type은 setDate
            pstmt.setString(6, user.getSns());

            return pstmt.executeUpdate();
        }
    }

    @Override
    public int updateUser(User user) throws Exception {
        String sql = "update user_info " +
                     "set pwd = ?, name = ?, email = ?, birth = ?, sns=?, reg_date=? " +
                     "where id = ?";
        try(Connection conn = ds.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
        ){
            pstmt.setString(1, user.getPwd());
            pstmt.setString(2, user.getName());
            pstmt.setString(3, user.getEmail());
            pstmt.setDate(4, new java.sql.Date(  user.getBirth().getTime() ));
            // timestamp로 nanosecond까지 찍을 수 있다. 2023-03-19 23:59:23.232 이런식으로 나온다.
            pstmt.setTimestamp(6, new java.sql.Timestamp(user.getReg_date().getTime()));
            pstmt.setString(7, user.getId());
            return pstmt.executeUpdate();
        }
    }

    @Override
    public int count() throws Exception {
        String sql = "select count(*) from user_info";
        try (
                Connection conn = ds.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql);
                ResultSet rs = pstmt.executeQuery();
                ){
            rs.next();
            int result = rs.getInt(1);

            return result;
        }
        // 여기는 return 필요 없다.
    }

    @Override
    public void deleteAll() throws Exception {
        try (Connection conn = ds.getConnection();){
            String sql = "delete from user_info";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.executeUpdate();
        }
    }
}
