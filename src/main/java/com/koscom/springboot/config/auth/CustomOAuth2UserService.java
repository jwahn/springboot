package com.koscom.springboot.config.auth;

import com.koscom.springboot.config.auth.dto.OAuthAttributes;
import com.koscom.springboot.config.auth.dto.SessionUser;
import com.koscom.springboot.domain.user.User;
import com.koscom.springboot.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.Collections;

@RequiredArgsConstructor
@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
    private final UserRepository userRepository;
    private final HttpSession httpSession;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId(); // (1)
        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails()
                .getUserInfoEndpoint().getUserNameAttributeName(); // (2)

        OAuthAttributes attributes = OAuthAttributes.of(registrationId, userNameAttributeName, oAuth2User.getAttributes()); // (3)

        User user = saveOrUpdate(attributes);
        httpSession.setAttribute("user", new SessionUser(user)); // (4)

        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority(user.getRoleKey())),
                attributes.getAttributes(),
                attributes.getNameAttributeKey());
    }

    // 가입을 안한 사용자라면 => 가입을 시키고
    // 가입된 사용자라면 => 혹시나 소셜 정보에서 변경된 부분이 있으면 DB에 반영한다
    // 세션에서 인증정보만 저장하면 => 로그인 상태
    private User saveOrUpdate(OAuthAttributes attributes) {
        User user = userRepository.findByEmail(attributes.getEmail())
                .map(savedUser -> savedUser.update(attributes.getName(), attributes.getPicture())) // 있을 경우
                .orElse(attributes.toEntity()); //찾았는데 없으면 새로 user 객체를 만든다

        return userRepository.save(user); // JPA의 save 는 내부적으로 insert or update 를 수행함. 즉 이렇게만 호출하면 알아서 JPA가 insert or update 해준다는 의미
    }
}