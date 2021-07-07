package io.seoul.helper.config.auth;

import io.seoul.helper.domain.user.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@RequiredArgsConstructor
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    private final CustomOAuth2UserService customOAuth2UserService;

    @Override //security 반영 설정
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .headers().frameOptions().disable()
                .and()
                .authorizeRequests()
                .antMatchers("/", "/css/**", "/images/**", "/js/**", "/h2-console/**",
                        "/login**", "/home**", "/callback/", "/error")//로그인이 없어도 접근 가능한 구역
                .permitAll()
                .antMatchers(HttpMethod.GET, "/api/v1/**")
                .permitAll()
                .antMatchers(HttpMethod.POST, "/api/v1/batch/**")
                .permitAll()
                .antMatchers("/api/v1/**").hasAnyRole(Role.USER.name(), Role.ADMIN.name())
                .anyRequest().authenticated()
                .and()
                .logout().logoutSuccessUrl("/") //logout에 대한 설정부분
                .and()
                .oauth2Login()              //oauth로 로그인시 작동하게 될 내용
                .defaultSuccessUrl("/", true)
                .userInfoEndpoint().userService(customOAuth2UserService);
    }
}
