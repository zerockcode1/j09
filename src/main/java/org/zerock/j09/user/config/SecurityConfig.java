package org.zerock.j09.user.config;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.util.AntPathMatcher;
import org.zerock.j09.user.security.CustomAccessDeniedHandler;
import org.zerock.j09.user.security.CustomFailHandler;
import org.zerock.j09.user.security.CustomHttp403ForbiddenEntryPoint;
import org.zerock.j09.user.security.CustomSuccessHandler;
import org.zerock.j09.user.security.filter.ApiCheckFilter;
import org.zerock.j09.user.security.filter.ApiLoginFilter;

@Configuration
@Log4j2
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    CustomSuccessHandler successHandler;

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


        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.addFilterBefore(apiCheckFilter(), UsernamePasswordAuthenticationFilter.class);
        http.addFilterBefore(apiLoginFilter(), UsernamePasswordAuthenticationFilter.class);


    }

    @Bean
    public ApiLoginFilter apiLoginFilter() throws Exception {
        ApiLoginFilter apiLoginFilter = new ApiLoginFilter("/api/login", authenticationManager());
        apiLoginFilter.setAuthenticationFailureHandler(new CustomFailHandler());

        return apiLoginFilter;
    }

    @Bean
    public ApiCheckFilter apiCheckFilter(){
        return new ApiCheckFilter("/member/**/*");
    }

}
