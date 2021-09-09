package com.koscom.springboot.config.auth;

import com.koscom.springboot.domain.user.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@RequiredArgsConstructor
@EnableWebSecurity // (1)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final CustomOAuth2UserService customOAuth2UserService;

    @Value("${security.enabled:true}")
    private boolean securityEnabled;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .headers().frameOptions().disable() // (2)
                .and()
                    .authorizeRequests() // (3)
                        .antMatchers("/", "/css/**", "/images/**", "/js/**", "/h2-console/**").permitAll()
                        .antMatchers("/api/v1/**").hasRole(Role.USER.name()) // (4) 인가: 최소 USER Role을 가지고 있어야 사용 가능, 즉 비가입된 사용자는 사용 불가능
                        .anyRequest().authenticated() // (5) 인증, 로그인만 되면 상관없음
                .and()
                    .logout()
                        .logoutSuccessUrl("/") // (6) 로그아웃 성공시 리다이렉트
                .and()
                    .oauth2Login() // (7)
                        .userInfoEndpoint() // (8)
                            .userService(customOAuth2UserService); // (9)
    }

    // 테스트에서 사용할 경우 security 무시
    @Override
    public void configure(WebSecurity web) throws Exception {
        if(!securityEnabled) { //securityEnabled가 false면, 위의 보안은 무시하겠다. (TEST 할 때는 false로 세팅하면 된다.)
            web.ignoring().antMatchers("/**");
        }
    }
}