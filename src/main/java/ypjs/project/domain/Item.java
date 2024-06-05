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
    private Long ItemId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CATEGORY_ID")
    private Category category;


    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL)
    private List<ItemRatings> itemRatings = new ArrayList<>();


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


    //연관관계 메서드
    public void setCategory(Category category) {
        this.category = category;
    }


    public void addItemRatings(ItemRatings itemRating) {
        itemRatings.add(itemRating);
        itemRating.setItem(this);
    }




}