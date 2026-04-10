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
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // CORS aanzetten voordat de rest wordt gecontroleerd
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))

                // CSRF uitzetten (niet nodig voor stateless JWT REST API's)
                .csrf(AbstractHttpConfigurer::disable)

                // Stateless sessies aanzetten (Spring mag de state niet onthouden, JWT doet dat)
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                // Autorisatie regels
                .authorizeHttpRequests(auth -> auth
                    // Publieke endpoints
                    .requestMatchers("/auth/login", "/auth/register").permitAll()

                    // Boeken
                    .requestMatchers(HttpMethod.GET, "/boeken/**").authenticated()
                    .requestMatchers("/boeken/**").authenticated()

                    // Leningen
                    .requestMatchers("/leningen/**").authenticated()
                    .requestMatchers(HttpMethod.GET, "/leningen/gebruiker/**").authenticated()   // Filteren op gebruiker
                    .requestMatchers(HttpMethod.GET, "/leningen/filter/zoek").authenticated()    // Zoeken op titel of auteur
                    .requestMatchers(HttpMethod.GET, "/leningen/filter/genre").authenticated()   // Filteren op genre
                    .requestMatchers(HttpMethod.GET, "/leningen/filter/datum").authenticated()   // Filteren op datum

                     // Default: alles wat niet expliciet genoemd is
                    .anyRequest().authenticated()
                )

                // Voeg onze eigen JWT filter toe vóór de standaard inlog-filter
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // CORS configuratie bean
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // React frontend toestaan
        configuration.setAllowedOrigins(List.of("http://localhost:5173"));
        
        // HTTP methodes toestaan
        configuration.setAllowedMethods(List.of("GET", "POST", "PATCH", "OPTIONS"));
        
        // Alle headers toestaan
        configuration.setAllowedHeaders(List.of("*"));
        
        // Credentials (zoals de Authorization header) toestaan
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
}
