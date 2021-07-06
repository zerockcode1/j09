package org.zerock.j09.user.config;

import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.zerock.j09.user.security.CustomAccessDeniedHandler;
import org.zerock.j09.user.security.CustomHttp403ForbiddenEntryPoint;

@Configuration
@Log4j2
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    {
        log.info("SecurityConfig......................");
        log.info("SecurityConfig......................");
        log.info("SecurityConfig......................");
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler(){
        return new CustomAccessDeniedHandler();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        log.info("configure.......................");

        http.authorizeRequests()
                .antMatchers("/sample/all").permitAll()
                .antMatchers("/sample/member").hasRole("USER")
                .antMatchers("/sample/admin").hasRole("ADMIN");

        http.formLogin();
        http.exceptionHandling().accessDeniedHandler(accessDeniedHandler());


        http.csrf().disable();
    }

//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//
//        auth.inMemoryAuthentication()
//                .withUser("user00")
//                .password("$2a$10$aepkuLbOsk2bAkWWFKaOSuFmNQuvwQ38W.bcf2.o3vjpdyQlAmdE6" )
//                .authorities("ROLE_USER");
//    }
}
