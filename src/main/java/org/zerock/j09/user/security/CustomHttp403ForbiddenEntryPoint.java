package org.zerock.j09.user.security;

import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Log4j2
public class CustomHttp403ForbiddenEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {

        log.error(authException.getLocalizedMessage());

        log.error(authException.getMessage());



        log.error(request.getRequestURI());
        log.error("403");
        log.error("403");


        response.getWriter().print("LOGIN FIRST PLZ...");
    }

}
