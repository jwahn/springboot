package com.koscom.springboot.web.dto.posts;

import com.koscom.springboot.domain.posts.Posts;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;

@Getter
@NoArgsConstructor
public class PostsResponseDto {
    private Long id;
    private String title;
    private String content;
    private String author;
    //private long sumAmount;
    //private long sumCommentAuthor; //이런 식으로 확장되어 나가게 된다. Posts 기본 entity엔 있을 필요 없는 내용이다.
    
    //Entity는 지켜야 하는 core 이며, 
    //조회(responseDto)의 결과물은 다양한 변화에 대응할 수 있도록 자유롭게 설계할 수 있도록 한다

    //setter도 없고, 변수도 모두 Private이므로, 이런 디자인의 경우엔 사용을 매우 제한했다는 것을 알 수 있다.
    //제한이 없고 많이 열려있는 코드는 여러 사람들이 각자의 상상력으로 자유롭게 쓰고 있기 때문에 나중에 변경하기가 어렵다.
    public PostsResponseDto(Posts posts) {
        this.id = posts.getId();
        this.title = posts.getTitle();
        this.content = posts.getContent();
        this.author = posts.getAuthor();
    }
}
