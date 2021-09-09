package com.koscom.springboot.domain.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    // email 주소로 User를 찾아주는 기능입니다
    // 없을수도 있지만, 있을 경우에는 email이 같은 건을 반환한다.
    Optional<User> findByEmail(String email);
}