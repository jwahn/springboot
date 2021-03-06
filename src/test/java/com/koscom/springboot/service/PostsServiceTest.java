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
import java.util.Optional;

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

    @Test
    void postService를_통해서_삭제가_된다() {
        Posts save = postsRepository.save(Posts.builder()
                .title("delete test")
                .content("delete content")
                .build());
        postsService.delete(save.getId());
        List<Posts> resultAfter = postsRepository.findAll();
        assertThat(resultAfter).hasSize(0); //삭제 후 조회 시 아무 것도 없어야 함
    }

    @Test
    void id가_일치해야만_삭제가_된다() {
        Posts save = postsRepository.save(Posts.builder()
                .title("delete test")
                .content("delete content")
                .build());

        Posts deleteTarget = postsRepository.save(Posts.builder()
                .title("delete test")
                .content("delete content")
                .build());

        postsService.delete(deleteTarget.getId()); //삭제하려는 대상이 아닌 다른 posts의 아이디를 넣어본다
        Optional<Posts> resultById = postsRepository.findById(deleteTarget.getId()); //Optional은 null이 반환될 수도 있다는 의미이다
        assertThat(resultById.isPresent()).isFalse(); //삭제 후 조회 시 아무 것도 없어야 함
//        assertThat(resultById.get().getId()).isEqualTo(deleteTarget.getId());
    }
}