package ypjs.project.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ypjs.project.domain.Item;
import ypjs.project.domain.Like;
import ypjs.project.domain.Member;
import ypjs.project.dto.likedto.LikeRequestDto;
import ypjs.project.dto.likedto.LikeResponseDto;
import ypjs.project.repository.ItemRepository;
import ypjs.project.repository.LikeRepository;
import ypjs.project.repository.MemberRepository;

import java.util.List;
import java.util.Optional;

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
    public void toggleLike(LikeRequestDto likeRequestDto) {

        //엔티티 조회
        Member member = findMember(likeRequestDto.getMemberId());
        Item item = findItem(likeRequestDto.getItemId());

        //좋아요 조회
        Like findLike = likeRepository.findByMemberAndItem(member.getMemberId(), item.getItemId()).orElse(null);

        //좋아요 두번 누른 경우/취소
        if (findLike!=null) {
            likeRepository.delete(findLike);
            LikeResponseDto.success("좋아요를 취소했습니다!");
            item.deleteLike(); //해당 아이템 엔티티에서 좋아요-1
        } else {

            //좋아요 처음 누른 경우/저장
            Like like = new Like(member, item);
            likeRepository.save(like);
            LikeResponseDto.success("좋아요를 눌렀습니다!");
            item.addLike(); //해당 아이템 엔티티에서 좋아요+1
        }
    }

    private Member findMember(Long memberId) {
        return Optional.ofNullable(memberRepository.findOne(memberId))
                .orElseThrow(() -> new IllegalStateException("회원이 존재하지 않습니다."));
    }

    private Item findItem(Long itemId) {
        return Optional.ofNullable(itemRepository.findOne(itemId))
                .orElseThrow(() -> new IllegalStateException("상품이 존재하지 않습니다."));
    }



}