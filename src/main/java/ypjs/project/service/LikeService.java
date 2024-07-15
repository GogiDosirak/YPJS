package ypjs.project.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ypjs.project.domain.Item;
import ypjs.project.domain.Like;
import ypjs.project.domain.Member;
import ypjs.project.dto.likedto.LikedItemDto;
import ypjs.project.repository.ItemRepository;
import ypjs.project.repository.LikeRepository;
import ypjs.project.repository.MemberRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LikeService {

    private final LikeRepository likeRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;

    /**
     * 좋아요 추가 및 취소
     */
    @Transactional
    public boolean toggleLike(Long memberId, Long itemId) {

        //엔티티 조회
        Member member = findMember(memberId);
        Item item = findItem(itemId);

        //좋아요 조회
        Like findLike = likeRepository.findByMemberAndItem(memberId, itemId).orElse(null);

        //좋아요 두번 누른 경우/취소
        if (findLike!=null) {
            likeRepository.delete(findLike);
            item.deleteLike(); //해당 아이템 엔티티에서 좋아요-1
            return false;
        } else {
            //좋아요 처음 누른 경우/저장
            Like like = new Like(member, item);
            likeRepository.save(like);
            item.addLike(); //해당 아이템 엔티티에서 좋아요+1
            return true;
        }
    }

    @Transactional(readOnly = true)
    public List<LikedItemDto> findAllLikedItemByMemberId(Long memberId, Pageable pageable, String sortBy){
        List<Item> items = likeRepository.findLikedItemByMemberId(memberId, pageable, sortBy);
        List<LikedItemDto> result = items.stream()
                .map(LikedItemDto::new)
                .collect(Collectors.toList());
        return result;
    }

    public int countAllLikedItemByMemberId(Long memberId){
        return likeRepository.countLikedItemByMemberId(memberId);
    }

    private Member findMember(Long memberId) {
        return Optional.ofNullable(memberRepository.findOne(memberId))
                .orElseThrow(() -> new IllegalStateException("회원이 존재하지 않습니다."));
    }

    private Item findItem(Long itemId) {
        return Optional.ofNullable(itemRepository.findOne(itemId))
                .orElseThrow(() -> new IllegalStateException("상품이 존재하지 않습니다."));
    }

//    /**
//     * 좋아요 했는지 확인
//     */
//    @Transactional(readOnly = true)
//    public boolean isLiked(Long memberId, Long itemId){
//        return likeRepository.findByMemberAndItem(memberId,itemId).isPresent();
//    }
}