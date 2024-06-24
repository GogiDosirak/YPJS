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
    public ItemQnaDetailDto findOne(Long id) {
        ItemQna itemQna = itemQnaRepository.findOne(id);
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

    //==답변 작성==//
    @Transactional
    public void answer(ItemQnaAnswerDto itemQnaAnswerDto) {
        //상품문의정보 조회
        ItemQna itemQna = itemQnaRepository.findOne(itemQnaAnswerDto.getItemQnaId());

        //멤버정보 조회
        Member member = memberRepository.findOne(itemQnaAnswerDto.getMemberId());

        itemQna.answer(member, itemQnaAnswerDto.getAnswer());

    }


    //==문의 내용 수정==//
    @Transactional
    public void update(ItemQnaUpdateDto itemQnaUpdateDto) {
        //상품문의정보 조회
        ItemQna itemQna = itemQnaRepository.findOne(itemQnaUpdateDto.getItemQnaId());

        if(StringUtils.hasText(itemQnaUpdateDto.getQuestion()) && !StringUtils.hasText(itemQnaUpdateDto.getAnswer())) {
            //질문 수정
            itemQna.updateQ(itemQnaUpdateDto.getQuestion());
        } else if(!StringUtils.hasText(itemQnaUpdateDto.getQuestion()) && StringUtils.hasText(itemQnaUpdateDto.getAnswer())) {
            //답변 수정
            itemQna.updateA(itemQnaUpdateDto.getAnswer());
        }

    }

    //==삭제==//
    @Transactional
    public void delete(Long itemQnaId) {
        ItemQna itemQna = itemQnaRepository.findOne(itemQnaId);
        itemQnaRepository.delete(itemQna);
    }
}
