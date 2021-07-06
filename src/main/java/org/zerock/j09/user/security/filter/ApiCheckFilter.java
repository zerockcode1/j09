package org.zerock.j09.user.security.filter;

import lombok.extern.log4j.Log4j2;
import net.minidev.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.zerock.j09.user.security.JWTUtil;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Log4j2
public class ApiCheckFilter extends OncePerRequestFilter {

    private AntPathMatcher antPathMatcher;
    private String pattern;
    private JWTUtil jwtUtil;

    public ApiCheckFilter(String pattern) {
        this.pattern = pattern;
        this.antPathMatcher = new AntPathMatcher();
        this.jwtUtil = new JWTUtil();
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
       log.info("Api Check Filter............................");

       String requestURI = request.getRequestURI();

       log.info(requestURI);

       log.info(antPathMatcher.match(pattern, requestURI));

       if(antPathMatcher.match(pattern, requestURI) == false){

           filterChain.doFilter(request, response);

           return;
       }

        if(checkHeader(request)){
            filterChain.doFilter(request, response);
            return;
        }else {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            // json 리턴
            response.setContentType("application/json;charset=utf-8");
            JSONObject json = new JSONObject();
            String message = "FAIL CHECK API TOKEN";
            json.put("code", "403");
            json.put("message", message);

            PrintWriter out = response.getWriter();
            out.print(json);
            return;
        }

    }

    private boolean checkHeader(HttpServletRequest request){

        String headerValue = request.getHeader("Authorization");

        boolean checkResult = false;

        String authHeader = request.getHeader("Authorization");

        if(StringUtils.hasText(authHeader) && authHeader.startsWith("Bearer ")){
            log.info("Authorization exist: " + authHeader);

            try {
                String email = jwtUtil.validateAndExtract(authHeader.substring(7));
                log.info("validate result: " + email);
                checkResult =  email.length() > 0;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return checkResult;

    }
}
