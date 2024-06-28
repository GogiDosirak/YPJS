
package ypjs.project.service;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ypjs.project.domain.Category;
import ypjs.project.domain.Item;
import ypjs.project.domain.Member;
import ypjs.project.dto.itemdto.*;
import ypjs.project.repository.CategoryRepository;
import ypjs.project.repository.ItemRepository;
import ypjs.project.repository.MemberRepository;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;
    private  final CategoryRepository categoryRepository;
    private  final MemberRepository memberRepository;



    //상품등록
    @Transactional
    public Item saveItem(ItemRequestDto itemRequestDto, Long memberId, ItemFileDto itemFileDto, MultipartFile file) throws Exception {
        Category category = categoryRepository.findOneCategory(itemRequestDto.getCategoryId());

        Member member = memberRepository.findOne(memberId);

        String projectPath = System.getProperty("user.dir") + "/src/main/resources/static/files"; // 저장할 경로를 지정
        UUID uuid = UUID.randomUUID(); // 식별자(이름) 랜덤 생성
        String fileName = uuid + "_" + file.getOriginalFilename(); // 랜덤으로 식별자가 붙은 다음에 _원래파일이름(매개변수로 들어온)
        File saveFile = new File(projectPath, fileName); // 파일 껍데기를 생성해줄건데 projectPath 경로에 넣어줄거고 이름은 위의 파일네임 (매개변수 file을 넣어줄 껍데기 생성)
        file.transferTo(saveFile); // Exception 해줘야 밑줄 사라짐


        Item item = new Item(
                category,
                member,
                itemRequestDto.getItemName(),
                itemRequestDto.getItemContent(),
                itemRequestDto.getItemPrice(),
                itemRequestDto.getItemStock());

        // ItemFileDto에 파일 정보 설정 (필드에 직접 접근하는 방법)
        itemFileDto.itemFileName = fileName;
        itemFileDto.itemFilePath = "/files/" + fileName;

        //// Item에 파일 정보 추가
        item.addfile(
                itemFileDto.getItemFileName(), itemFileDto.getItemFilePath()
        );

        itemRepository.saveItem(item);

        return item;
    }




    //단건상품 조회
    public Item findOneItem(Long ItemId) {

        return itemRepository.findOne(ItemId);
    }





    //수정
    @Transactional
    public void updateItem(Long itemId, ItemUpdateDto itemUpdateDto, ItemFileDto itemFileDto,
                           MultipartFile file) throws IOException {
        Item findItem = itemRepository.findOne(itemId);
        Category category = categoryRepository.findOneCategory(itemUpdateDto.getCategoryId());

        if (file.getSize() != 0) { //파일 사이즈가 0이 아닌경우 (즉, 썸네일이 새로 업로드 된 경우에만)
            String projectPath = System.getProperty("user.dir") + "/src/main/resources/static/files";
            UUID uuid = UUID.randomUUID();
            String fileName = uuid + "_" + file.getOriginalFilename();
            File saveFile = new File(projectPath, fileName);
            file.transferTo(saveFile);


            // ItemFileDto에 파일 정보 설정 (필드에 직접 접근하는 방법)
            itemFileDto.itemFileName = fileName;
            itemFileDto.itemFilePath = "/files/" + fileName;

            //// Item에 파일 정보 추가
            findItem.addfile(
                    itemFileDto.getItemFileName(), itemFileDto.getItemFilePath()
            );
        }



        findItem.changeItem(category,
                itemUpdateDto.getItemName(),
                itemUpdateDto.getItemContent(),
                itemUpdateDto.getItemPrice(),
                itemUpdateDto.getItemStock());



    }







    //삭제
    @Transactional
    public void deleteItem(Long itemId) {
        itemRepository.deleteItem(itemId);
    }



    //전체상품 조회
    public List<Item> findAllItems() {
        return itemRepository.findAllItems();
    }


    //조회수
    @Transactional
    public void increaseItemCnt(Long itemId) {
        itemRepository.increaseCnt(itemId);

    }


    //카테고리 당 아이템 조회
    public List<ItemListDto> findAllCategoryItem(Long categoryId, Pageable pageable, String keyword) {
        List<Item> items = itemRepository.findAllItems(categoryId,pageable, keyword);

        List<ItemListDto> result = items.stream()
                .map(ItemListDto::new)
                .collect(Collectors.toList());

        return result;
    }





    //카테고리당 아이템 조회(정렬,검색,페이징)
    public List<ItemListDto> finaAllItemPagingSortBy(Long categoryId, String keyword, Pageable pageable, String sortBy) {
        List<Item> items = itemRepository.findAllItemPagingSortByAndKeyword(categoryId, keyword, pageable, sortBy);

        List<ItemListDto> result = items.stream()
                .map(ItemListDto::new)
                .collect(Collectors.toList());

        return result;

    }



    //아이템 아이디 전체 조회
    public List<ItemListDto> findAllItem(String keyword, Pageable pageable, String sortBy) {
        List<Item> items = itemRepository.findAllItem(keyword, pageable, sortBy);


        List<ItemListDto> result = items.stream()
                .map(ItemListDto::new)
                .collect(Collectors.toList());

        return result;

    }



    //아이템 전체 페이징 개수 조회
    public int countAll(String keyword) {
        return itemRepository.countAll(keyword);
    }


    //카테고리 별 아이템 페이징 개수 조회
    public int countAllCategoryItem(String keyword, Long categoryId) {
        return itemRepository.countAllCategoryItem(keyword,categoryId);
    }




    //리스트 보려고 만듦
//    public List<ItemOneDto> findAll() {
//        List<Item> items = itemRepository.findAllItems();
//
//        List<ItemOneDto> result = items.stream()
//                .map(ItemOneDto::new)
//                .collect(Collectors.toList());
//
//        return result;
//    }




//    public List<ItemListDto> findAllItem(String keyword,  String sortBy) {
//        List<Item> items = itemRepository.findAllItem(keyword, sortBy);
//
//
//        List<ItemListDto> result = items.stream()
//                .map(ItemListDto::new)
//                .collect(Collectors.toList());
//
//        return result;
//
//    }












}
