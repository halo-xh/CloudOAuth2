package com.xh.oauth.token.provider;

import com.xh.common.utils.JacksonUtils;
import com.xh.common.utils.SnowflakeIdWorker;
import com.xh.oauth.security.authenticate.Oauth2Request;
import com.xh.oauth.token.entity.OAuthClientToken;
import com.xh.oauth.token.service.OAuthClientTokenService;
import com.xh.oauth.utils.RedisUtils;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.security.Key;
import java.util.Date;

/**
 * Created by Xiao Hong on 2021-07-22
 * </p>
 */
@Component
public class JwtClientTokenProvider implements ClientTokenProvider {

    public static final String REQUEST_ID = "requestId";

    private final Logger log = LoggerFactory.getLogger(JwtClientTokenProvider.class);

    //    @Value("${app.config.jwt.key}")
    private String secret = "4BhYGpEnjaYZ446rDeRKhQofxZuHt13OPxd7FVUF9g5QWVtAVvZ8+ldGlxOLs36cVyLHebeE2XXs1/bjpv28iQ==";

    private Key key;

    //    @Value("${app.config.jwt.valid-second-rem}")
    private long tokenValiditySecondsForRememberMe = 300L;

    //    @Value("${app.config.jwt.valid-second}")
    private long tokenValiditySeconds = 300L;

    @Autowired
    private OAuthClientTokenService clientTokenService;

    private final SnowflakeIdWorker idWorker = new SnowflakeIdWorker(3,7);

    @Autowired
    private RedisUtils redisUtils;

//    private TokenStore tokenStore; todo.

    @Override
    public boolean validateToken(String jwt) {
        try {
            Jws<Claims> jws = Jwts.parser().setSigningKey(key).parseClaimsJws(jwt);
            return jws != null;
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
        return false;
    }


    @Override
    public Oauth2Request getPrincipal(String token) {
        Claims claims = Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody();
        String subject = claims.getSubject();
        return JacksonUtils.deserialize(subject, Oauth2Request.class);
    }

    @Override
    public Long createToken(Oauth2Request oauth2Request) {
        Long requestId = oauth2Request.getRequestId();
        if (requestId != null) {
            // already created. use cache.
            String val = redisUtils.getVal(String.valueOf(requestId));
            if (val != null) {
                return requestId;
            }
        }
        final long validity = System.currentTimeMillis() + this.tokenValiditySeconds*1000;
        long id = idWorker.nextId();
        String jwtToken = buildToken(oauth2Request, new Date(validity), id);
        OAuthClientToken clientToken = new OAuthClientToken();
        clientToken.setRequestId(id);
        clientToken.setToken(jwtToken);
        clientToken.setClientId(oauth2Request.getClientId());
        clientToken.setExpireDate(new Date(validity));
        clientTokenService.save(clientToken);
        redisUtils.set(String.valueOf(id), jwtToken, validity);
        return id;
    }

    @Override
    public String getCached(Long reqId) {
        return redisUtils.getVal(String.valueOf(reqId));
    }


    private String buildToken(Oauth2Request oauth2Request, Date validity, Long requestId) {
        String request = JacksonUtils.serialize(oauth2Request);
        return Jwts.builder()
                .setSubject(request)
                .claim(REQUEST_ID, requestId)
                .signWith(key, SignatureAlgorithm.HS512)
                .setExpiration(validity).compact();
    }


    @PostConstruct
    public void init() {
        byte[] keyBytes;
        keyBytes = Decoders.BASE64.decode(secret);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

}
