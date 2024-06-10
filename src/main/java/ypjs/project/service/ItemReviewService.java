package ypjs.project.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ypjs.project.domain.Item;
import ypjs.project.domain.ItemReview;
import ypjs.project.domain.Member;
import ypjs.project.dto.ItemReviewDto;
import ypjs.project.dto.ItemUpdateDto;
import ypjs.project.repository.ItemRepository;
import ypjs.project.repository.ItemReviewRepository;
import ypjs.project.repository.MemberRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemReviewService {

    private final ItemReviewRepository itemReviewRepository;
    private final ItemRepository itemRepository;
    private final MemberRepository memberRepository;


    //리뷰등록
    @Transactional
    public ItemReview saveItemReview(ItemReviewDto itemReviewDto) {
        Item item = itemRepository.findOne(itemReviewDto.getItemId());
        Member member = memberRepository.findOne(itemReviewDto.getMemberId());


        ItemReview itemReview = new ItemReview(
                item,
                member,
                itemReviewDto.getItemScore(),
                itemReviewDto.getItemReviewName(),
                itemReviewDto.getItemReviewContent()
        );

        itemReviewRepository.saveReview(itemReview);

        return itemReview;
    }


    //리뷰조회
    public ItemReview findOneItemReview(Long itemReviewId) {
        return itemReviewRepository.findOneReview(itemReviewId);
    }



    //수정
    @Transactional
    public void updateItemReview(Long itemReviewId, ItemReviewDto itemReviewDto) {

        ItemReview itemReview = itemReviewRepository.findOneReview(itemReviewId);

        if (itemReview == null) {
            throw new IllegalArgumentException("ItemReview not found with id: " + itemReviewId);
        }

        Item item = itemRepository.findOne(itemReviewDto.getItemId());
        Member member = memberRepository.findOne(itemReviewDto.getMemberId());

        itemReview.changeItemReview(
                item,
                member,
                itemReviewDto.getItemScore(),
                itemReviewDto.getItemReviewName(),
                itemReviewDto.getItemReviewContent()
        );
    }






}
