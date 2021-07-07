package org.zerock.j09.user.security.filter;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
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

    @Autowired
    private MemberDetailsService memberDetailsService;

    public ApiCheckFilter(String pattern){
        this.pattern = pattern;
        this.matcher = new AntPathMatcher();
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        log.info("ApiCheckFilter...............");
        log.info("ApiCheckFilter...............");
        log.info("ApiCheckFilter...............");

        String requestURI = request.getRequestURI();
        boolean matchResult = matcher.match(pattern,requestURI);

        if(matchResult == false){
            log.info("passing...............");
            filterChain.doFilter(request, response);
            return;
        }

        log.info("check target................." + memberDetailsService);

        String tokenValue = request.getHeader("Authorization");

        log.info(tokenValue);

        if(tokenValue != null ){

            String jwtStr = tokenValue.substring(7);

            try {
                String email =  new JWTUtil().validateAndExtract(jwtStr);

                log.info("=========extract result: " + email);

                UserDetails userDetails = memberDetailsService.loadUserByUsername(email);

                log.info(userDetails);

                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authentication);
                filterChain.doFilter(request, response);

            } catch (Exception e) {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                String content ="{\"msg\" : \"TOKEN ERROR\"}";
                response.getWriter().println(content);
            }

        }else {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            String content ="{\"msg\" : \"TOKEN ERROR\"}";
            response.getWriter().println(content);
        }//end if else


    }
}
