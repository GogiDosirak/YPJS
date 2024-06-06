package ypjs.project.domain;

import jakarta.persistence.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class Category {


    @Id @GeneratedValue
    @Column(name = "CATEGORY_ID")
    private Long categoryId;

    //연관관계 메서드
    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
    private List<Item> items = new ArrayList<>();


    //자신을 부모타입으로 가짐
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CATEGORY_PARENT_ID")
    private Category categoryParent;

    @Column(name = "CATEGORY_NAME")
    private String categoryName;

    //자식은 여러개 가질 수 있음
    @OneToMany(mappedBy = "categoryParent")
    private List<Category> categoryChild = new ArrayList<>();


    //생성자
    public Category() {}


    public Category (Category categoryParent, String categoryName) {
        this.categoryParent = categoryParent;
        this.categoryName = categoryName;
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