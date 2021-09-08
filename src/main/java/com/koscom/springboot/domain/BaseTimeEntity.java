package com.koscom.springboot.domain;

import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

// Auditing : createAt, updateAt, createUser, updateUser를 자동으로 관리해주는 기능을 말한다.
@Getter
@MappedSuperclass // (1) JPA에서 컬럼으로 사용하겠다는 뜻
@EntityListeners(AuditingEntityListener.class) // (2)
public abstract class BaseTimeEntity {

    @CreatedDate // (3) 등록시간
    private LocalDateTime createdDate;

    @LastModifiedDate // (4) 수정시간
    private LocalDateTime modifiedDate;

}
