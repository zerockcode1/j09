package org.zerock.j09.user.security.filter;

import com.google.gson.Gson;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.zerock.j09.user.dto.MemberDTO;
import org.zerock.j09.user.security.util.JWTUtil;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

@Log4j2
public class ApiLoginFilter extends AbstractAuthenticationProcessingFilter {

    public ApiLoginFilter(String defaultFilterProcessesUrl, AuthenticationManager authenticationManager) {
        super(defaultFilterProcessesUrl, authenticationManager);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {

        log.info("=================================");
        log.info("===============attempt login==================");
        log.info("=================================");

        String email = request.getParameter("email");
        String pw = request.getParameter("pw");

        log.info("email: " + email +" pw: " + pw);

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(email,pw);

        Authentication authResult =  this.getAuthenticationManager().authenticate(authenticationToken);

        log.info(this.getAuthenticationManager().getClass().getName());

        log.info(authResult);


        return authResult;
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        log.info("success login after.................");

        Object principal = authResult.getPrincipal();

        log.info(principal);

        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        Map<String, Object> map = new HashMap<>();

        String email = ((MemberDTO)principal).getUsername();

        try {
            String jwt = new JWTUtil().generateToken(email);

            map.put("TOKEN",jwt);

            Gson gson = new Gson();
            String str = gson.toJson(map);

            response.getWriter().println(str);

        } catch (Exception e) {
            e.printStackTrace();
        }




    }
}








