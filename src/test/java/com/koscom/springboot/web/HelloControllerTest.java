package com.koscom.springboot.web;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;

import static org.hamcrest.Matchers.is;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@ExtendWith(SpringExtension.class)
//@WebMvcTest // web에 관련된, controller만 테스트할 때 @SpringBootTest 해도 되긴 하지만 불필요한 것까지 다 끌고 오니까. 간략하게
@WebMvcTest(controllers = HelloController.class) //다른 repository나 이런 것들이 생겼으니까. 이제 다른건 읽지마 라고 세팅해줌
public class HelloControllerTest {

    @Autowired
    MockMvc mvc; //가짜 톰캣을 띄워줄 거다. 좀 전에 한 것처럼 프로젝트 실행할 필요 없다.

    @Test // ctrl + shift + F10
    void hello주소로요청이오면_hello가_리턴된다() throws Exception { //test 코드에서만큼은 메서드명에 한글을 쓰는 것이 허용된다.
        String expectResult = "hello";

        mvc.perform(get("/hello")) // 리퀘스트를 던졌고,
                .andExpect(status().isOk()) // 결과가 왔을 때, httpStatus는 isOk 이고
                .andExpect(content().string(expectResult)); // 수신한 content는 expectResult와 같을 것이다.

        // 눈으로 보는 것보다 훨씬 간편하고, 새로고침해서 보는 것보다 훨씬 낫다.
        // test 코드 자체가 문서가 되고, QA 리스트가 된다. **
    }

    @Test
    void helloDto가_리턴된다() throws Exception {
        String name = "hello";
        int amount = 1000;

        mvc.perform(get("/hello/dto")
                        .param("name", name)
                        .param("amount", String.valueOf(amount)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(name)))
                .andExpect(jsonPath("$.amount", is(amount)));
        //$는 json의 시작 { 를 의미한다.
    }

    @Test
    void amount가없으면_응답코드가_400이_된다() throws Exception {
        String name = "hello";

        mvc.perform(get("/hello/dto")
                        .param("name", name))
                .andExpect(status().isBadRequest());
    }
}
