package com.fastcampus.ch4.domain;

import org.junit.Test;

import static org.junit.Assert.*;

public class PageHandlerTest {

    @Test
    public void test1() {
        PageHandler ph = new PageHandler(250, 1); // 경계선 테스트
        // 현재 페이지가 11이면 11, 12 ,13 ~ 20 까지
        ph.print();
        System.out.println("ph = " + ph);
        assertTrue(ph.getBeginPage() == 1);
        assertTrue(ph.getEndPage() == 10);
    }

    @Test
    public void test2() {
        PageHandler ph = new PageHandler(250, 11); // 내부 값 하나 골라서 테스트
        // 현재 페이지가 11이면 11, 12 ,13 ~ 20 까지
        ph.print();
        System.out.println("ph = " + ph);
        assertTrue(ph.getBeginPage() == 11);
        assertTrue(ph.getEndPage() == 20);
    }

    @Test
    public void test3() {
        PageHandler ph = new PageHandler(255, 10); // 경계선 테스트
        // 현재 페이지가 25이면 begin = 21, end 25인데 showNext가 false인지 확인
        ph.print();
        System.out.println("ph = " + ph);
        assertTrue(ph.getBeginPage() == 1);
        assertTrue(ph.getEndPage() == 10);
    }

}