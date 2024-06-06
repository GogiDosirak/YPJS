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

@Service
@RequiredArgsConstructor
public class LikeService {

    private final LikeRepository likeRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;

    @Transactional
    public void insertLike(LikeRequestDTO likeRequestDTO) throws Exception {
        Member member = memberRepository.findOne(likeRequestDTO.getMemberId());
        Item item = itemRepository.findOne(likeRequestDTO.getItemId());
        if (member == null) {
            throw new IllegalStateException("회원이 존재하지 않습니다.");
        }
        if(item == null){
            throw new IllegalStateException("상품이 존재하지 않습니다.");
        }
        if (likeRepository.findByMemberAndItem(member.getMemberId(), item.getItemId()) != null) {
            throw new IllegalStateException("이미 좋아요를 눌렀습니다");
        }

        Like like = new Like(member, item);

        likeRepository.save(like);

    }

    @Transactional
    public void deleteLike(LikeRequestDTO likeRequestDTO) throws Exception {
        Member member = memberRepository.findOne(likeRequestDTO.getMemberId());
        Item item = itemRepository.findOne(likeRequestDTO.getItemId());
        if (member == null) {
            throw new IllegalStateException("회원이 존재하지 않습니다.");
        }
        if(item == null){
            throw new IllegalStateException("상품이 존재하지 않습니다.");
        }
        Like like = likeRepository.findByMemberAndItem(member.getMemberId(), item.getItemId());
        if(like == null){
            throw new IllegalStateException("좋아요한 게시물이 없습니다.");
        }

        likeRepository.delete(like);
    }

}
