package org.zerock.j09.user.security.filter;

import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.zerock.j09.user.dto.MemberDTO;
import org.zerock.j09.user.security.JWTUtil;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Log4j2
public class ApiLoginFilter extends AbstractAuthenticationProcessingFilter {

    private JWTUtil jwtUtil;

    public ApiLoginFilter(String defaultFilterProcessesUrl, AuthenticationManager authenticationManager) {
        super(defaultFilterProcessesUrl, authenticationManager);
        this.jwtUtil = new JWTUtil();
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {

        log.info("-------------------------------------");

        String email = request.getParameter("email");
        String pw = request.getParameter("pw");

        UsernamePasswordAuthenticationToken authenticationToken
                 = new UsernamePasswordAuthenticationToken(email,pw);

        Authentication authResult =  this.getAuthenticationManager().authenticate(authenticationToken);

        log.info(authResult);

        return authResult;
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        log.info("-----------------ApiLoginFilter---------------------");
        log.info("successfulAuthentication: " + authResult);

        log.info(authResult.getPrincipal());

        //email address
        String email = ((MemberDTO)authResult.getPrincipal()).getUsername();

        String token = null;
        try {
            token = jwtUtil.generateToken(email);

            response.setContentType("text/plain");
            response.getOutputStream().write(token.getBytes());

            log.info(token);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
