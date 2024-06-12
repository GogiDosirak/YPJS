package ypjs.project.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ypjs.project.domain.Cart;
import ypjs.project.domain.Item;
import ypjs.project.domain.Member;
import ypjs.project.dto.CartAddDto;
import ypjs.project.dto.CartResponseDto;
import ypjs.project.dto.CartUpdateDto;
import ypjs.project.dto.OrderItemDto;
import ypjs.project.repository.CartRepository;
import ypjs.project.repository.ItemRepository;
import ypjs.project.repository.MemberRepository;

import java.util.ArrayList;
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
        Member member = memberRepository.findById(cartAddDto.getMemberId());

        //상품정보 조회
        Item item = itemRepository.findById(cartAddDto.getItemId());

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
        Cart cart = cartRepository.findById(cartUpdateDto.getCartId());

        //수량 변경
        cart.updateItemCount(cartUpdateDto.getItemCount());

    }


    //==멤버별 장바구니 전체 조회==//
    public List<CartResponseDto> findAllByMemberId(Long memberId) {
        List<Cart> carts = cartRepository.findAllByMemberId(memberId);
        List<CartResponseDto> cartDtos  = carts.stream()
                .map(c -> new CartResponseDto(c))
                .collect(toList());

        return cartDtos;
    }


    //==삭제==//
    @Transactional
    public void delete(Long cartId) {
        //장바구니정보 생성
        Cart cart = cartRepository.findById(cartId);
        //장바구니 삭제
        cartRepository.delete(cart);
    }


    //==장바구니 상품으로 주문상품 생성하기==//
    public List<OrderItemDto> createOrderItems(List<CartResponseDto> cartDtos) {
        List<OrderItemDto> orderItemDtos = new ArrayList<>();
        for(CartResponseDto c : cartDtos) {
            OrderItemDto oI = new OrderItemDto(c.getItemId(),c.getItemCount());
            orderItemDtos.add(oI);
        }
        return orderItemDtos;
    }
}
