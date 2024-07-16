package ypjs.project.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import ypjs.project.domain.Item;
import ypjs.project.domain.ItemQna;
import ypjs.project.domain.Member;
import ypjs.project.dto.itemqnadto.*;
import ypjs.project.repository.ItemQnaRepository;
import ypjs.project.repository.ItemRepository;
import ypjs.project.repository.MemberRepository;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemQnaService {

    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;
    private final ItemQnaRepository itemQnaRepository;


    //==생성==//
    @Transactional
    public Long create(ItemQnaCreateDto itemQnaCreateDto) {

        //상품정보 조회
        Item item = itemRepository.findOne(itemQnaCreateDto.getItemId());

        //멤버정보 조회
        Member member = memberRepository.findOne(itemQnaCreateDto.getMemberId());

        ItemQna itemQna = new ItemQna(
                item,
                member,
                itemQnaCreateDto.getQuestion()
        );

        itemQnaRepository.save(itemQna);

        return itemQna.getItem().getItemId();

    }

    //==조회==//
    public ItemQnaDetailDto findOne(Long itemId) {
        ItemQna itemQna = itemQnaRepository.findOne(itemId);
        return new ItemQnaDetailDto(itemQna);
    }

    public List<ItemQnaSimpleDto> findAllByItemId(Long itemId, Pageable pageable) {
        List<ItemQna> itemQnas = itemQnaRepository.findAllByItemId(itemId, pageable);

        return itemQnas.stream()
                .map(iQ -> new ItemQnaSimpleDto(iQ))
                .collect(toList());
    }

    public List<ItemQnaSimpleDto> findAllByMemberId(Long memberId, Pageable pageable) {
        List<ItemQna> itemQnas = itemQnaRepository.findAllByMemberId(memberId, pageable);

        return itemQnas
                .stream()
                .map(ItemQnaSimpleDto::new)
                .collect(toList());
    }

    public List<ItemQnaDetailDto> findAll(Pageable pageable) {
        List<ItemQna> itemQnas = itemQnaRepository.findAll(pageable);

        return itemQnas
                .stream()
                .map(ItemQnaDetailDto::new)
                .collect(toList());
    }

    //==개수 조회==//
    public int countAll() {
        return itemQnaRepository.countAll();
    }

    public int countByItemId(Long itemId) {
        return itemQnaRepository.countByItemId(itemId);
    }

    public int countByMemberId(Long memberId) {
        return itemQnaRepository.countByMemberId(memberId);
    }

    //==답변 작성==//
    @Transactional
    public void answer(ItemQnaAnswerDto itemQnaAnswerDto) {
        //상품문의정보 조회
        ItemQna itemQna = itemQnaRepository.findOne(itemQnaAnswerDto.getItemQnaId());

        //멤버정보 조회
        Member member = memberRepository.findOne(itemQnaAnswerDto.getMemberId());

        itemQna.answer(member, itemQnaAnswerDto.getAnswer());

    }

    //==삭제==//
    @Transactional
    public void delete(Long itemQnaId) {
        ItemQna itemQna = itemQnaRepository.findOne(itemQnaId);
        itemQnaRepository.delete(itemQna);
    }
}
