package com.koscom.springboot.domain.posts;

import org.springframework.data.jpa.repository.JpaRepository;

//<Posts, Long> ==> <대상이 되는 Entity, PK타입>
//JpaRepository를 상속 받은 인터페이스는 기본 CRUD가 모두 자동 구현된다. 쩔어
public interface PostsRepository extends JpaRepository<Posts, Long> {
}
