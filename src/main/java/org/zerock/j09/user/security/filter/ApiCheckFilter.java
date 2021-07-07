package org.zerock.j09.user.security.filter;

import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;
import org.zerock.j09.user.dto.MemberDTO;
import org.zerock.j09.user.security.util.JWTUtil;
import org.zerock.j09.user.service.MemberDetailsService;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Log4j2
public class ApiCheckFilter extends OncePerRequestFilter {

    private String pattern;
    private AntPathMatcher matcher;
    private JWTUtil jwtUtil;

    @Autowired
    private MemberDetailsService memberDetailsService;

    public ApiCheckFilter(String pattern){
        this.pattern = pattern;
        this.matcher = new AntPathMatcher();
        this.jwtUtil = new JWTUtil();
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        log.info("ApiCheckFilter...............");

        String requestURI = request.getRequestURI();

        boolean matchResult = matcher.match(pattern,requestURI);

        if(matchResult == false){
            log.info("passing...............");
            filterChain.doFilter(request, response);
            return;
        }

        String tokenValue = request.getHeader("Authorization");
        String email = null;
        log.info(tokenValue);

        String jwtStr = tokenValue.substring(7);

        try {
            email = jwtUtil.validateAndExtract(jwtStr);

            log.info("=========extract result: " + email);

            log.info("--------------------------------------------");

        } catch (Exception e) {
            log.error(e.getMessage());
            makeErrorMessage(response, e);
            return;
        }


        checkSecurityContext(email, request);
        filterChain.doFilter(request, response);
    }

    private void checkSecurityContext(String email, HttpServletRequest request) {

        Authentication beforeAuth = SecurityContextHolder.getContext().getAuthentication();

        log.info(beforeAuth);
        log.info("===============================================");

        if (beforeAuth == null) {

            UserDetails userDetails = memberDetailsService.loadUserByUsername(email);

            log.info(userDetails);

            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.getAuthorities());
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            SecurityContextHolder.getContext().setAuthentication(authentication);

        }
    }

    private void makeErrorMessage(HttpServletResponse response, Exception exception){
        try {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            String content = "{\"msg\" : \""+ exception.getClass().getSimpleName()+"\"}";
            response.getWriter().println(content);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
