package com.koscom.springboot.domain.posts;

import com.koscom.springboot.domain.BaseTimeEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor // 디폴트 생성자, 필수로 입력
@Entity
public class Posts extends BaseTimeEntity {  // 테이블의 1개 행을 담는 그릇 vs. 도메인 클래스 (후자에 비중)
    @Id // pk
    @GeneratedValue(strategy = GenerationType.IDENTITY) // PK 채번 방식
    private Long id; // pk (auto increment, bigint)

    @Column(length = 500, nullable = false) //varchar(500), not null
    private String title;

    @Column(length = 2000, nullable = false) //varchar(2000), not null
    private String content;

    private String author; // @Column 이 없어도 자동으로 컬럼 선언은 됨. String일 경우 자동으로 varchar(255), nullable=true 로 설정됨

    @Builder //lombok의 builder. 어느 자리에 어떤 값이 들어갈 건지를 명확하게 표현할 수 있어서 더욱 좋다.
    public Posts(String title, String content, String author) { //id가 없고 나머지 필드만 있는 생성자
        this.title = title;
        this.content = content;
        this.author = author;
    }

    //Q. builder는 무엇인가?
    //Q. setter는 왜 없는가? 최신 Trend 에서는 setter를 쓰지 말자 쪽으로 가고 있다고 함. setter 없이 값을 어떻게 바꾸지??

    //title과 content만 수정 가능하다
    //author는 수정 가능하지 않다.
    //수정일자도 신규로 생성된다.
    public void update(String title, String content) {
        this.title = title;
        this.content = content;
    }

}
