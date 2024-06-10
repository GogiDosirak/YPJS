package ypjs.project.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
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

    @Column(name = "Item_Cnt")
    private int itemCnt;

//    @Column(name = "LIKE_CONT")
//    private int likeCont;


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





    //연관관계 메서드
    public void setCategory(Category category) {
        this.category = category;
    }


    public void addItemRatings(ItemReview itemReview) {
        itemReviews.add(itemReview);
        itemReview.setItem(this);
    }




}