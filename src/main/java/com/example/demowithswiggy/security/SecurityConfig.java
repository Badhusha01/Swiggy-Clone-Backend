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
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))         
            .authorizeHttpRequests(auth -> auth
                .anyRequest().permitAll() 
            );
        return http.build();
    }
    
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        
        // 🎯 1. Origins-ல கண்டிப்பா டொமைன் லிஸ்ட் மட்டும் தான் இருக்கணும், '*' இருக்க கூடாது!
        configuration.setAllowedOrigins(List.of(
            "http://localhost:5173",
            "https://swiggy-clone-frontend-six.vercel.app"
        )); 
        
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        
        // 🎯 2. Headers-லயும் '*' தூக்கிட்டு, பிரவுசர் கேக்குற எல்லா ஹெடர்ஸையும் தெளிவா குடுத்துடுறோம்!
        configuration.setAllowedHeaders(List.of("Authorization", "Cache-Control", "Content-Type", "Origin", "Accept", "X-Requested-With"));
        
        // 🎯 3. Credentials ட்ரூவாக இருக்கும்போது மேல இருக்குற ரெண்டும் பக்காவா வேலை செய்யும்!
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