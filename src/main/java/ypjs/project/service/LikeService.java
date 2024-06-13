package ypjs.project.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ypjs.project.domain.Item;
import ypjs.project.domain.Like;
import ypjs.project.domain.Member;
import ypjs.project.dto.request.LikeRequestDTO;
import ypjs.project.dto.response.LikeResponseDTO;
import ypjs.project.repository.ItemRepository;
import ypjs.project.repository.LikeRepository;
import ypjs.project.repository.MemberRepository;

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
    public void toggleLike(LikeRequestDTO likeRequestDTO) {

        //엔티티 조회
        Member member = findMember(likeRequestDTO.getMemberId());
        Item item = findItem(likeRequestDTO.getItemId());

        //좋아요 조회
        Like findLike = likeRepository.findByMemberAndItem(member.getMemberId(), item.getItemId()).orElse(null);

        //좋아요 두번 누른 경우/취소
        if (findLike!=null) {
            likeRepository.delete(findLike);
            LikeResponseDTO.success("좋아요를 취소했습니다!");

        } else {

        //좋아요 처음 누른 경우/저장
            Like like = new Like(member, item);
            likeRepository.save(like);
            LikeResponseDTO.success("좋아요를 눌렀습니다!");
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

