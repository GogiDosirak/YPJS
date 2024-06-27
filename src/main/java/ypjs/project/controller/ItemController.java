package ypjs.project.controller;


import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ypjs.project.domain.Category;
import ypjs.project.domain.Item;
import ypjs.project.domain.Page;
import ypjs.project.dto.ResponseDto;
import ypjs.project.dto.categorydto.CategoryOneDto;
import ypjs.project.dto.itemdto.*;
import ypjs.project.dto.logindto.LoginDto;
import ypjs.project.dto.noticedto.NoticeDto;
import ypjs.project.repository.ItemReviewRepository;
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



    //item등록
    @PostMapping("/ypjs/item/post")
    public String saveItem(@RequestParam("file") MultipartFile file,
                           @Valid @ModelAttribute  ItemRequestDto requestDto,
                           @Valid @ModelAttribute  ItemFileDto itemFileDto, HttpSession session) throws Exception {
        //멤버정보 찾기
        LoginDto.ResponseLogin responseLogin = (LoginDto.ResponseLogin) session.getAttribute("member");

//        itemService.saveItem(requestDto, responseLogin.getMemberId(), itemFileDto, file);

        //멤버 임시로 넣어 놈
        itemService.saveItem(requestDto, 1L, itemFileDto, file);

        return "redirect:/ypjs/item/get";


    }



    //item1개 조회
    @GetMapping("/ypjs/item/get/{itemId}")
    public String getOneItem (@PathVariable("itemId") Long itemId,
                                  Model model) {

        Item findItem = itemService.findOneItem(itemId);

        ItemOneDto item = new ItemOneDto(findItem);


        model.addAttribute("item", item);

        //조회수
        itemService.increaseItemCnt(itemId);

        //리뷰리스트
        //itemReviewService.findAllItemReview(itemId);



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





    //수정등록
    @PostMapping("/ypjs/item/update/{itemId}")
    public String updateItem(@PathVariable("itemId") Long itemId,
                                    @RequestParam("file") MultipartFile file,
                                    @Valid @ModelAttribute  ItemUpdateDto itemUpdateDto,
                                    @Valid @ModelAttribute  ItemFileDto itemFileDto, Model model, HttpSession session) throws Exception {

        //멤버정보 찾기
        LoginDto.ResponseLogin responseLogin = (LoginDto.ResponseLogin) session.getAttribute("member");

        itemService.findOneItem(itemId);
        itemService.updateItem(itemId, itemUpdateDto, itemFileDto, file);


        return "redirect:/ypjs/item/get/{itemId}";

    }


    //삭제
    @DeleteMapping("/ypjs/item/delete/{itemId}")
    public ResponseEntity deleteItem(@PathVariable("itemId") Long itemId) {
        itemService.deleteItem(itemId);
        return ResponseEntity.ok().build();
    }









}
