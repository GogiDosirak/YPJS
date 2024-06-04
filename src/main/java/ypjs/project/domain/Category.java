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
    private int CategoryId;

    //연관관계 메서드
    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
    private List<Item> items = new ArrayList<>();


    @Column(name = "CATEGORY_NAME")
    private String CategoryName;


    //자신을 부모타입으로 가짐
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CATEGORY_PARENT_ID")
    private Category CategoryParentId;

    //자식은 여러개 가질 수 있음
    @OneToMany(mappedBy = "CategoryParentId")
    private List<Category> CategoryChild = new ArrayList<>();

    //연관관계 메서드
    public void addChildCategory(Category child) {
        this.CategoryChild.add(child);
        child.setCategoryParentId(this);
    }

    private void setCategoryParentId(Category category) {
    }


}