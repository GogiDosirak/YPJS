package ypjs.project.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import ypjs.project.repository.CartRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class CartServiceTest {

    @Autowired CartService cartService;
    @Autowired
    CartRepository cartRepository;

    @Test
    public void 장바구니개수조회() throws Exception {

        Long num = cartService.count(1L);

        System.out.println(num);
    }
}