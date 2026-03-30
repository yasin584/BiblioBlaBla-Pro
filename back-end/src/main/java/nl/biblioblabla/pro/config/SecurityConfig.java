package nl.biblioblabla.pro.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor // Lombok verzorgt de constructor voor jwtAuthenticationFilter
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // 1. CORS aanzetten voordat de rest wordt gecontroleerd
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))

                // 2. CSRF uitzetten (niet nodig voor stateless JWT REST API's)
                .csrf(AbstractHttpConfigurer::disable)

                // 3. Stateless sessies aanzetten (Spring mag de state niet onthouden, JWT doet dat)
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // 4. Autorisatie regels
                .authorizeHttpRequests(auth -> auth
                        // Publieke endpoints (iedereen mag deze aanroepen zonder token)
                        .requestMatchers("/user/login").permitAll()   

                        // Beveiligde endpoints (hier is een geldige JWT token voor nodig)
                        .requestMatchers("/boeken/**").authenticated()
                        .requestMatchers("/leningen/**").authenticated()
                        .requestMatchers(HttpMethod.POST, "/boeken/**").authenticated()

                        // Alles wat we vergeten zijn, is standaard beveiligd
                        .anyRequest().authenticated()
                )

                // 5. Voeg onze eigen JWT filter toe vóór de standaard inlog-filter
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // CORS configuratie bean
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // React frontend toestaan
        configuration.setAllowedOrigins(List.of("http://localhost:8080"));
        
        // Alle HTTP methodes toestaan
        configuration.setAllowedMethods(List.of("GET", "POST", "PATCH"));
        
        // Alle headers toestaan
        configuration.setAllowedHeaders(List.of("*"));
        
        // Credentials (zoals de Authorization header) toestaan
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
}