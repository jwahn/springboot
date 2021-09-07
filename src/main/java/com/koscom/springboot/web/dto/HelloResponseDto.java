package com.koscom.springboot.web.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

// ctrl + e : 최근 열었던 파일들 보기
@ToString // toString() override 하는 귀찮은 일 없이, lombok의 toString 기능을 이용할 수 있음
@Getter
@RequiredArgsConstructor //final이 있는 것들에 대해 생성자를 만듦
public class HelloResponseDto {
    private final String name;
    private final int amount;

    // alt + insert 눌러서 생성자 추가해볼 수 있는데. lombok이 있으니까 생성자 별도로 안만들어도 잘 되나 테스트해 보자.
}
