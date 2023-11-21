package security.jwt;
import com.salesianos.triana.VaxConnectApi.user.modal.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.Data;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import security.errorhandling.JwtTokenException;

import javax.crypto.SecretKey;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.UUID;

import static org.yaml.snakeyaml.tokens.Token.ID.Key;

@Log
@Service
public class JwtProvider {

    public static final String TOKEN_TYPE = "JWT";
    public static final String TOKEN_HEADER = "Authorizacion";
    public static final String TOKEN_PREFIX = "Bearer";

    @Value("${jwt.secret=secretoiberico}")
    private String jwtSecret;

    @Value("${jwt.duraction=1}")
    private int jfwLifeInDays;

    private JwtParser jwtParser;

    private SecretKey secretKey;

    @PostConstruct
    public void init(){
        secretKey = Keys.hmacShaKeyFor(jwtSecret.getBytes());

        jwtParser = Jwts.parser().verifyWith(secretKey).build();
    }

    public String generateToken(Authentication authentication){
        User user = (User) authentication.getPrincipal();
        return generateToken(user);

    }

    public String generateToken(User user){
        Date tokenExpirationTimeDate =
                Date.from(
                        LocalDateTime
                                .now()
                                .plusDays(jfwLifeInDays)
                                .atZone(ZoneId.systemDefault())
                                .toInstant()
                );

        return Jwts.builder()
                .header()
                .type(TOKEN_TYPE)
                .and()
                .subject(user.getId().toString())
                .issuedAt(tokenExpirationTimeDate)
                .signWith(secretKey)
                .compact();

    }

    public UUID getUserIdFromJwtToken(String token){

        return UUID.fromString(
                jwtParser.parseSignedClaims(token).getPayload().getSubject()
        );

    }

    public boolean validateToken(String token){

        try {
            jwtParser.parse(token);
            return true;
        }catch (SignatureException | MalformedJwtException | ExpiredJwtException | UnsupportedJwtException | IllegalArgumentException ex){
            log.info("Error con el token: " + ex.getMessage());
            throw new JwtTokenException(ex.getMessage());
        }


    }






}
