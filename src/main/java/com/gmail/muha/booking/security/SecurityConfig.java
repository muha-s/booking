package com.gmail.muha.booking.security;

import com.gmail.muha.booking.model.entity.enums.UserRole;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            JwtAuthenticationConverter jwtAuthenticationConverter,
            CorsConfigurationSource corsConfigurationSource) throws Exception {

        http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(corsConfigurationSource))
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> authorize

                        .requestMatchers(HttpMethod.POST, "/auth/login").permitAll()
                        .requestMatchers(HttpMethod.GET, "/auth/me").authenticated()

                        .requestMatchers(HttpMethod.POST, "/users").permitAll()
                        .requestMatchers(HttpMethod.POST, "/users/verify-email").permitAll()
                        .requestMatchers(HttpMethod.POST, "/users/restore-request").permitAll()
                        .requestMatchers(HttpMethod.POST, "/users/restore").permitAll()

                        .requestMatchers(HttpMethod.POST, "/hotel-admin/activate").permitAll()

                        .requestMatchers(HttpMethod.GET, "/hotel-admin/hotels", "/hotel-admin/hotels/**")
                        .hasRole(UserRole.HOTEL_ADMIN.name())

                        .requestMatchers(HttpMethod.POST, "/hotel-admin/hotels/**")
                        .hasRole(UserRole.HOTEL_ADMIN.name())

                        .requestMatchers(HttpMethod.PUT, "/hotel-admin/hotels/**")
                        .hasRole(UserRole.HOTEL_ADMIN.name())

                        .requestMatchers(HttpMethod.DELETE, "/hotel-admin/hotels/**")
                        .hasRole(UserRole.HOTEL_ADMIN.name())

                        .requestMatchers(HttpMethod.GET, "/users/me")
                        .hasRole(UserRole.USER.name())

                        .requestMatchers(HttpMethod.PUT, "/users/me", "/users/me/**")
                        .hasRole(UserRole.USER.name())

                        .requestMatchers(HttpMethod.DELETE, "/users/me")
                        .hasRole(UserRole.USER.name())

                        .requestMatchers(HttpMethod.GET, "/cities").permitAll()

                        .requestMatchers(HttpMethod.POST, "/cities")
                        .hasRole(UserRole.SUPER_ADMIN.name())

                        .requestMatchers(HttpMethod.DELETE, "/cities/**")
                        .hasRole(UserRole.SUPER_ADMIN.name())

                        .requestMatchers(HttpMethod.GET, "/hotels").permitAll()

                        .requestMatchers(HttpMethod.POST, "/hotels")
                        .hasRole(UserRole.SUPER_ADMIN.name())

                        .requestMatchers(HttpMethod.DELETE, "/hotels/**")
                        .hasRole(UserRole.SUPER_ADMIN.name())

                        .requestMatchers(HttpMethod.GET, "/rooms/available").permitAll()

                        .requestMatchers(HttpMethod.GET, "/bookings/my", "/bookings/my/**")
                        .hasRole(UserRole.USER.name())

                        .requestMatchers(HttpMethod.POST, "/bookings")
                        .hasRole(UserRole.USER.name())

                        .requestMatchers(HttpMethod.PUT, "/bookings/my/**")
                        .hasRole(UserRole.USER.name())

                        .requestMatchers(HttpMethod.DELETE, "/bookings/my/**")
                        .hasRole(UserRole.USER.name())

                        .requestMatchers(HttpMethod.POST, "/hotel-reviews")
                        .hasRole(UserRole.USER.name())

                        .requestMatchers(HttpMethod.GET, "/hotel-reviews/hotel/**")
                        .hasRole(UserRole.USER.name())

                        .requestMatchers("/super-admin/**")
                        .hasRole(UserRole.SUPER_ADMIN.name())

                        .anyRequest().denyAll()
                )
                .oauth2ResourceServer(oauth2 ->
                        oauth2.jwt(jwt ->
                                jwt.jwtAuthenticationConverter(jwtAuthenticationConverter)));

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            UserDetailsService userDetailsService,
            PasswordEncoder passwordEncoder) {

        DaoAuthenticationProvider authenticationProvider =
                new DaoAuthenticationProvider(userDetailsService);

        authenticationProvider.setPasswordEncoder(passwordEncoder);

        return new ProviderManager(authenticationProvider);
    }

    @Bean
    public SecretKey jwtSecretKey(@Value("${app.jwt.secret}") String secret) {
        return new SecretKeySpec(
                secret.getBytes(StandardCharsets.UTF_8),
                "HmacSHA256"
        );
    }

    @Bean
    public JwtEncoder jwtEncoder(SecretKey secretKey) {
        return NimbusJwtEncoder.withSecretKey(secretKey).build();
    }

    @Bean
    public JwtDecoder jwtDecoder(SecretKey secretKey) {
        return NimbusJwtDecoder.withSecretKey(secretKey).build();
    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {

        JwtGrantedAuthoritiesConverter authoritiesConverter =
                new JwtGrantedAuthoritiesConverter();

        authoritiesConverter.setAuthoritiesClaimName("roles");
        authoritiesConverter.setAuthorityPrefix("");

        JwtAuthenticationConverter authenticationConverter =
                new JwtAuthenticationConverter();

        authenticationConverter.setJwtGrantedAuthoritiesConverter(
                authoritiesConverter
        );

        return authenticationConverter;
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOrigins(
                List.of("http://localhost:4200")
        );

        configuration.setAllowedMethods(
                List.of("GET", "POST", "PUT", "DELETE", "OPTIONS")
        );

        configuration.setAllowedHeaders(List.of("*"));

        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
}