package com.example.demowithswiggy.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) 
            // 🔥 Explicit Configuration source-ah மாப் பண்றோம்
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))         
            .authorizeHttpRequests(auth -> auth
                .anyRequest().permitAll() 
            );
        return http.build();
    }
    
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        
        // 🔥 ' * ' போடுறதுக்கு பதிலா உன்னோட லோக்கல் மற்றும் லைவ் வெர்சல் URL-ஐ ஸ்ட்ரெயிட்டா அலோவ் பண்றோம் மாப்ள!
        configuration.setAllowedOrigins(List.of(
            "http://localhost:5173",
            "https://swiggy-clone-frontend-six.vercel.app"
        )); 
        
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        
        // 🔥 இந்த லைன் தான் ரொம்ப முக்கியம்! பிரண்ட்எண்ட் டோக்கன்/க்ரெடென்ஷியல்ஸ் பாஸ் பண்ணா இது ட்ரூவா இருக்கணும்!
        configuration.setAllowCredentials(true); 
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}