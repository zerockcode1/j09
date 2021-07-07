package org.zerock.j09.user.security.util;


import io.jsonwebtoken.*;
import io.jsonwebtoken.impl.DefaultClaims;
import io.jsonwebtoken.impl.DefaultJws;
import lombok.extern.log4j.Log4j2;


import java.io.UnsupportedEncodingException;
import java.time.ZonedDateTime;
import java.util.Date;


@Log4j2
public class JWTUtil {

    private String secretKey = "zerock12345678";

    //1month
    private long expire = 60*24*7;

    public String generateToken(String content) throws Exception{

        return Jwts.builder()
                .setIssuedAt(new Date())
                .setExpiration(Date.from(ZonedDateTime.now().plusMinutes(expire).toInstant()))
                //.setExpiration(Date.from(ZonedDateTime.now().plusSeconds(1).toInstant()))
                .claim("sub", content)
                .signWith(SignatureAlgorithm.HS256, secretKey.getBytes("UTF-8"))
                .compact();
    }

    public String validateAndExtract(String tokenStr)throws ExpiredJwtException, UnsupportedJwtException, MalformedJwtException, SignatureException, IllegalArgumentException {

        String contentValue = null;

        DefaultJws defaultJws = null;
        try {
            defaultJws = (DefaultJws) Jwts.parser()
                    .setSigningKey(secretKey.getBytes("UTF-8")).parseClaimsJws(tokenStr);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        log.info(defaultJws);

        DefaultClaims defaultClaims = (DefaultClaims) defaultJws.getBody();

        log.info(defaultJws.getBody().getClass());
        log.info("------------exp------------------------");

        long expTime = Long.parseLong(defaultClaims.get("exp").toString());



        DefaultClaims claims = (DefaultClaims) defaultJws.getBody();

        log.info("------------------------");

        contentValue = claims.getSubject();


        return contentValue;
    }

}