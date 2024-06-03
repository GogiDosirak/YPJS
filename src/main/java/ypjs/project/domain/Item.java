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
    private int ItemId;

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




}