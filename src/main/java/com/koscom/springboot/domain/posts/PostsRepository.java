package com.koscom.springboot.domain.posts;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

//<Posts, Long> ==> <대상이 되는 Entity, PK타입>
//JpaRepository를 상속 받은 인터페이스는 기본 CRUD가 모두 자동 구현된다. 쩔어
public interface PostsRepository extends JpaRepository<Posts, Long> {

    @Query("SELECT p FROM Posts p ORDER BY p.id DESC") //SQL처럼 보이지만 JPA 문법이다. 즉, SELECT p라고 명시해주어야 전체 내용을 가져올 수 있다.
    List<Posts> findAllDesc();
}
