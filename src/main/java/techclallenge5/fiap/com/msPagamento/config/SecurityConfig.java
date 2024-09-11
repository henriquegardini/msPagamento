//package techclallenge5.fiap.com.msPagamento.config;
//
//import io.jsonwebtoken.security.Keys;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//
//import javax.crypto.SecretKey;
//import java.security.Key;
//import java.util.Base64;
//
//@Configuration
////@EnableWebSecurity
//public class SecurityConfig extends AbstractHttpConfigurer {
//
////    @Value("${jwt.publicKey}")
//    private String SECRET = "fYvblTNc8KGo9RB0gWY6f95QNW5fHZkFgQKb3U0pS8Ay0DcN5TO16zRE8P5fK0clJ2zrSaXhgOKdvKIdRRwfDRBbaLTl705bVHUP9xMvpCqDOeXorFAglwv4w3N5eLE1";
//
////    @Bean
////    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
////        http.csrf(AbstractHttpConfigurer::disable);
////        http.authorizeHttpRequests(request -> {
////             request.requestMatchers("/login").permitAll()
////                    .requestMatchers("/payment/**").hasRole("ADMIN")
////                    .requestMatchers("/payment/**").authenticated()
////                     ;
////        });
//////        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
////        return http.build();
////    }
//
//    @Bean
//    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http.formLogin();
//        http.authorizeHttpRequests().anyRequest().authenticated();
//        return http.build();
//    }
//
//    @Bean
//    public JwtAuthenticationFilter jwtAuthenticationFilter() {
//        SecretKey key = jwtSecretKey();
//        return new JwtAuthenticationFilter(key);
//    }
//
//    @Bean
//    public SecretKey jwtSecretKey() {
////        byte[] decodedKey = Base64.getDecoder().decode(SECRET);
//        byte[] decodedKey = SECRET.getBytes();
//        return Keys.hmacShaKeyFor(decodedKey);
//    }
//}
