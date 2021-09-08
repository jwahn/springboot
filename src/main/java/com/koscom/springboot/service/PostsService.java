package com.koscom.springboot.service;


import com.koscom.springboot.domain.posts.Posts;
import com.koscom.springboot.domain.posts.PostsRepository;
import com.koscom.springboot.web.dto.posts.PostsListResponseDto;
import com.koscom.springboot.web.dto.posts.PostsResponseDto;
import com.koscom.springboot.web.dto.posts.PostsSaveRequestDto;
import com.koscom.springboot.web.dto.posts.PostsUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor //final로 선언된 필드들은 생성자 항목으로 자동 포함시켜서 생성자 생성
@Service //spring bean 등록 & Service 클래스 선언
public class PostsService {
    private final PostsRepository postsRepository;

    // 등록
    @Transactional
    public Long save(PostsSaveRequestDto requestDto) {
        return postsRepository.save(requestDto.toEntity()).getId();
    }

    // 수정
    @Transactional
    public Long update(Long id, PostsUpdateRequestDto dto) {
        Posts entity = postsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id=" + id));

        entity.update(dto.getTitle(), dto.getContent());
        //Q. DB에 반영 안하나요? like
        //postsRepository.save(entity);
        //이게 바로, JPA를 쓰는 이유입니다. @Transactional이 있으면, 이미 한 번 DB에 저장된 적 있는 데이터를 가져와서 수정했을 시
        //Transaction이 끝날 때 알아서 저장해준다.
        //이러한 것을 Dirty Checking(더티 체킹) 이라고 한다.
        //최초에 entity를 가져왔을 때, JPA 내부에서 1차로 cache를 한다.
        //Transaction이 끝나는 시점에, 1차 cache 내용과 다르다면 DB에 update를 날린다.
        //객체의 값만 바꿨는데, DB에 바로 반영이 되었다
        return entity.getId();
    }

    // 조회
    // 조회의 경우엔 서비스가 커질수록 특정 테이블 하나에서만 가져오는 경우가 거의 없다.
    // 때문에 조회는 MyBatis 혹은 Querydsl을 사용하고 (Native 한 쿼리를 작성할 수 있는) + 조회 뿐만 아니라 통계 등에서도.
    // 등록/수정/삭제 는 JPA를 사용하는 방식을 사용한다.
    // JPA의 장점은 모든 DB를 호환할 수 있다는 것이지만, 반대로 모든 DB에 특화된 기능을 100% 지원하지 못하는 것을 단점이라 할 수 있다.
    // ex. Oracle rownum 이 MySQL엔 없기 때문에 JPA 는 rownum을 지원하지 않는다.
    public PostsResponseDto findById(Long id) {
        Posts entity = postsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id=" + id));
        return new PostsResponseDto(entity);
    }

    @Transactional(readOnly = true)
    public List<PostsListResponseDto> findAllDesc() {
        // java 7까지 방식
//        List<Posts> allDesc = postsRepository.findAllDesc();
//        List<PostsListResponseDto> result = new ArrayList<>();
//
//        for (Posts posts : allDesc) {
//            result.add(new PostsListResponseDto(posts));
//        }
//
//        return result;

        // java 8
        return postsRepository.findAllDesc().stream()
                .map(PostsListResponseDto::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public void delete(Long id) {
        Posts posts = postsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다."));

        postsRepository.delete(posts);
    }

}
