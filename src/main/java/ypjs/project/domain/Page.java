package ypjs.project.domain;

public class Page {

    //총 페이지 수 계산 메서드
    public static int totalPages(int TBSize, int pageSize) {
        return TBSize == 0 ? 1 : (int) Math.ceil((double) TBSize / (double) pageSize);
    }
}
