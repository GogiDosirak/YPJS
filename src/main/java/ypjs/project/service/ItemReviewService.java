package ypjs.project.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ypjs.project.domain.Item;
import ypjs.project.domain.ItemReview;
import ypjs.project.domain.Member;
import ypjs.project.dto.itemdto.ItemReviewDto;
import ypjs.project.dto.itemdto.ItemReviewListDto;
import ypjs.project.repository.ItemRepository;
import ypjs.project.repository.ItemReviewRepository;
import ypjs.project.repository.MemberRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemReviewService {

    private final ItemReviewRepository itemReviewRepository;
    private final ItemRepository itemRepository;
    private final MemberRepository memberRepository;


    //리뷰등록
    @Transactional
    public ItemReview saveItemReview(ItemReviewDto itemReviewDto, Long memberId) {
        Item item = itemRepository.findOne(itemReviewDto.getItemId());
        Member member = memberRepository.findOne(memberId);


        ItemReview itemReview = new ItemReview(
                item,
                member,
                itemReviewDto.getItemScore(),
                itemReviewDto.getItemReviewName(),
                itemReviewDto.getItemReviewContent()
        );

        //리뷰 저장
        itemReviewRepository.saveReview(itemReview);

        // 새로운 리뷰가 추가된 후 아이템의 리뷰 리스트를 업데이트
        item.getItemReviews().add(itemReview);

        //평점 업데이트
        item.updateItemRatings();

        itemRepository.saveItem(item);


        return itemReview;
    }




    //리뷰조회
    public ItemReview findOneItemReview(Long itemReviewId) {
        return itemReviewRepository.findOneReview(itemReviewId);
    }



    public List<ItemReviewListDto> findItemReview(Long itemId, Pageable pageable, String sortBy) {
        List<ItemReview> reviews = itemReviewRepository.findItemReview(itemId, pageable, sortBy);
        //Member member = memberRepository.findOne(memberId);

        List<ItemReviewListDto> result = reviews.stream()
                .map(ItemReviewListDto::new)
                .collect(Collectors.toList());

        return result;
    }


    //멤버 당 리뷰조회
    public List<ItemReviewListDto> findItemReviewByMember(Long memberId, Pageable pageable, String sortBy) {
        List<ItemReview> reviews = itemReviewRepository.findItemReviewByMember(memberId, pageable, sortBy);

        //Member member = memberRepository.findOne(memberId);

        List<ItemReviewListDto> result = reviews.stream()
                .map(ItemReviewListDto::new)
                .collect(Collectors.toList());

        return result;
    }





    //수정
    @Transactional
    public void updateItemReview(Long itemReviewId, ItemReviewDto itemReviewDto) {

        ItemReview itemReview = itemReviewRepository.findOneReview(itemReviewId);

        if (itemReview == null) {
            throw new IllegalArgumentException("ItemReview not found with id: " + itemReviewId);
        }

        Long iRId = itemReview.changeItemReview(
                itemReviewDto.getItemScore(),
                itemReviewDto.getItemReviewName(),
                itemReviewDto.getItemReviewContent()
        );


        //평점 업데이트
        itemReview.getItem().updateItemRatings();

    }



   // 리뷰 삭제
   @Transactional
    public void deleteItemReview(Long itemReviewId) {
        ItemReview itemReview = findOneItemReview(itemReviewId);
        Item item = itemReview.getItem();

        itemReviewRepository.deleteItemReview(itemReviewId);

        item.removeItemReview(itemReview);

        item.updateItemRatings();



    }


    //아이템리뷰 페이징 개수 조회
    public int countAllItemReview(Long itemId){
        return itemReviewRepository.countAllItemReview(itemId);
    }

    //아이템 리뷰 멤버 당 페이징 개수 조회
    public int countAllItemReviewByMember(Long memberId){
        return itemReviewRepository.countAllItemReviewByMember(memberId);
    }


    //리뷰썼을 경우 리뷰쓰기 링크 숨기기
    public ItemReview findItemReviewByItemIdAndMemberId(Long itemId, Long memberId) {

        return itemReviewRepository.findItemReviewByItemIdAndMemberId(itemId, memberId);
    }

    public boolean existsByItemIdAndMemberId(Long itemId, Long memberId) {
        return itemReviewRepository.existsByItemIdAndMemberId(itemId, memberId);
    }







}
