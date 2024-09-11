//package techclallenge5.fiap.com.msPagamento.config;
//
//import io.jsonwebtoken.Claims;
//import io.jsonwebtoken.Jwts;
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import javax.crypto.SecretKey;
//import java.io.IOException;
//import java.util.Arrays;
//import java.util.Collection;
//
//public class JwtAuthenticationFilter extends OncePerRequestFilter {
//
//    @Autowired
//    private SecretKey key;
//
//    public JwtAuthenticationFilter(SecretKey jwtSecretkey) {
//        super();
//        this.key = jwtSecretkey;
//    }
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
//            throws ServletException, IOException {
//        String authorizationHeader = request.getHeader("Authorization");
//        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
//            String token = authorizationHeader.substring(7);
//            try {
//                Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
//                String username = claims.getSubject();
//                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
//                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
//                            username, null, createAuthentication(claims));
//                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//                    SecurityContextHolder.getContext().setAuthentication(authentication);
//                }
//            } catch (Exception e) {
//                logger.warn(e.getMessage(), e);
//                SecurityContextHolder.clearContext();
//            }
//        }
//        filterChain.doFilter(request, response);
//    }
//
//    private Collection<? extends GrantedAuthority> createAuthentication(final Claims claims) {
//        var role = claims.get("role");
//        Collection<? extends GrantedAuthority> authorities = role == null ? null : Arrays.asList(() ->""+role);
//        return authorities;
//    }
//}
