package com.project.socialnetwork.configuration;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;

import javax.crypto.spec.SecretKeySpec;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Value("${api.prefix}")
    private String prefix;
    @Value("${jwt.secretKey}")
    public String secretKey;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(httpSecurityCsrfConfigurer -> httpSecurityCsrfConfigurer.disable())
                .authorizeHttpRequests(request ->
                        request.requestMatchers(HttpMethod.POST,prefix+"/users/send").permitAll()
                                .requestMatchers(HttpMethod.POST,prefix+"/users/login").permitAll()
                                .requestMatchers(HttpMethod.POST,prefix+"/users/register").permitAll()
                                .requestMatchers(HttpMethod.POST,prefix+"/users/verification").permitAll()
                                .requestMatchers(HttpMethod.GET,prefix+"/users/*/email").permitAll()
                                .requestMatchers(HttpMethod.PUT,prefix+"/users/*/update-password").permitAll()
                                .requestMatchers(HttpMethod.POST,prefix+"/users/search").hasAuthority("SCOPE_ADMIN")
                                .requestMatchers(HttpMethod.PUT,prefix+"/users").hasAuthority("SCOPE_ADMIN")
                                .requestMatchers(HttpMethod.POST,prefix+"/posts/list").hasAuthority("SCOPE_ADMIN")
                                .requestMatchers(HttpMethod.PUT,prefix+"/posts/*/lock").hasAuthority("SCOPE_ADMIN")
                                .requestMatchers(prefix+"/posts/**").hasAnyAuthority("SCOPE_USER","SCOPE_ADMIN")
                                .requestMatchers(prefix+"/comments/**").hasAnyAuthority("SCOPE_USER","SCOPE_ADMIN")
                                .requestMatchers(prefix+"/post-images/**").hasAnyAuthority("SCOPE_USER","SCOPE_ADMIN")
                                .anyRequest().authenticated());
        http.oauth2ResourceServer(oauth2 ->
                oauth2.jwt(jwtConfigurer -> jwtConfigurer.decoder(jwtDecoder())));
        return http.build();
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes(), "HS512");
        NimbusJwtDecoder nimbusJwtDecoder = NimbusJwtDecoder.withSecretKey(secretKeySpec)
                .macAlgorithm(MacAlgorithm.HS512)
                .build();
        return nimbusJwtDecoder;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }
}
