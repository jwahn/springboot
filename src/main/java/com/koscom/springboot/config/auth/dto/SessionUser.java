package com.koscom.springboot.config.auth.dto;

import com.koscom.springboot.domain.user.User;
import lombok.Getter;

import java.io.Serializable;

@Getter
public class SessionUser implements Serializable {
    //기본적으로 SessionUser와 User 정보는 유사하지만, 별도로 분리한 이유는
    //직렬화 했을 때 용량 관리를 컴팩트하게 하기 위함
    //세션 정보는 간소하게 관리하는 것이 여러모로 낫겠지?
    private String name;
    private String email;
    private String picture;

    public SessionUser(User user) {
        this.name = user.getName();
        this.email = user.getEmail();
        this.picture = user.getPicture();
    }
}