package com.koscom.springboot.service;

import com.koscom.springboot.domain.posts.Posts;
import com.koscom.springboot.domain.posts.PostsRepository;
import com.koscom.springboot.web.dto.posts.PostsSaveRequestDto;
import com.koscom.springboot.web.dto.posts.PostsUpdateRequestDto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class PostsServiceTest {

    @Autowired
    PostsRepository postsRepository;

    @Autowired
    PostsService postsService;

    @AfterEach
    void tearDown() {
        //postsRepository.deleteAllInBatch(); //delete from table
        postsRepository.deleteAll(); //JPA 상태를 보고, 자식 테이블까지 삭제할지 결정
    }

    @Test
    void postsService를통해서_저장이된다() {
        Posts save = postsRepository.save(Posts.builder()
                .title("1")
                .content("2")
                .author("3")
                .build()); //기존에 저장된 값이 있어야 update 해서 다시 저장할 수 있다

        System.out.println("save=" + save.getTitle() + " " + save.getContent() + " " + save.getAuthor());

        String title = "test";
        String content = "test2";

        PostsUpdateRequestDto dto = PostsUpdateRequestDto.builder()
                .title(title)
                .content(content)
                .build();

        postsService.update(save.getId(), dto);

//        System.out.println("update=" + save.getTitle() + " " + save.getContent() + " " + save.getAuthor());

        List<Posts> result = postsRepository.findAll();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getTitle()).isEqualTo(title);
        assertThat(result.get(0).getContent()).isEqualTo(content);
    }

    @Test
    void post를_수정하면_수정시간이_갱신된다() {
        Posts save = postsRepository.save(Posts.builder()
                .title("1")
                .content("2")
                .build()); //기존에 저장된 값이 있어야 update 해서 다시 저장할 수 있다

        LocalDateTime beforeTime = save.getModifiedDate();
        System.out.println("beforeTime=" + beforeTime);

        String title = "test";
        String content = "test2";

        PostsUpdateRequestDto dto = PostsUpdateRequestDto.builder()
                .title(title)
                .content(content)
                .build();

        postsService.update(save.getId(), dto);

        List<Posts> result = postsRepository.findAll();

        LocalDateTime newTime = result.get(0).getModifiedDate();
        System.out.println("newTime="+newTime);

        assertThat(newTime).isAfter(beforeTime);

    }
}