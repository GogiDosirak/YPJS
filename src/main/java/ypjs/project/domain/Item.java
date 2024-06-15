package ypjs.project.domain;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class Item {



    @Id @GeneratedValue
    @Column(name = "ITEM_ID")
    private Long itemId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CATEGORY_ID")
    private Category category;

    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL)
    private List<ItemReview> itemReviews = new ArrayList<>();


    @Column(name = "ITEM_NAME")
    private String itemName;

    @Lob
    @Column(name = "ITEM_CONTENT", columnDefinition = "LONGTEXT")
    private String itemContent;

    @Column(name = "ITEM_PRICE")
    private int itemPrice;

    @Column(name = "ITEM_STOCK")
    private int itemStock;

    @Column(name = "ITEM_FILENAME")
    private String itemFilename;

    @Column(name = "ITEM_FILEPATH")
    private String itemFilepath;

    @Column(name = "ITEM_CREATEDATE")
    private LocalDateTime itemCreateDate;

    @Column(name = "ITEM_CNT")
    private int itemCnt;

    @Column(name = "ITEM_RATINGS")
    private double itemRatings;

    @Column(name = "LIKE_CONT")
    private int likeCont;


    //생성자

    public Item() {}

    public Item(Category category, String itemName, String itemContent, int itemPrice, int itemStock) {
        this.category = category;
        this.itemName = itemName;
        this.itemContent = itemContent;
        this.itemPrice = itemPrice;
        this.itemStock = itemStock;

    }


    //상품변경메서드
    public Long changeItem(Category category, String itemName, String itemContent, int itemPrice, int itemStock) {
        this.category = category;
        this.itemName = itemName;
        this.itemContent = itemContent;
        this.itemPrice = itemPrice;
        this.itemStock = itemStock;

        return this.itemId;
    }






    //평점계산
    public void updateItemRatings() {

        if (itemReviews == null || itemReviews.isEmpty()) {
            this.itemRatings = 0;
        }

        double totalScore = 0;
        int count = 0; // 평가된 리뷰의 개수를 세기 위한 변수
        for (ItemReview review : itemReviews) {
            if (review.getItemScore() != 0) {
                //double score = Math.min(Math.max(review.getItemScore(), 1.0), 5.0);
                //totalScore += score;
                totalScore += review.getItemScore();
                count++;
            }
        }
        if (count > 0) {
            double averageScore = totalScore / count;
            // 소숫점 첫째자리까지 반환
            this.itemRatings = Math.round(averageScore * 10.0) / 10.0; //소숫점 첫째자리까지 반환
        } else {
            this.itemRatings = 0;
        }

    }





    //연관관계 메서드
    public void setCategory(Category category) {
        this.category = category;
    }


    public void addItemReview(ItemReview itemReview) {
        itemReviews.add(itemReview);
        itemReview.setItem(this);
    }

    public void removeItemReview(ItemReview itemReview) {
        itemReviews.remove(itemReview);
    }

    //==재고 제거 메서드==//
    public void removeStock(int count) {
        this.ItemStock -= count;
    }

    //==재고 추가 메서드==//
    public void addStock(int count) {
        this.ItemStock += count;
    }


}