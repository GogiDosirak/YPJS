package ypjs.project.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ypjs.project.domain.Item;
import ypjs.project.domain.Like;
import ypjs.project.domain.Member;
import ypjs.project.dto.request.LikeRequestDTO;
import ypjs.project.repository.ItemRepository;
import ypjs.project.repository.LikeRepository;
import ypjs.project.repository.MemberRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LikeService {

    private final LikeRepository likeRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;

    /**
     * 좋아요
     */
    @Transactional
    public void insertLike(LikeRequestDTO likeRequestDTO) throws Exception {

        //엔티티 조회
        Member member = memberRepository.findOne(likeRequestDTO.getMemberId());
        Item item = itemRepository.findOne(likeRequestDTO.getItemId());

        if (member == null) {
            throw new IllegalStateException("회원이 존재하지 않습니다.");
        }
        if(item == null){
            throw new IllegalStateException("상품이 존재하지 않습니다.");
        }
        if (!likeRepository.findByMemberAndItem(member.getMemberId(), item.getItemId()).isEmpty()) {
            throw new IllegalStateException("이미 좋아요를 눌렀습니다");
        }

        //좋아요 생성
        Like like = new Like(member, item);

        //좋아요 저장
        likeRepository.save(like);

    }

    /**
     * 좋아요 취소
     */
    @Transactional
    public void deleteLike(LikeRequestDTO likeRequestDTO) throws Exception {

        //엔티티 조회
        Member member = memberRepository.findOne(likeRequestDTO.getMemberId());
        Item item = itemRepository.findOne(likeRequestDTO.getItemId());

        //확인로직
        if (member == null) {
            throw new IllegalStateException("회원이 존재하지 않습니다.");
        }
        if(item == null){
            throw new IllegalStateException("상품이 존재하지 않습니다.");
        }
        //엔티티 조회
        List <Like> findLike  = likeRepository.findByMemberAndItem(member.getMemberId(), item.getItemId());

        //확인로직
        if(findLike == null){
            throw new IllegalStateException("좋아요한 게시물이 없습니다.");
        }

        //좋아요 취소
        likeRepository.delete(findLike.get(0));
    }

}
