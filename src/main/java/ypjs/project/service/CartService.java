package ypjs.project.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ypjs.project.domain.Cart;
import ypjs.project.domain.Item;
import ypjs.project.domain.Member;
import ypjs.project.dto.cartdto.CartAddDto;
import ypjs.project.dto.cartdto.CartListDto;
import ypjs.project.dto.cartdto.CartUpdateDto;
import ypjs.project.repository.CartRepository;
import ypjs.project.repository.ItemRepository;
import ypjs.project.repository.MemberRepository;

import java.util.List;

import static java.util.stream.Collectors.toList;

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

    //==멤버별 장바구니 개수 조회==//
    public Long count(Long memberId) {
        return cartRepository.count(memberId);

        //return cartRepository.findAllByMemberId(memberId).size();
        //select 문으로 전체 정보를 다 검색해와서 부적합
    }


    //==멤버별 장바구니 전체 조회==//
    public List<CartListDto> findAllByMemberId(Long memberId) {
        List<Cart> carts = cartRepository.findAllByMemberId(memberId);
        List<CartListDto> cartDtos  = carts.stream()
                .map(c -> new CartListDto(c))
                .collect(toList());

        return cartDtos;
    }


    //==멤버별 중복 상품 조회==//
    public List<Long> findItemIdByMemberId(Long memberId) {
        return cartRepository.findItemIdByMemberId(memberId);
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
