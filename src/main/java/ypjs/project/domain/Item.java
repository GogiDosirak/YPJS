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
    private List<ItemRatings> ItemRatings = new ArrayList<>();


    @Column(name = "ITEM_NAME")
    private String ItemName;

    @Lob
    @Column(name = "ITEM_CONTENT", columnDefinition = "LONGTEXT")
    private String ItemContent;

    @Column(name = "ITEM_PRICE")
    private int ItemPrice;

    @Column(name = "ITEM_STOCK")
    private int ItemStock;

    @Column(name = "ITEM_FILENAME")
    private String ItemFilename;

    @Column(name = "ITEM_FILEPATH")
    private String ItemFilepath;

    @Column(name = "ITEM_CREATEDATE")
    private LocalDateTime ItemCreateDate;

    @Column(name = "LIKE_CONT") //처음에 0값을 넣는걸로 만들었구, 관련 메서드에 문제생기면 수현에게 연락바람
    private int likeCont = 0;

    //==좋아요 메서드==//
    public void addLike(){
        this.likeCont += 1;
    }

    public void deleteLike(){
        if (this.likeCont > 0) {
            this.likeCont -= 1;
        }
    }


}