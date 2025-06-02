package finmanage.core.security;


import finmanage.core.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtTokenProvider {

    private static final Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);

    @Value("${app.jwt.secret}")
    private String jwtSecretString; // Store actual key securely, this is just for placeholder

    @Value("${app.jwt.expiration-ms}")
    private int jwtExpirationInMs;

    private SecretKey getSigningKey() {
        // Ensure the secret is strong enough for HS256, or use HS512
        // For HS256, the key must be at least 256 bits (32 bytes)
        // This is a simplified key generation. In production, use a securely generated and stored key.
        byte[] keyBytes = jwtSecretString.getBytes();
        if (keyBytes.length < 32) {
            logger.warn("JWT secret key is less than 32 bytes. This is not secure for HS256.");
            // Pad or generate a proper key. For now, let's ensure it's at least 32 bytes if too short for demonstration.
            byte[] paddedKeyBytes = new byte[32];
            System.arraycopy(keyBytes, 0, paddedKeyBytes, 0, Math.min(keyBytes.length, 32));
            keyBytes = paddedKeyBytes;

        }
        return Keys.hmacShaKeyFor(keyBytes);
    }


    public String generateToken(Authentication authentication) {
        User userPrincipal = (User) authentication.getPrincipal();
        return generateTokenFromUser(userPrincipal);
    }

    public String generateTokenFromUser(User userPrincipal) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationInMs);

        return Jwts.builder()
                .setSubject(Long.toString(userPrincipal.getId()))
                .claim("username", userPrincipal.getUsername())
                .claim("role", userPrincipal.getRole())
                .setIssuedAt(new Date())
                .setExpiration(expiryDate)
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }


    public Long getUserIdFromJWT(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
        return Long.parseLong(claims.getSubject());
    }

    public String getUsernameFromJWT(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.get("username", String.class);
    }


    public boolean validateToken(String authToken) {
        try {
            Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(authToken);
            return true;
        } catch (SignatureException ex) {
            logger.error("Invalid JWT signature");
        } catch (MalformedJwtException ex) {
            logger.error("Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            logger.error("Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            logger.error("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            logger.error("JWT claims string is empty.");
        }
        return false;
    }

    public String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}