package ypjs.project.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import ypjs.project.domain.Category;
import ypjs.project.domain.Item;
import ypjs.project.domain.Page;
import ypjs.project.dto.itemdto.ItemListDto;
import ypjs.project.dto.itemdto.ItemOneDto;
import ypjs.project.dto.itemdto.ItemUpdateDto;
import ypjs.project.service.CategoryService;
import ypjs.project.service.ItemReviewService;
import ypjs.project.service.ItemService;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;
    private final ItemReviewService itemReviewService;
    private final CategoryService categoryService;



    //item등록 화면
    @GetMapping("/ypjs/item/post")
    public String insert() {return "item/itemPost";}



    //item1개 조회
    @GetMapping("/ypjs/item/get/{itemId}")
    public String getOneItem (@PathVariable("itemId") Long itemId,
                              Model model) {

        Item findItem = itemService.findOneItem(itemId);

        ItemOneDto item = new ItemOneDto(findItem);


        model.addAttribute("item", item);

        //조회수
        itemService.increaseItemCnt(itemId);

        //리뷰 갯수
        int reviewCount = itemReviewService.countAllItemReview(itemId);
        model.addAttribute("reviewCount", reviewCount);

        //임시 맴버아이디//하드코딩//바꿔치기 해야함
        Long memberId = 1L;
        model.addAttribute("memberId", memberId);

        return "item/itemGet";

    }



    //category당 아이템 조회
//    @GetMapping("/ypjs/categoryItem/get/{categoryId}")
//    public CategoryOneDto getOneCategory(@PathVariable("categoryId") Long categoryId) {
//
//        Category category =  categoryService.findOneCategory(categoryId);
//
//       List<ItemListDto> items = itemService.findAllItem(categoryId);
//
//        return new  CategoryOneDto(category, items);
//    }





    //카테고리당 아이템 조회(정렬,검색,페이징(페이징받아서 페이징))
    @GetMapping("/ypjs/categoryItem/get/{categoryId}")
    public String getAllCategoryItem(@PathVariable("categoryId") Long categoryId,
                                     @RequestParam(value = "page",defaultValue = "0") int page,
                                     @RequestParam(value = "size",defaultValue = "3") int size,
                                     @RequestParam(value = "sortBy", defaultValue = "itemId") String sortBy,
                                     @RequestParam(value = "keyword", required = false) String keyword,
                                     Model model) {

        Pageable pageable = PageRequest.of(page, size);

        Category category = categoryService.findOneCategory(categoryId);

        List<ItemListDto> items = itemService.finaAllItemPagingSortBy(categoryId, keyword, pageable, sortBy);


        //총 페이지 수 계산
        int totalPages = Page.totalPages(itemService.countAllCategoryItem(keyword, categoryId), size);

        //카테고리
        List<Category> parentCategories = categoryService.findParentCategories();
        model.addAttribute("parentCategories", parentCategories);

        //임시 맴버아이디//하드코딩//바꿔치기 해야함
        Long memberId = 1L;
        model.addAttribute("memberId", memberId);

        model.addAttribute("items",items);
        model.addAttribute("category", category);
        model.addAttribute("sortBy", sortBy); // 정렬 옵션을 다시 모델에 추가
        model.addAttribute("keyword", keyword); //검색조건 유지
        model.addAttribute("page",page); //페이징
        model.addAttribute("size",size); //페이징
        model.addAttribute("totalPages", totalPages); //총 페이지 수





        return "item/itemCategoryList";


    }



    //아이템 전체 조회
    @GetMapping("/ypjs/item/get")
    public String getAllItem(
            @RequestParam(value = "page",defaultValue = "0") int page,
            @RequestParam(value = "size",defaultValue = "3") int size,
            @RequestParam(value = "sortBy", defaultValue = "itemId") String sortBy,
            @RequestParam(value = "keyword", required = false) String keyword,
            Model model) {

        Pageable pageable = PageRequest.of(page, size);

        List<ItemListDto> items = itemService.findAllItem(keyword, pageable, sortBy);

        //총 페이지 수 계산
        int totalPages = Page.totalPages(itemService.countAll(keyword), size);

        //카테고리
        List<Category> parentCategories = categoryService.findParentCategories();
        model.addAttribute("parentCategories", parentCategories);

        //임시 맴버아이디//하드코딩//바꿔치기 해야함
        Long memberId = 1L;
        model.addAttribute("memberId", memberId);

        model.addAttribute("items", items);
        model.addAttribute("sortBy", sortBy); // 정렬 옵션을 다시 모델에 추가
        model.addAttribute("keyword", keyword); //검색조건 유지
        model.addAttribute("page",page); //페이징
        model.addAttribute("size",size); //페이징
        model.addAttribute("totalPages", totalPages); //총 페이지 수



        return "item/itemList";


    }




    //수정보기
    @GetMapping("/ypjs/item/update/{itemId}")
    public String udateItem(@PathVariable("itemId") Long itemId, Model model) {



        Item findItem = itemService.findOneItem(itemId);

        System.out.println(findItem);

        ItemUpdateDto item =  new ItemUpdateDto(
                findItem.getItemId(),
                findItem.getCategory().getCategoryId(),
                findItem.getItemName(),
                findItem.getItemContent(),
                findItem.getItemPrice(),
                findItem.getItemStock());

        model.addAttribute("item", item);



        return "item/itemUpdate";
    }





}
