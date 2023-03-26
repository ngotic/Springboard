package com.fastcampus.ch4.domain;

public class PageHandler {

    private int totalCnt; // 총 게시물 갯수
    private int pageSize; // 한 페이지의 크기
    private int naviSize = 10;  // 페이지 내비게이션의 크기 > 이건 default로 설정
    private int totalPage; // 전체 페이지의 개수
    private int page;      // 현재 페이지
    private int beginPage; // 네비게이션의 첫번째 페이지
    private int endPage;   // 네비게이션의 마지막 페이지
    private boolean showPrev;  // 이전 페이지로 이동하는 링크를 보여줄 것인지의 여부
    private boolean showNext;  // 다음 페이지로 이동하는 링크를 보여줄 것인지의 여부

    public int getTotalCnt() {
        return totalCnt;
    }

    public void setTotalCnt(int totalCnt) {
        this.totalCnt = totalCnt;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getNaviSize() {
        return naviSize;
    }

    public void setNaviSize(int naviSize) {
        this.naviSize = naviSize;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getBeginPage() {
        return beginPage;
    }

    public void setBeginPage(int beginPage) {
        this.beginPage = beginPage;
    }

    public int getEndPage() {
        return endPage;
    }

    public void setEndPage(int endPage) {
        this.endPage = endPage;
    }

    public boolean isShowPrev() {
        return showPrev;
    }

    public void setShowPrev(boolean showPrev) {
        this.showPrev = showPrev;
    }

    public boolean isShowNext() {
        return showNext;
    }

    public void setShowNext(boolean showNext) {
        this.showNext = showNext;
    }

    public PageHandler(int totalCnt, int page){

        this(totalCnt, page, 10);
    }
    // 이걸 계산해서 거기에 맞게 화면을 보여준다.
    public PageHandler(int totalCnt, int page, int pageSize){
        this.totalCnt = totalCnt;
        this.page = page;
        this.pageSize = pageSize;
        totalPage = (int)Math.ceil( totalCnt / (double)pageSize );
        // Math 클래스에선 반환값 double이 많음
        beginPage = (page-1) / naviSize * naviSize  + 1;
        // 현재 page가 5면 beginPage가 1 ~ 5
        // page가 15개 있으면 beginPage 11 ~ 15
        // page가 11개 있으면 beginPage는 11
        // page가 25개 있으면 beginPage는 21
        // page / pageSize *pageSize  + 1 > 패턴 파악하기
        endPage = Math.min(beginPage-1 + naviSize, totalPage);// 경우의 수 따지고 > 세분화, 두가지 경우의 수다. <totalPage보다 작을 때!>가 있다.
                               // 이 둘중에 작은 값을 endPage로 써라
        // showPrev, showNext> 이전 페이지로 갈지 보여줄지 말지?
        showPrev = beginPage != 1;
        showNext = endPage != totalPage;
        // endPage -> beginPage+10(naviSize)
        // 근데 글 개수가 작으면 naviSize까지 못간다. > // totalPage naviSize보다 작으면 됨
    }

    void print() {
        System.out.println("page = " + page);
        System.out.println(showPrev ? "[PREV]": "" );
        for( int i=beginPage; i<=endPage ; i++){
            System.out.println(i+ " ");
        }
        System.out.println(showNext ? "[NEXT]": "");
    }

    @Override
    public String toString() {
        return "PageHandler{" +
                "totalCnt=" + totalCnt +
                ", pageSize=" + pageSize +
                ", naviSize=" + naviSize +
                ", totalPage=" + totalPage +
                ", page=" + page +
                ", beginPage=" + beginPage +
                ", endPage=" + endPage +
                ", showPrev=" + showPrev +
                ", showNext=" + showNext +
                '}';
    }
}
