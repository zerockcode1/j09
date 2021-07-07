package org.zerock.j09.user.security.filter;

import com.google.gson.Gson;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;
import org.zerock.j09.user.entity.MemberRefreshToken;
import org.zerock.j09.user.repository.MemberRefreshTokenRepository;
import org.zerock.j09.user.security.util.JWTUtil;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Log4j2
public class ApiRefreshFilter extends OncePerRequestFilter {

    private String pattern;
    private AntPathMatcher antPathMatcher;
    private JWTUtil jwtUtil;

    @Autowired
    private MemberRefreshTokenRepository memberRefreshTokenRepository;



    public ApiRefreshFilter(String pattern){
        this.pattern = pattern;
        this.antPathMatcher = new AntPathMatcher();
        this.jwtUtil = new JWTUtil();
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String requestURI = request.getRequestURI();

        if(antPathMatcher.match(pattern, requestURI) == false){
            filterChain.doFilter(request, response);
            return;
        }

        String refreshValue = request.getParameter("refresh");

        Optional<MemberRefreshToken>  result = memberRefreshTokenRepository.findFirstByRefreshStr(refreshValue);

        try {

            MemberRefreshToken token = result.get();

            String email  = token.getEmail();


            String jwtStr = jwtUtil.generateToken(email);

            String refreshStr = "" + System.currentTimeMillis();

            Map<String, Object> map = new HashMap<>();

            map.put("TOKEN",jwtStr);
            map.put("REFRESH", refreshStr);

            long expireDate = System.currentTimeMillis() + (1000*60*60*30);

            MemberRefreshToken refreshToken = MemberRefreshToken.builder().email(email).refreshStr(refreshStr).expireDate(expireDate).build();

            memberRefreshTokenRepository.save(refreshToken);

            response.setStatus(HttpServletResponse.SC_OK);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);

            Gson gson = new Gson();
            String str = gson.toJson(map);

            response.getWriter().println(str);

        } catch (Exception exception) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);

            Gson gson = new Gson();
            Map<String,Object> errorMap = new HashMap<>();
            errorMap.put("msg", "Login Fail...");
            errorMap.put("code", exception.getClass().getSimpleName());

            String jsonStr = gson.toJson(errorMap);
            log.info(jsonStr);

            response.getWriter().println(jsonStr);
        }


    }
}
