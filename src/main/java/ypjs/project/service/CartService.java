package ypjs.project.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ypjs.project.domain.Cart;
import ypjs.project.domain.Item;
import ypjs.project.domain.Member;
import ypjs.project.dto.CartAddDto;
import ypjs.project.dto.CartUpdateDto;
import ypjs.project.repository.CartRepository;
import ypjs.project.repository.ItemRepository;
import ypjs.project.repository.MemberRepository;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CartService {

    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;
    private final CartRepository cartRepository;

    //==추가==//
    @Transactional
    public void add(CartAddDto cartAddDto) {
        //멤버정보 조회
        Member member = memberRepository.findOne(cartAddDto.getMemberId());

        //상품정보 조회
        Item item = itemRepository.findOne(cartAddDto.getItemId());

        //장바구니 생성
        Cart cart = new Cart(
                member,
                item,
                cartAddDto.getItemCount()
        );

        //장바구니 추가
        cartRepository.save(cart);
    }

    //==상품수량 변경==//
    @Transactional
    public void update(CartUpdateDto cartUpdateDto) {
        //장바구니정보 조회
        Cart cart = cartRepository.findOne(cartUpdateDto.getCartId());

        //수량 변경
        cart.updateItemCount(cartUpdateDto.getItemCount());

    }


    //==멤버별 장바구니 전체 조회==//
    public List<Cart> findAllWithMemberId (Long memberId) {
        return cartRepository.findAllWithMemberId(memberId);
    }


    //==삭제==//
    @Transactional
    public void delete(Long cartId) {
        //장바구니정보 생성
        Cart cart = cartRepository.findOne(cartId);
        //장바구니 삭제
        cartRepository.delete(cart);
    }
}
