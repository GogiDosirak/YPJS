package ypjs.project.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import ypjs.project.domain.enums.ItemQnaStatus;
import ypjs.project.dto.ItemQnaCreateDto;
import ypjs.project.dto.ItemQnaResponseDto;
import ypjs.project.repository.ItemQnaRepository;
import ypjs.project.repository.ItemRepository;
import ypjs.project.repository.MemberRepository;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class ItemQnaServiceTest {

    @PersistenceContext EntityManager em;

    @Autowired ItemQnaService itemQnaService;
    @Autowired ItemQnaRepository itemQnaRepository;
    @Autowired ItemRepository itemRepository;
    @Autowired MemberRepository memberRepository;

    @Test
    public void 상품문의등록() throws Exception {
        /*Given*/

        ItemQnaCreateDto i = new ItemQnaCreateDto(
                1L,
                1L,
                "문의"
        );

        /*When*/
        Long itemQnaId = itemQnaService.create(i);

        System.out.println("***itemQnaId: " + itemQnaId);

        ItemQnaResponseDto findI = itemQnaService.finById(itemQnaId);

        /*Then*/
        //실패시
        System.out.println(findI);

        assertEquals("상품 문의 등록시 상태는 PENDING", ItemQnaStatus.PENDING,
                findI.getStatus());
        assertEquals("상품 문의 내용이 정확해야 한다.", "문의",
                findI.getQuestion());


        //성공시
        System.out.println("테스트 성공");

    }


}