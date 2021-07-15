package com.xh.auth.jwt;

import com.xh.auth.domain.LoginToken;
import com.xh.auth.service.LoginTokenService;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.security.Key;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

/**
 * author  Xiao Hong
 * date  2020/12/6 17:35
 * description
 */

@Component
public class TokenProvider {

    private static final String AUTH = "auth";
    private final Logger log = LoggerFactory.getLogger(TokenProvider.class);


    //    @Value("${app.config.jwt.key}") todo.
    private String secret = "4BhYGpEnjaYZ446rDeRKhQofxZuHt13OPxd7FVUF9g5QWVtAVvZ8+ldGlxOLs36cVyLHebeE2XXs1/bjpv28iQ==";

    //    @Value("${app.config.jwt.valid-second-rem}")
    private long tokenValidityInMillisecondsForRememberMe = 1000 * 3600L;

    //    @Value("${app.config.jwt.valid-second}")
    private long tokenValidityInMilliseconds = 1000 * 36000L;

    private Key key;

    @Autowired
    private LoginTokenService userTokenService;


    public boolean validateToken(String jwt) {
        try {
            Jws<Claims> jws = Jwts.parser().setSigningKey(key).parseClaimsJws(jwt);
            return validateUserToken(jws.getBody(), jwt);
        } catch (SignatureException e) {
            log.info("Invalid JWT signature.");
        } catch (MalformedJwtException e) {
            log.info("Invalid JWT token.");
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT token.");
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT token.");
        } catch (IllegalArgumentException e) {
            log.info("JWT token compact of handler are invalid.");
        }
        SecurityContextHolder.clearContext();
        return false;
    }

    public Authentication getAuthentication(String token) {
        Claims claims = Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody();
        Collection<? extends GrantedAuthority> authorities = null;
        if (StringUtils.isEmpty(claims.get(AUTH).toString())) {
            authorities = new ArrayList<>();
        } else {
            authorities = Arrays.stream(claims.get(AUTH).toString().split(","))
                    .map(SimpleGrantedAuthority::new).collect(Collectors.toList());
        }
        User principal = new User(claims.getSubject(), "", authorities);
        return new UsernamePasswordAuthenticationToken(principal, token, authorities);
    }


    private boolean validateUserToken(Claims jwtBody, String authToken) {
        String subject = jwtBody.getSubject();
        String username = String.valueOf(jwtBody.getSubject());
        boolean existsUserToken = userTokenService.existsUserToken(username, authToken);
        if (!existsUserToken) {
            SecurityContextHolder.clearContext();
            log.info("User session token not found in database for {}, probably logged out by another session.", subject);
        }
        return existsUserToken;
    }

    private String buildToken(Authentication authentication, Date validity) {
        String authorities = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
        String username = authentication.getName();
        return Jwts.builder()
                .setSubject(username)
                .claim(AUTH, authorities)
                .signWith(key, SignatureAlgorithm.HS512)
                .setExpiration(validity).compact();
    }

    public String createToken(Authentication authentication, boolean rememberMe) {
        long now = (new Date()).getTime();
        Date validity;
        if (rememberMe) {
            validity = new Date(now + this.tokenValidityInMillisecondsForRememberMe);
        } else {
            validity = new Date(now + this.tokenValidityInMilliseconds);
        }
        String jwtToken = buildToken(authentication, validity);
        LoginToken loginToken = new LoginToken();
        loginToken.setExpireDate(validity);
        loginToken.setUserName(authentication.getName());
        loginToken.setToken(jwtToken);
        userTokenService.saveToken(loginToken);
        return jwtToken;
    }

    @PostConstruct
    public void init() {
        byte[] keyBytes;
        keyBytes = Decoders.BASE64.decode(this.secret);
        this.key = Keys.hmacShaKeyFor(keyBytes);
//        this.tokenValidityInMilliseconds = 1000 * jwtConfigProperties.getValidSecond();
//        this.tokenValidityInMillisecondsForRememberMe = 1000 * jwtConfigProperties.getValidSecondRem();
    }
}
