package ypjs.project.domain;

import jakarta.persistence.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "category")
public class Category {


    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CATEGORY_ID")
    private Long categoryId;

    //연관관계 메서드
    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
    private List<Item> items = new ArrayList<>();


    @Column(name = "CATEGORY_NAME")
    private String categoryName;


    //자신을 부모타입으로 가짐
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CATEGORY_PARENT_ID")
    private Category categoryParent;


    //자식은 여러개 가질 수 있음
    @OneToMany(mappedBy = "categoryParent")
    private List<Category> categoryChild = new ArrayList<>();


    //생성자
    public Category() {}


    public Category (Category categoryParent, String categoryName) {
        this.categoryParent = categoryParent;
        this.categoryName = categoryName;
    }


    //카테고리 부모 롱타입으로
    public void LongCategory(Category categoryParent) {
        this.categoryParent = categoryParent;
    }

    //카테고리 변경 메서드
    public Long changeCategory ( Category categoryParent, String categoryName) {
        this.categoryParent = categoryParent;
        this.categoryName = categoryName;

        return this.categoryId;
    }






    //연관관계 메서드
    public void addChildCategory(Category child) {
        this.categoryChild.add(child);
        child.categoryParent = this;
    }


    public void addItem(Item item) {
        items.add(item);
        item.setCategory(this);
    }


    /*
    private void setCategoryParentId(Category category) {
        category.CategoryParentId = this;
    }
     */


}